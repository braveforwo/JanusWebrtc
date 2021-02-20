package com.forward.januswebrtc.domain.plugin.videoroom;

import com.forward.januswebrtc.domain.plugin.RequestBody;
import lombok.Data;

@Data
public class VideoRoomPublisherPublishRequestBody extends RequestBody {
    private Boolean audio = false;
    private Boolean video = true;
    private Boolean data = true;
    private String audiocodec;
    private String videocodec;

    private Long bitrate;

    private Boolean record;

    private String filename;

    private String display;

}
