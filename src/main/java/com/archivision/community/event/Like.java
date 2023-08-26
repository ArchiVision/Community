package com.archivision.community.event;

import java.io.Serializable;

public record Like(Long liker, Long liked) implements Serializable {

}
