package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean isFlagged;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.isFlagged = false;
    this.flagReason = null;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  /** Returns whether the video is flagged or not at a boolean. */
  boolean getIsFlagged() {
    return isFlagged;
  }

  /** Returns the reason for the video being flagging. */
  String getFlagReason() {
    return flagReason;
  }

  /** Sets whether the video is flagged or not. */
  void setIsFlagged(boolean isFlagged){ this.isFlagged = isFlagged; }

  /** Sets the reason for the video being flagged. */
  void setFlagReason(String flagReason) { this.flagReason = flagReason; }
}
