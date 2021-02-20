package com.forward.januswebrtc.domain;

public interface Request {
    default String turnToString(){throw new RuntimeException("没有实现方法");}
    default String uid(){throw new RuntimeException("没有实现方法");}
    default Boolean hasOnceResponse(){return false;}
}
