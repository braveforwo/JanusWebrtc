package com.forward.januswebrtc.common;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.domain.Request;
import com.forward.januswebrtc.domain.Response;
import com.forward.januswebrtc.util.MessageManager;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class JanusWebSocket extends WebSocketClient{
    private static final String JANUS_PROTOCOL = "janus-protocol";
    public  CompletableFuture<Response> response = null;
    private KeepAliveThread keepAliveThread;
    private long session_id;
    private long handle_id;

    public void setKeepAliveThread(KeepAliveThread keepAliveThread) {
        this.keepAliveThread = keepAliveThread;
    }

    public long getSession_id() {
        return session_id;
    }

    public void setSession_id(long session_id) {
        this.session_id = session_id;
    }

    public long getHandle_id() {
        return handle_id;
    }

    public void setHandle_id(long handle_id) {
        this.handle_id = handle_id;
    }

    public CompletableFuture<Response> getResponse() {
        return response;
    }

    public void setResponse(CompletableFuture<Response> response) {
        this.response = response;
    }

    public JanusWebSocket(URI serverUri) {
        super(serverUri, new Draft_6455(Collections.<IExtension>emptyList(),
                Collections.<IProtocol>singletonList(new Protocol(JANUS_PROTOCOL))));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
//        System.out.println(s);
        MessageManager.THE_ONLY_ONE_MANAGER.commitResponseMessage(this,s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
              keepAliveThread.setRunning(false);
    }

    @Override
    public void onError(Exception e) {

    }

    public void sendMessage(Request request){
        this.send(request.turnToString());
        setResponse(new CompletableFuture<>());
        MessageManager.THE_ONLY_ONE_MANAGER.pendRequestMessage(request,response);
    }
}
