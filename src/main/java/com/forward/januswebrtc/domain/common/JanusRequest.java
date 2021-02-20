package com.forward.januswebrtc.domain.common;

import com.alibaba.fastjson.JSON;
import com.forward.januswebrtc.annotation.JanusCommand;
import com.forward.januswebrtc.domain.Request;
import lombok.Getter;
import lombok.Setter;

@JanusCommand("janus")
@Getter
@Setter
public class JanusRequest implements Request {
    protected String janus;
    protected String transaction;
    protected Long session_id;
    protected Long handle_id;
    protected String data;

    public void setOnce(Boolean once) {
        this.once = once;
    }

    protected Boolean once = false;

    @Override
    public String turnToString() {
        return JSON.toJSONString(this);
    }

    /**
     * 获取当前请求的transaction，用来判断响应对应哪个请求
     * @return transaction
     */
    @Override
    public String uid() {
        return getTransaction();
    }

    /**
     * 用来判断当前的请求是否只返回一次响应，有些janus请求会返回两次响应结果，比如video call message请求
     * 会返回ackevent 和 janusevent
     * @return 只有一次为true 默认请求为false
     */
    @Override
    public Boolean hasOnceResponse() {
        return once;
    }
}
