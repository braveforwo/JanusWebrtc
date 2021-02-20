package com.forward.januswebrtc.controller;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.common.JanusWebSocket;
import com.forward.januswebrtc.common.KeepAliveThread;
import com.forward.januswebrtc.domain.Event;
import com.forward.januswebrtc.domain.Response;
import com.forward.januswebrtc.domain.bean.RTCJsep;
import com.forward.januswebrtc.domain.common.CreateSessionRequest;
import com.forward.januswebrtc.domain.common.CreateSessionResponse;
import com.forward.januswebrtc.domain.common.JanusEvent;
import com.forward.januswebrtc.domain.plugin.PluginHandleCreateRequest;
import com.forward.januswebrtc.domain.plugin.PluginHandleCreateResponse;
import com.forward.januswebrtc.domain.plugin.videocall.VideoCallMessageRequest;
import com.forward.januswebrtc.domain.plugin.videocall.VideoCallRequestBody;
import com.forward.januswebrtc.util.JanusClientUtil;
import com.forward.januswebrtc.util.MessageManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

@Controller
public class VideoCallController {
    @RequestMapping("index")
    public String index(){
        return "video";
    }

    /**
     * 推送事件
     * @param username 用户名
     * @param response response
     */
    @GetMapping(value = "getPush")
    public void getData(String username, HttpServletResponse response){
        JanusWebSocket janusWebSocket = JanusClientUtil.getJanusClient(username);
        //获取事件队列
        BlockingQueue blockingQueue = MessageManager.THE_ONLY_ONE_MANAGER.getNeedToPush().get(janusWebSocket);
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");
        try{
            PrintWriter pw = response.getWriter();
            while (true){
                if (pw.checkError()){
                    System.out.println("客户端断开连接");
                    break;
                }
                //获取事件
                Event event = (Event) blockingQueue.take();
//                System.out.println(username+JSON.toJSONString(event));
                //传递到前端
                pw.println("data:"+JSON.toJSONString(event));
                pw.println();
                pw.flush();
            }
        }catch (InterruptedException | IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 接受通话
     * @param username 注册的用户名唯一性
     * @param rtcjsep answer
     * @return 返回
     * @throws InterruptedException h
     * @throws ExecutionException h
     */
    @RequestMapping("acceptSomeOne")
    @ResponseBody
    public String AcceptSomeOne(String username, String rtcjsep) throws InterruptedException, ExecutionException {
//        System.out.println(rtcjsep);
//        System.out.println(username);
        JanusWebSocket janusWebSocket = JanusClientUtil.getJanusClient(username);
        VideoCallMessageRequest videoCallMessageRequest = new VideoCallMessageRequest();
        videoCallMessageRequest.setJanus("message");
        videoCallMessageRequest.setSession_id(janusWebSocket.getSession_id());
        videoCallMessageRequest.setHandle_id(janusWebSocket.getHandle_id());
        videoCallMessageRequest.setTransaction(UUID.randomUUID().toString());
        videoCallMessageRequest.setJsep(JSON.parseObject(rtcjsep,RTCJsep.class));
        VideoCallRequestBody videoCallRequestBody = new VideoCallRequestBody();
        videoCallRequestBody.setRequest("accept");
        videoCallMessageRequest.setBody(videoCallRequestBody);
        janusWebSocket.sendMessage(videoCallMessageRequest);
        Response janusEvent = janusWebSocket.getResponse().get();
        MessageManager.THE_ONLY_ONE_MANAGER.getNeedToPush().get(janusWebSocket).add((Event) janusEvent);
        if(janusEvent.uid()!=null) return JSON.toJSONString(janusEvent);
        return null;
    }

    /**
     * 请求通话
     * @param username 注册的用户名唯一性
     * @param called 被呼叫的用户名
     * @param rtcjsep offer
     * @return 返回
     * @throws InterruptedException h
     * @throws ExecutionException h
     */
    @RequestMapping("callSomeOne")
    @ResponseBody
    public String CallSomeOne(String username,String called, String rtcjsep) throws InterruptedException, ExecutionException {
//        System.out.println(rtcjsep);
        JanusWebSocket janusWebSocket = JanusClientUtil.getJanusClient(username);
        VideoCallMessageRequest videoCallMessageRequest = new VideoCallMessageRequest();
        videoCallMessageRequest.setJanus("message");
        videoCallMessageRequest.setSession_id(janusWebSocket.getSession_id());
        videoCallMessageRequest.setHandle_id(janusWebSocket.getHandle_id());
        videoCallMessageRequest.setTransaction(UUID.randomUUID().toString());
        videoCallMessageRequest.setJsep(JSON.parseObject(rtcjsep,RTCJsep.class));
        VideoCallRequestBody videoCallRequestBody = new VideoCallRequestBody();
        videoCallRequestBody.setRequest("call");
        videoCallRequestBody.setUsername(called);
        videoCallMessageRequest.setBody(videoCallRequestBody);
//        System.out.println(videoCallMessageRequest.turnToString());
        janusWebSocket.sendMessage(videoCallMessageRequest);
        Response janusEvent = janusWebSocket.getResponse().get();
        MessageManager.THE_ONLY_ONE_MANAGER.getNeedToPush().get(janusWebSocket).add((Event) janusEvent);
        if(janusEvent.uid()!=null) return JSON.toJSONString(janusEvent);
        return null;
    }

    @RequestMapping("hangup")
    @ResponseBody
    public String Hangup(String username) throws ExecutionException, InterruptedException {
        JanusWebSocket janusWebSocket = JanusClientUtil.getJanusClient(username);
        VideoCallMessageRequest videoCallMessageRequest = new VideoCallMessageRequest();
        videoCallMessageRequest.setJanus("message");
        videoCallMessageRequest.setSession_id(janusWebSocket.getSession_id());
        videoCallMessageRequest.setHandle_id(janusWebSocket.getHandle_id());
        videoCallMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoCallRequestBody videoCallRequestBody = new VideoCallRequestBody();
        videoCallRequestBody.setRequest("hangup");
        videoCallMessageRequest.setBody(videoCallRequestBody);
        janusWebSocket.sendMessage(videoCallMessageRequest);
        Response janusEvent = janusWebSocket.getResponse().get();
        MessageManager.THE_ONLY_ONE_MANAGER.getNeedToPush().get(janusWebSocket).add((Event) janusEvent);
        return JSON.toJSONString(janusEvent);
    }

    /**
     * 创建会话绑定videocall插件并注册用户名
     * @param username 用户名唯一性
     * @return h
     * @throws ExecutionException h
     * @throws InterruptedException h
     */
    @RequestMapping("register")
    @ResponseBody
    public String register(String username) throws ExecutionException, InterruptedException {
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient(username);
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        long sessionId = response.getId();
        KeepAliveThread keepAliveThread = new KeepAliveThread(janusWebSocket);
        janusWebSocket.setKeepAliveThread(keepAliveThread);
        keepAliveThread.start();
        janusWebSocket.setSession_id(sessionId);
//        System.out.println("sessionid=="+sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
//        System.out.println(JSON.toJSONString(pluginHandleCreateResponse));
        long handleId = pluginHandleCreateResponse.getId();
        janusWebSocket.setHandle_id(handleId);
        VideoCallMessageRequest videoCallMessageRequest = new VideoCallMessageRequest();
        videoCallMessageRequest.setJanus("message");
        videoCallMessageRequest.setSession_id(sessionId);
        videoCallMessageRequest.setHandle_id(handleId);
        videoCallMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoCallRequestBody videoCallRequestBody = new VideoCallRequestBody();
        videoCallRequestBody.setRequest("register");
        videoCallRequestBody.setUsername(username);
        videoCallMessageRequest.setBody(videoCallRequestBody);
        janusWebSocket.sendMessage(videoCallMessageRequest);
        JanusEvent janusEvent = (JanusEvent)janusWebSocket.getResponse().get();
        MessageManager.THE_ONLY_ONE_MANAGER.getNeedToPush().get(janusWebSocket).add(janusEvent);
        if(janusEvent.getTransaction()!=null) return "success";
        return "";
    }
}
