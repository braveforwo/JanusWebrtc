package com.forward.januswebrtc.domain.bean;

import lombok.Data;

import java.util.Objects;

@Data
public class RTCJsep {
    private String type;
    private String sdp;
}