package com.forward.januswebrtc.domain.event;

import com.forward.januswebrtc.annotation.JanusCommand;
import lombok.Data;

@Data
@JanusCommand("media")
public class MediaEvent extends WebRtcEvent {
    private String type;

    private Boolean receiving;
}
