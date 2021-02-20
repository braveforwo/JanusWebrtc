package com.forward.januswebrtc.domain.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Janus返回的error json对象
 */
@Getter
@Setter
public class JanusError {
    private transient String janus;
    private transient String transaction;
    private transient Long session_id;
    private Long code;
    private String reason;
}
