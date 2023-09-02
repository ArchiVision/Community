package com.archivision.community.event;

import java.io.Serializable;

public record LikeEvent(Long liker, Long liked) implements Serializable {

}
