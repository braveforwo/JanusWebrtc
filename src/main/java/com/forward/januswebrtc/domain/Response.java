package com.forward.januswebrtc.domain;

public interface Response {
    default Boolean isSuccess(){throw new RuntimeException("没有实现方法");}
    default Response turnToObject(String message){throw new RuntimeException("没有实现方法");}
    default String uid(){throw new RuntimeException("没有实现方法");}
}
