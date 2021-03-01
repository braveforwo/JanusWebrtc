package com.forward.januswebrtc.util;

import com.forward.januswebrtc.common.JanusWebSocket;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

public class JanusClientUtil {
    private static final ConcurrentHashMap<String, JanusWebSocket> wsclients = new ConcurrentHashMap<>();
    private static final String serverURI = "ws://xxx.xxx.xxx.xxx:8188";

    /**
     * 获取januswebsocket
     * @param namespace 唯一标识
     * @return JanusWebSocket
     */
    public static JanusWebSocket getJanusClient(String namespace){
        return wsclients.getOrDefault(namespace, null);
    }

    /**
     * 创建一个januswebsocket
     * @param namespace 唯一标识
     * @return JanusWebSocket
     * @throws InterruptedException h
     */
    public static JanusWebSocket createJanusClient(String namespace) throws InterruptedException {
        JanusWebSocket webSocketClient = new JanusWebSocket(URI.create(serverURI));
        webSocketClient.connectBlocking();
        if(!webSocketClient.isOpen()){
            throw new RuntimeException("连接失败。。。");
        }
        wsclients.put(namespace,webSocketClient);
        //初始化对应的阻塞队列
        MessageManager.THE_ONLY_ONE_MANAGER.initNeedToPush(webSocketClient);
        return webSocketClient;
    }
}
