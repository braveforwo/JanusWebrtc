package com.forward.januswebrtc.domain.common;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.annotation.JanusCommand;
import com.forward.januswebrtc.domain.Response;
import lombok.Getter;
import lombok.Setter;

@JanusCommand("janus")
@Getter
@Setter
public class JanusResponse implements Response {

    protected String janus;

    protected String transaction;

    protected Long session_id;

    protected String data;

    protected JanusError error;

    protected final static String SUCCESSED = "success";

    /**
     * 用来判断响应是否为使用插件前的响应，因为使用所有janus插件前都需要先进行create会话，
     * attach插件，使用插件前的响应都是一样的可以统一进行处理
     * @return
     */
    @Override
    public Boolean isSuccess() {
        return this.SUCCESSED.equalsIgnoreCase(janus);
    }

    @Override
    public Response turnToObject(String message) {
        return JSON.parseObject(message,this.getClass());
    }

    @Override
    public String uid() {
        return getTransaction();
    }
}
