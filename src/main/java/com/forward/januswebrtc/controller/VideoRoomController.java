package com.forward.januswebrtc.controller;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.common.JanusWebSocket;
import com.forward.januswebrtc.common.KeepAliveThread;
import com.forward.januswebrtc.domain.Response;
import com.forward.januswebrtc.domain.bean.RTCJsep;
import com.forward.januswebrtc.domain.common.CreateSessionRequest;
import com.forward.januswebrtc.domain.common.CreateSessionResponse;
import com.forward.januswebrtc.domain.plugin.PluginHandleCreateRequest;
import com.forward.januswebrtc.domain.plugin.PluginHandleCreateResponse;
import com.forward.januswebrtc.domain.plugin.videocall.VideoCallRequestBody;
import com.forward.januswebrtc.domain.plugin.videoroom.*;
import com.forward.januswebrtc.util.JanusClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
public class VideoRoomController {
    @RequestMapping("second")
    public String index(){
        return "videoroom";
    }

    @RequestMapping("createRoom")
    @ResponseBody
    public String CreateRoom(String username) throws ExecutionException, InterruptedException {
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient(username);
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        KeepAliveThread keepAliveThread = new KeepAliveThread(janusWebSocket);
        keepAliveThread.start();
        janusWebSocket.setKeepAliveThread(keepAliveThread);
        long sessionId = response.getId();
        janusWebSocket.setSession_id(sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setPlugin("janus.plugin.videoroom");
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
        long handleId = pluginHandleCreateResponse.getId();
        janusWebSocket.setHandle_id(handleId);
        VideoRoomMessageRequest videoCallMessageRequest = new VideoRoomMessageRequest();
        videoCallMessageRequest.setJanus("message");
        videoCallMessageRequest.setSession_id(sessionId);
        videoCallMessageRequest.setHandle_id(handleId);
        videoCallMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoCallRequestBody videoCallRequestBody = new VideoCallRequestBody();
        videoCallRequestBody.setRequest("create");
        videoCallMessageRequest.setBody(videoCallRequestBody);
        janusWebSocket.sendMessage(videoCallMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        return JSON.toJSONString(response1);
    }

    @RequestMapping("publishJoinRoom")
    @ResponseBody
    public String PublishJoinRoom(String username,String roomid) throws ExecutionException, InterruptedException{
        JanusWebSocket janusWebSocket = JanusClientUtil.getJanusClient(username);
        VideoRoomMessageRequest videoRoomMessageRequest = new VideoRoomMessageRequest();
        videoRoomMessageRequest.setJanus("message");
        videoRoomMessageRequest.setSession_id(janusWebSocket.getSession_id());
        videoRoomMessageRequest.setHandle_id(janusWebSocket.getHandle_id());
        videoRoomMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoRoomPublisherJoinRequestBody videoRoomPublisherJoinRequestBody = new VideoRoomPublisherJoinRequestBody();
        videoRoomPublisherJoinRequestBody.setRequest("join");
        videoRoomPublisherJoinRequestBody.setRoom(Long.valueOf(roomid));
        videoRoomMessageRequest.setBody(videoRoomPublisherJoinRequestBody);
        janusWebSocket.sendMessage(videoRoomMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        return "success";
    }

    @RequestMapping("publish")
    @ResponseBody
    public String Publish(String username,String rtcjsep) throws ExecutionException, InterruptedException{
        JanusWebSocket janusWebSocket = JanusClientUtil.getJanusClient(username);
        VideoRoomMessageRequest videoRoomMessageRequest = new VideoRoomMessageRequest();
        videoRoomMessageRequest.setJanus("message");
        videoRoomMessageRequest.setSession_id(janusWebSocket.getSession_id());
        videoRoomMessageRequest.setHandle_id(janusWebSocket.getHandle_id());
        videoRoomMessageRequest.setTransaction(UUID.randomUUID().toString());
        videoRoomMessageRequest.setJsep(JSON.parseObject(rtcjsep, RTCJsep.class));
        VideoRoomPublisherPublishRequestBody videoRoomPublisherPublishRequestBody = new VideoRoomPublisherPublishRequestBody();
        videoRoomPublisherPublishRequestBody.setRequest("publish");
        videoRoomMessageRequest.setBody(videoRoomPublisherPublishRequestBody);
        janusWebSocket.sendMessage(videoRoomMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        return JSON.toJSONString(response1);
    }

    @RequestMapping("subscribeJoinRoom")
    @ResponseBody
    public String SubscribeJoinRoom(String username,String roomid,String publisherid) throws ExecutionException, InterruptedException{
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient(username);
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        KeepAliveThread keepAliveThread = new KeepAliveThread(janusWebSocket);
        keepAliveThread.start();
        janusWebSocket.setKeepAliveThread(keepAliveThread);
        long sessionId = response.getId();
        janusWebSocket.setSession_id(sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setPlugin("janus.plugin.videoroom");
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
        long handleId = pluginHandleCreateResponse.getId();
        janusWebSocket.setHandle_id(handleId);
        VideoRoomMessageRequest videoRoomMessageRequest = new VideoRoomMessageRequest();
        videoRoomMessageRequest.setJanus("message");
        videoRoomMessageRequest.setSession_id(janusWebSocket.getSession_id());
        videoRoomMessageRequest.setHandle_id(janusWebSocket.getHandle_id());
        videoRoomMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoRoomSubscriberJoinRequestBody videoRoomSubscriberJoinRequestBody = new VideoRoomSubscriberJoinRequestBody();
        videoRoomSubscriberJoinRequestBody.setRequest("join");
        videoRoomSubscriberJoinRequestBody.setRoom(Long.valueOf(roomid));
        videoRoomSubscriberJoinRequestBody.setFeed(Long.valueOf(publisherid));
        videoRoomMessageRequest.setBody(videoRoomSubscriberJoinRequestBody);
        janusWebSocket.sendMessage(videoRoomMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        return "success";
    }

    @RequestMapping("startSubscribe")
    @ResponseBody
    public String StartSubscribe(String username,String rtcjsep) throws ExecutionException, InterruptedException{
        JanusWebSocket janusWebSocket = JanusClientUtil.getJanusClient(username);
        VideoRoomMessageRequest videoRoomMessageRequest = new VideoRoomMessageRequest();
        videoRoomMessageRequest.setJanus("message");
        videoRoomMessageRequest.setSession_id(janusWebSocket.getSession_id());
        videoRoomMessageRequest.setHandle_id(janusWebSocket.getHandle_id());
        videoRoomMessageRequest.setTransaction(UUID.randomUUID().toString());
        videoRoomMessageRequest.setJsep(JSON.parseObject(rtcjsep, RTCJsep.class));
        VideoRoomSubscriberStartRequestBody videoRoomSubscriberStartRequestBody = new VideoRoomSubscriberStartRequestBody();
        videoRoomSubscriberStartRequestBody.setRequest("start");
        videoRoomMessageRequest.setBody(videoRoomSubscriberStartRequestBody);
        janusWebSocket.sendMessage(videoRoomMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        return "success";
    }
}
