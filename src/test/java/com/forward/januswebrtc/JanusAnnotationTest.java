package com.forward.januswebrtc;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.common.JanusWebSocket;
import com.forward.januswebrtc.domain.Request;
import com.forward.januswebrtc.domain.Response;
import com.forward.januswebrtc.domain.bean.RTCJsep;
import com.forward.januswebrtc.domain.common.CreateSessionRequest;

import com.forward.januswebrtc.domain.common.CreateSessionResponse;
import com.forward.januswebrtc.domain.plugin.PluginHandleCreateRequest;
import com.forward.januswebrtc.domain.plugin.PluginHandleCreateResponse;
import com.forward.januswebrtc.domain.plugin.videocall.VideoCallMessageRequest;
import com.forward.januswebrtc.domain.plugin.videocall.VideoCallRequestBody;
import com.forward.januswebrtc.domain.plugin.videoroom.VideoRoomDestroyRequestBody;
import com.forward.januswebrtc.domain.plugin.videoroom.VideoRoomMessageRequest;
import com.forward.januswebrtc.domain.plugin.videoroom.VideoRoomPublisherJoinRequestBody;
import com.forward.januswebrtc.util.JanusClientUtil;
import org.junit.Test;

import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class JanusAnnotationTest {
    long room;
    @Test
    public void test() throws InterruptedException, ExecutionException {
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient("456");
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        long sessionId = response.getId();
        System.out.println("sessionid=="+sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setPlugin("janus.plugin.videoroom");
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
        System.out.println(JSON.toJSONString(pluginHandleCreateResponse));
        long handleId = pluginHandleCreateResponse.getId();
        VideoRoomMessageRequest videoCallMessageRequest = new VideoRoomMessageRequest();
        videoCallMessageRequest.setJanus("message");
        videoCallMessageRequest.setSession_id(sessionId);
        videoCallMessageRequest.setHandle_id(handleId);
        videoCallMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoCallRequestBody videoCallRequestBody = new VideoCallRequestBody();
        videoCallRequestBody.setRequest("create");
//        videoCallRequestBody.setUsername("123");
        videoCallMessageRequest.setBody(videoCallRequestBody);
        janusWebSocket.sendMessage(videoCallMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        System.out.println(JSON.toJSONString(response1));

    }
    @Test
    public void createRoom() throws  InterruptedException, ExecutionException {
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient("123");
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        long sessionId = response.getId();
        System.out.println("sessionid=="+sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setPlugin("janus.plugin.videoroom");
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
        System.out.println(JSON.toJSONString(pluginHandleCreateResponse));
        long handleId = pluginHandleCreateResponse.getId();
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
        room = JSON.parseObject(JSON.parseObject(JSON.toJSONString(response1)).getJSONObject("plugindata").getString("data")).getLong("room");
        System.out.println("roomid"+room);
        joinRoom1(room);
        joinRoom2(room);
        destroyRoom(room);
        while (true){}
    }

    public void joinRoom1(long room) throws  InterruptedException, ExecutionException {
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient("123");
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        long sessionId = response.getId();
        System.out.println("sessionid=="+sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setPlugin("janus.plugin.videoroom");
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
        System.out.println(JSON.toJSONString(pluginHandleCreateResponse));
        long handleId = pluginHandleCreateResponse.getId();
        VideoRoomMessageRequest videoRoomMessageRequest = new VideoRoomMessageRequest();
        videoRoomMessageRequest.setJanus("message");
        videoRoomMessageRequest.setSession_id(sessionId);
        videoRoomMessageRequest.setHandle_id(handleId);
        videoRoomMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoRoomPublisherJoinRequestBody videoRoomPublisherJoinRequestBody = new VideoRoomPublisherJoinRequestBody();
        videoRoomPublisherJoinRequestBody.setRequest("join");
        videoRoomPublisherJoinRequestBody.setRoom(room);
        System.out.println(room);
        videoRoomPublisherJoinRequestBody.setId(123L);
        videoRoomMessageRequest.setBody(videoRoomPublisherJoinRequestBody);
        janusWebSocket.sendMessage(videoRoomMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        System.out.println("join1"+JSON.toJSONString(response1));
    }

    public void joinRoom2(long room) throws  InterruptedException, ExecutionException {
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient("123");
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        long sessionId = response.getId();
        System.out.println("sessionid=="+sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setPlugin("janus.plugin.videoroom");
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
        System.out.println(JSON.toJSONString(pluginHandleCreateResponse));
        long handleId = pluginHandleCreateResponse.getId();
        VideoRoomMessageRequest videoRoomMessageRequest = new VideoRoomMessageRequest();
        videoRoomMessageRequest.setJanus("message");
        videoRoomMessageRequest.setSession_id(sessionId);
        videoRoomMessageRequest.setHandle_id(handleId);
        videoRoomMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoRoomPublisherJoinRequestBody videoRoomPublisherJoinRequestBody = new VideoRoomPublisherJoinRequestBody();
        videoRoomPublisherJoinRequestBody.setRequest("join");
        videoRoomPublisherJoinRequestBody.setRoom(room);
        videoRoomPublisherJoinRequestBody.setId(456L);
        videoRoomMessageRequest.setBody(videoRoomPublisherJoinRequestBody);
        janusWebSocket.sendMessage(videoRoomMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        System.out.println("join1"+JSON.toJSONString(response1));

    }

    public void destroyRoom(long room) throws  InterruptedException, ExecutionException {
        JanusWebSocket janusWebSocket = JanusClientUtil.createJanusClient("123");
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setJanus("create");
        createSessionRequest.setTransaction(UUID.randomUUID().toString());
        janusWebSocket.sendMessage(createSessionRequest);
        CreateSessionResponse response = (CreateSessionResponse)janusWebSocket.getResponse().get();
        long sessionId = response.getId();
        System.out.println("sessionid=="+sessionId);
        PluginHandleCreateRequest pluginHandleCreateRequest = new PluginHandleCreateRequest();
        pluginHandleCreateRequest.setJanus("attach");
        pluginHandleCreateRequest.setTransaction(UUID.randomUUID().toString());
        pluginHandleCreateRequest.setPlugin("janus.plugin.videoroom");
        pluginHandleCreateRequest.setSession_id(sessionId);
        janusWebSocket.sendMessage(pluginHandleCreateRequest);
        PluginHandleCreateResponse pluginHandleCreateResponse = (PluginHandleCreateResponse)janusWebSocket.getResponse().get();
        System.out.println(JSON.toJSONString(pluginHandleCreateResponse));
        long handleId = pluginHandleCreateResponse.getId();
        VideoRoomMessageRequest videoRoomMessageRequest = new VideoRoomMessageRequest();
        videoRoomMessageRequest.setJanus("message");
        videoRoomMessageRequest.setSession_id(sessionId);
        videoRoomMessageRequest.setHandle_id(handleId);
        videoRoomMessageRequest.setTransaction(UUID.randomUUID().toString());
        VideoRoomDestroyRequestBody videoRoomDestroyRequestBody = new VideoRoomDestroyRequestBody();
        videoRoomDestroyRequestBody.setRequest("destroy");
        videoRoomDestroyRequestBody.setRoom(room);
        videoRoomMessageRequest.setBody(videoRoomDestroyRequestBody);
        janusWebSocket.sendMessage(videoRoomMessageRequest);
        Response response1 = janusWebSocket.getResponse().get();
        System.out.println("destroy"+JSON.toJSONString(response1));
    }
    @Test
    public void testHash(){
        RTCJsep rtcJsep = new RTCJsep();
        rtcJsep.setSdp("asdf");
        RTCJsep rtcJsep1 = new RTCJsep();
        rtcJsep1.setSdp("iopj");
        ConcurrentHashMap<RTCJsep,String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put(rtcJsep,"123");
        concurrentHashMap.put(rtcJsep1,"456");
        Enumeration<RTCJsep> requestEnumeration = concurrentHashMap.keys();
        while (requestEnumeration.hasMoreElements()){
            System.out.println(requestEnumeration.nextElement().hashCode());
        }
        System.out.println(concurrentHashMap.size());
    }

}
