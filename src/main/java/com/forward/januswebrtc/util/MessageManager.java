package com.forward.januswebrtc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.forward.januswebrtc.common.JanusWebSocket;
import com.forward.januswebrtc.domain.Event;
import com.forward.januswebrtc.domain.Request;
import com.forward.januswebrtc.domain.Response;
import com.forward.januswebrtc.domain.common.JanusEvent;
import com.forward.januswebrtc.domain.common.JanusResponse;
import com.forward.januswebrtc.domain.constant.PluginConstant;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 消息处理工具
 */
public class MessageManager {
    /**
     * 工具唯一对象
     */
    public final static MessageManager THE_ONLY_ONE_MANAGER = new MessageManager();
    /**
     * 保存所有会话的每个请求
     */
    private ConcurrentHashMap<Request, CompletableFuture<Response>> pendingRequest;
    /**
     * 保存所有会话的响应
     */
    private BlockingQueue<Response> responses;
    /**
     * 保存每个会话对应的需要推送至前端的事件
     */
    private ConcurrentHashMap<JanusWebSocket,BlockingQueue<Event>> needToPush;
    private static volatile Boolean running = true;

    public ConcurrentHashMap<JanusWebSocket, BlockingQueue<Event>> getNeedToPush() {
        return needToPush;
    }

    public void setNeedToPush(ConcurrentHashMap<JanusWebSocket, BlockingQueue<Event>> needToPush) {
        this.needToPush = needToPush;
    }

    private MessageManager(){
        this.pendingRequest = new ConcurrentHashMap<>();
        this.responses = new ArrayBlockingQueue<Response>(10000);
        this.needToPush = new ConcurrentHashMap<>();
        //开始处理响应
        startProcessResponse();
    }

    //每个janusWebSocket创建时会调用这个方法，用来初始化当前会话所需要推送事件的阻塞队列
    public void initNeedToPush(JanusWebSocket janusWebSocket){
        needToPush.put(janusWebSocket,new ArrayBlockingQueue<>(1000));
    }

    /**
     * 提交每个响应并进行json转化为对应的对象，每个janusWebSocket onmessage接受消息时都会调用这个方法
     * @param janusWebSocket 调用当前方法的janusWebSocket
     * @param message janusWebSocket的接收消息
     */
    public  void commitResponseMessage(JanusWebSocket janusWebSocket,String message){
        JSONObject jsonObject = JSON.parseObject(message);
        //获取当前响应的transaction具有唯一性
        String uid = (String)jsonObject.get("transaction");

        Request request = null;
        //通过uid获取对应的请求
        if(uid!=null) request = getRelatedRequestByUid(uid);

        try {
            //可以分为两大类，请求响应，janus主动发消息
            if (request != null) {//对应请求不为空则为第一大类
                if ("success".equals(jsonObject.get("janus"))) {
                    responses.add(Objects.requireNonNull(ReflectUtil.getCommonResponse(request)).turnToObject(message));
                } else if ("error".equals(jsonObject.get("janus"))) {
                    responses.add(JSON.parseObject(message, JanusResponse.class));
                } else if ("event".equals(jsonObject.getString("janus"))) {
//                    String eventType = JSON.parseObject(jsonObject.getJSONObject("plugindata").getString("data")).getJSONObject("result").getString("event");
                    responses.add(JSON.parseObject(message, JanusEvent.class));
                    needToPush.get(janusWebSocket).add(JSON.parseObject(message, JanusEvent.class));
                    //请求响应的event事件可以由controller判断推送，不再由这里判断是否推送前端，整个commitResponseMessage只做消息转化成对应的对象
//                    if (eventType!=null&&PluginConstant.isNeedToPush(eventType)) {
//                        needToPush.get(janusWebSocket).add(JSON.parseObject(message, JanusEvent.class));
//                    }
                } else {
                    responses.add(JSON.parseObject(message, ReflectUtil.getEventResponse(jsonObject.getString("janus")).getClass()));
                }

            } else {//为空则为第二大类
                if ("event".equals(jsonObject.getString("janus"))) {
//                    String eventType = JSON.parseObject(jsonObject.getJSONObject("plugindata").getString("data")).getJSONObject("result").getString("event");
//                    responses.add(JSON.parseObject(message, JanusEvent.class));
                    //一股脑把全部janus主动发过来的事件全部推送至前端，不同插件有各自的逻辑前端，不应由后端处理
                    needToPush.get(janusWebSocket).add(JSON.parseObject(message, JanusEvent.class));
                    //判断该event是否需要推送至前端
//                    if (PluginConstant.isNeedToPush(eventType)) {
//                        needToPush.get(janusWebSocket).add(JSON.parseObject(message, JanusEvent.class));
//                    }
                } else {
                    responses.add(JSON.parseObject(message, ReflectUtil.getEventResponse(jsonObject.getString("janus")).getClass()));
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void pendRequestMessage(Request request, CompletableFuture<Response> response){
        pendingRequest.put(request,response);
    }


    private void startProcessResponse(){
        ThreadExecutorUtil.pool.execute(new Runnable() {
            @Override
            public void run() {
                   while(running){
                       Response resp = null;
                       try {
                           resp = responses.take();
                           final Response ref = resp;
                           //判断是否为使用插件前的响应
                           if(ref.isSuccess()){
                               onCompeleted(ref);
                           }else{
                               //获取响应对应的请求

                               Request request = getRelatedRequest(ref);
                               //如果有对应的请求则返回结果，没有则不处理即丢弃比如ack，slowlink等等
                               if(request!=null) {
                                   //判断是否为单次响应
                                   isOnceResponse(request.hasOnceResponse(),ref,request);
                               }
                           }
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
            }

        });
    }
    private void onCompeleted(Response ref) {
        Request req = getRelatedRequest(ref);
        CompletableFuture<Response> resp = (CompletableFuture<Response>) pendingRequest.get(req);
        //返回响应对象
        resp.complete(ref);
        pendingRequest.remove(req);
    }

    private void isOnceResponse(Boolean once,Response ref,Request request){
        //有些请求会返回两次结果，一般ack不重要就不执行直接不处理丢弃
        if(once||!"ack".equals(JSON.parseObject(JSON.toJSONString(ref)).getString("janus"))){
            CompletableFuture<Response> resp = (CompletableFuture<Response>) pendingRequest.get(request);
            resp.complete(ref);
            pendingRequest.remove(request);
        }
    }

    private Request getRelatedRequest(Response response){
        for (Iterator<Request> it = pendingRequest.keySet().iterator(); it.hasNext(); ) {
            Request request = it.next();
            if (request.uid().equals(response.uid())) return request;
        }
        return null;
    }

    private Request getRelatedRequestByUid(String uid){
        for (Iterator<Request> it = pendingRequest.keySet().iterator(); it.hasNext(); ) {
            Request request = it.next();
            if (request.uid().equals(uid)) return request;
        }
        return null;
    }

}
