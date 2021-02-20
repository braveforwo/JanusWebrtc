package com.forward.januswebrtc.domain.plugin.videocall;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.annotation.JanusCommand;
import com.forward.januswebrtc.annotation.PluginCommand;
import com.forward.januswebrtc.domain.Request;
import lombok.Data;

@Data
@PluginCommand("videocall.message")
public class VideoCallMessageRequest implements Request {
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
        return getTransaction();
    }
}
