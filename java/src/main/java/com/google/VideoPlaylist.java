package com.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private final String title;
    private List<Video> videos;

    VideoPlaylist(String title) {
        this.title = title;
        this.videos = new ArrayList<Video>();
    }

    /** Returns the title of the playlist. */
    public String getTitle() {
        return title;
    }

    /** Returns a collection of videos in the playlist. */
    public List<Video> getVideos() {
        return videos;
    }

    /** Adds a video to the playlist. */
    public boolean addVideo(Video video){
        if(videos.contains(video)) {
            return false;
        }
        videos.add(video);
        return true;
    }

    /** Removed a video from the playlist. */
    public boolean removeVideo(Video video){
        if(!videos.contains(video)) {
            return false;
        }
        videos.remove(video);
        return true;
    }

    /** Clears the playlist, removing all videos. */
    public void clearPlaylist(){
        videos = new ArrayList<Video>();
    }
}
