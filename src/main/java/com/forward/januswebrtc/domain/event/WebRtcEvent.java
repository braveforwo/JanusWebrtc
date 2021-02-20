package com.forward.januswebrtc.domain.event;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.domain.Event;
import com.forward.januswebrtc.domain.Response;
import lombok.Data;

import java.util.UUID;

@Data
public class WebRtcEvent implements Event {

    protected String janus;

    protected Long session_id;

    protected Long sender;

    protected String transaction;

    @Override
    public Response turnToObject(String message) {
        return JSON.parseObject(message,this.getClass());
    }

    @Override
    public Boolean isSuccess() {
        return false;
    }
    @Override
    public String uid() {
        return transaction!=null?transaction: UUID.randomUUID().toString();
    }
}
