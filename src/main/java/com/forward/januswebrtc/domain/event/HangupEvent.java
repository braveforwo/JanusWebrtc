package com.forward.januswebrtc.domain.event;

import com.forward.januswebrtc.annotation.JanusCommand;
import lombok.Data;

@Data
@JanusCommand("hangup")
public class HangupEvent extends WebRtcEvent{
    private String reason;
}
