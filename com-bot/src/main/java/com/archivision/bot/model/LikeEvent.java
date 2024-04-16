package com.archivision.bot.model;

import java.io.Serializable;

public record LikeEvent(Long liker, Long liked) implements Serializable {

}
