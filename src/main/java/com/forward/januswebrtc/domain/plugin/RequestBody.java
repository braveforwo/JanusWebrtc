package com.forward.januswebrtc.domain.plugin;

import com.forward.januswebrtc.domain.Request;
import lombok.Data;

@Data
public class RequestBody implements Request {
    private String request;
}
