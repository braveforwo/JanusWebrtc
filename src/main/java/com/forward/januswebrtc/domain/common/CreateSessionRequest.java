package com.forward.januswebrtc.domain.common;

import com.forward.januswebrtc.annotation.JanusCommand;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建会话请求
 */
@Setter
@Getter
@JanusCommand("janus.create")
public class CreateSessionRequest extends JanusRequest {
}
