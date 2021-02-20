package com.forward.januswebrtc.domain.event;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.annotation.PluginCommand;
import com.forward.januswebrtc.domain.Response;
import com.forward.januswebrtc.domain.common.JanusEvent;
@PluginCommand("videoroom.message")
public class VideoRoomEvent extends JanusEvent {
    @Override
    public Boolean isSuccess() {
        return "success".equalsIgnoreCase(janus);
    }

    @Override
    public Response turnToObject(String message) {
        return JSON.parseObject(message,this.getClass());
    }
}
