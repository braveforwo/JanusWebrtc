package com.forward.januswebrtc.domain.common;

import com.forward.januswebrtc.domain.Event;
import com.forward.januswebrtc.domain.Response;
import lombok.Data;

import java.util.UUID;

/**
 * 和插件交互的janus响应体
 */
@Data
public class JanusEvent implements Event {

    protected String janus;

    protected String transaction;

    protected Long session_id;

    protected Long sender;

    private JanusPluginData plugindata;

    private String jsep;
    @Data
    public static class JanusPluginData
    {

        protected String plugin;

        protected Object data;
    }

    @Override
    public Boolean isSuccess() {
        return false;
    }

    @Override
    public String uid() {
        return transaction==null?UUID.randomUUID().toString():getTransaction();
    }
}
