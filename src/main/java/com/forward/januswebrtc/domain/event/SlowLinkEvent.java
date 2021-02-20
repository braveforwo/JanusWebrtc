package com.forward.januswebrtc.domain.event;

import com.forward.januswebrtc.annotation.JanusCommand;
import lombok.Data;

@Data
@JanusCommand("slowlink")
public class SlowLinkEvent extends WebRtcEvent{
    private Boolean uplink;

    private String media;

    private Integer lost;
}
