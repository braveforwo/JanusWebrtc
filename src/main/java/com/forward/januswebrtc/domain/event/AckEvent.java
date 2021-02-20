package com.forward.januswebrtc.domain.event;

import com.forward.januswebrtc.annotation.JanusCommand;
import com.forward.januswebrtc.domain.Event;
import lombok.Data;

import java.util.UUID;

@Data
@JanusCommand("ack")
public class AckEvent extends WebRtcEvent {

}
