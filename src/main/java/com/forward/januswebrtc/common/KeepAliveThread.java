package com.forward.januswebrtc.common;

import com.forward.januswebrtc.domain.common.JanusRequest;

import java.util.UUID;

public class KeepAliveThread extends Thread{
    private Boolean running = true;

    public void setRunning(Boolean running) {
        this.running = running;
    }

    private JanusWebSocket janusWebSocket;
    public KeepAliveThread(JanusWebSocket janusWebSocket){
        this.janusWebSocket = janusWebSocket;
    }
    @Override
    public void run() {
        while (running){
            JanusRequest janusRequest = new JanusRequest();
            janusRequest.setJanus("keepalive");
            janusRequest.setOnce(true);
            janusRequest.setSession_id(janusWebSocket.getSession_id());
            janusRequest.setTransaction(UUID.randomUUID().toString());
            janusWebSocket.sendMessage(janusRequest);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
