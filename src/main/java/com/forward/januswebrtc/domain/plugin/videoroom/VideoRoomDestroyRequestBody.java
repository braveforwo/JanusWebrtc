package com.forward.januswebrtc.domain.plugin.videoroom;

import com.forward.januswebrtc.domain.plugin.RequestBody;
import lombok.Data;

@Data
public class VideoRoomDestroyRequestBody extends RequestBody {

    private String secret;
    private long room;
    private Boolean permanent;
}
