package com.forward.januswebrtc.domain.plugin.videoroom;

import com.forward.januswebrtc.domain.plugin.RequestBody;
import lombok.Data;

@Data
public class VideoRoomSubscriberJoinRequestBody extends RequestBody {

    private final String ptype = "subscriber";

    private Long room;

    private Long feed;

    private Long private_id;

    private Boolean close_pc = true;

    private Boolean audio = false;

    private Boolean video = true;

    private Boolean data = true;

    private Boolean offer_audio = false;

    private Boolean offer_video = true;

    private Boolean offer_data = true;

    private Long substream;

    private Long temporal;

    private Long spatial_layer;

    private Long temporal_layer;
}
