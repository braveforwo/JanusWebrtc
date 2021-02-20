package com.forward.januswebrtc.domain.common;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.annotation.JanusCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JanusCommand("janus.create")
@ToString
public class CreateSessionResponse extends JanusResponse{
    public long getId(){
        return JSON.parseObject(getData()).getLong("id");
    }
}
