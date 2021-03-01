package com.forward.januswebrtc.domain.plugin.videoroom;

import com.forward.januswebrtc.domain.plugin.RequestBody;
import lombok.Data;

@Data
public class VideoRoomListParticipantsRequestBody extends RequestBody {
    private long room;
}
