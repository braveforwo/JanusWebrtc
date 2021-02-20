package com.forward.januswebrtc.domain.plugin;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.annotation.JanusCommand;
import com.forward.januswebrtc.domain.common.JanusResponse;

@JanusCommand("janus.attach")
public class PluginHandleCreateResponse extends JanusResponse {
    public long getId(){
        return JSON.parseObject(getData()).getLong("id");
    }
}
