package com.forward.januswebrtc.domain.plugin.videoroom;

import com.forward.januswebrtc.domain.plugin.RequestBody;
import lombok.Data;

@Data
public class VideoRoomPublisherJoinRequestBody extends RequestBody {
    /**
     * type
     */
    private final String ptype = "publisher";
    /**
     * unique ID of the room to subscribe in
     */
    private Long room;
    /**
     * unique ID to register for the publisher; optional, will be chosen by the
     * plugin if missing
     */
    private Long id;
    /**
     * display name for the publisher; optional
     */
    private String display;
    /**
     * invitation token, in case the room has an ACL; optional
     */
    private String token;
}
