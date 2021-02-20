package com.forward.januswebrtc.domain.plugin.videoroom;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.annotation.PluginCommand;
import com.forward.januswebrtc.domain.Request;
import lombok.Data;

import java.util.UUID;

@Data
@PluginCommand("videoroom.message")
public class VideoRoomMessageRequest implements Request {
    protected String janus;


    protected String transaction;

    protected Long session_id;

    protected Long handle_id;

    protected Request body;

    protected Object jsep;

    @Override
    public String turnToString() {
        return JSON.toJSONString(this);
    }

    @Override
    public String uid() {
        return transaction!=null?transaction:UUID.randomUUID().toString();
    }
}
