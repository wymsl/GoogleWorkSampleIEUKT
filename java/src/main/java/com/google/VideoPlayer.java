package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private HashMap<String, VideoPlaylist> playlists;

  private String videoPlaying;
  private boolean isPaused;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    videoPlaying = null;
    isPaused = false;
    playlists = new HashMap();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  private String getVideoString(Video video) {
    String tags = String.join(" ", video.getTags());
    String desc = video.getTitle() + " (" + video.getVideoId() + ") [" + tags + "]";
    String flag = video.getIsFlagged()
            ? " - FLAGGED (reason: " + video.getFlagReason() + ")"
            : "";
    return desc + flag;
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videos = videoLibrary.getVideos();

    videos.sort(Comparator.comparing(Video::getTitle));

    for(Video video : videos){
      System.out.println("\t" + getVideoString(video));
    }
  }

  public void playVideo(String videoId) {

    Video new_video = videoLibrary.getVideo(videoId);
    Video old_video = videoLibrary.getVideo(videoPlaying);

    if(new_video == null){
      System.out.println("Cannot play video: Video does not exist");
      return;
    }

    if(new_video.getIsFlagged()){
      System.out.println("Cannot play video: Video is currently flagged " +
              "(reason: " + new_video.getFlagReason() + ")");
      return;
    }

    if(videoPlaying != null){
      System.out.println("Stopping video: " + old_video.getTitle());
    }
    System.out.println("Playing video: " + new_video.getTitle());
    videoPlaying = videoId;
    isPaused = false;
  }

  public void stopVideo() {
    if(videoPlaying == null){
      System.out.println("Cannot stop video: No video is currently playing");
    } else {
      Video video = videoLibrary.getVideo(videoPlaying);
      System.out.println("Stopping video: " + video.getTitle());
      videoPlaying = null;
    }
  }

  public void playRandomVideo() {
    List<Video> available_videos = new ArrayList<>();

    for(Video video : videoLibrary.getVideos()){
      if(!video.getIsFlagged()){
        available_videos.add(video);
      }
    }

    if(available_videos.size() == 0){
      System.out.println("No videos available");
      return;
    }

    Video random_video = available_videos.get(
            new Random().nextInt(available_videos.size()));
    playVideo(random_video.getVideoId());
  }

  public void pauseVideo() {
    if(videoPlaying == null){
      System.out.println("Cannot pause video: No video is currently playing");
      return;
    }

    Video video = videoLibrary.getVideo(videoPlaying);

    if(isPaused){
      System.out.println("Video already paused: " + video.getTitle());
    } else {
      System.out.println("Pausing video: " + video.getTitle());
      isPaused = true;
    }
  }

  public void continueVideo() {
    if(videoPlaying == null){
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }

    Video video = videoLibrary.getVideo(videoPlaying);

    if(isPaused){
      System.out.println("Continuing video: " + video.getTitle());
      isPaused = false;
    } else {
      System.out.println("Cannot continue video: Video is not paused");
    }
  }

  public void showPlaying() {
    if(videoPlaying == null){
      System.out.println("No video is currently playing");
      return;
    }

    Video video = videoLibrary.getVideo(videoPlaying);

    System.out.printf("Currently playing: " + getVideoString(video));
    if(isPaused){
      System.out.println(" - PAUSED");
    }
    System.out.println("\n");
  }

  private VideoPlaylist getPlaylist(String playlistName){
    VideoPlaylist playlist;

    if(playlists.containsKey(playlistName.toLowerCase())){
      return playlists.get(playlistName.toLowerCase());
    } else {
      return null;
    }
  }

  public void createPlaylist(String playlistName) {
    if(playlists.containsKey(playlistName.toLowerCase())){
      System.out.println("Cannot create playlist: " +
              "A playlist with the same name already exists");
      return;
    }

    VideoPlaylist playlist = new VideoPlaylist(playlistName);
    playlists.put(playlistName.toLowerCase(), playlist);
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = getPlaylist(playlistName);

    if(playlist == null){
      System.out.println("Cannot add video to " + playlistName +
              ": Playlist does not exist");
      return;
    }

    Video video = videoLibrary.getVideo(videoId);

    if(video == null){
      System.out.println("Cannot add video to " + playlistName +
              ": Video does not exist");
      return;
    }

    if(video.getIsFlagged()){
      System.out.println("Cannot add video to " + playlistName +
              ": Video is currently flagged " +
              "(reason: " + video.getFlagReason() + ")");
      return;
    }

    if(playlist.addVideo(video)){
      System.out.println("Added video to " + playlistName +
              ": " + video.getTitle());
    } else {
      System.out.println("Cannot add video to " + playlistName +
              ": Video already added");
    }
  }

  public void showAllPlaylists() {
    if(playlists.size() == 0){
      System.out.println("No playlists exist yet");
      return;
    }

    List<VideoPlaylist> video_playlists = new ArrayList<>(playlists.values());
    video_playlists.sort(Comparator.comparing(VideoPlaylist::getTitle));

    System.out.println("Showing all playlists:");
    for(VideoPlaylist playlist : video_playlists){
      System.out.println("\t" + playlist.getTitle());
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);

    if(playlist == null){
      System.out.println("Cannot show playlist " + playlistName +
              ": Playlist does not exist");
      return;
    }

    System.out.println("Showing playlist: " + playlistName);
    List<Video> videos = playlist.getVideos();
    if(videos.size() == 0){
      System.out.println("\tNo videos here yet");
      return;
    }

    for(Video video : videos){
      System.out.println("\t" + getVideoString(video));
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = getPlaylist(playlistName);

    if(playlist == null){
      System.out.println("Cannot remove video from " + playlistName +
              ": Playlist does not exist");
      return;
    }

    Video video = videoLibrary.getVideo(videoId);

    if(video == null){
      System.out.println("Cannot remove video from " + playlistName +
              ": Video does not exist");
      return;
    }

    if(playlist.removeVideo(video)){
      System.out.println("Removed video from " + playlistName +
              ": " + video.getTitle());
    } else {
      System.out.println("Cannot remove video from " + playlistName +
              ": Video is not in playlist");
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);

    if(playlist == null){
      System.out.println("Cannot clear playlist " + playlistName +
              ": Playlist does not exist");
      return;
    }

    playlist.clearPlaylist();
    System.out.println("Successfully removed all videos from " + playlistName);
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);

    if(playlist == null){
      System.out.println("Cannot delete playlist " + playlistName +
              ": Playlist does not exist");
      return;
    }

    playlists.remove(playlist);
    System.out.println("Deleted playlist: " + playlistName);
  }

  private void searchVideosHelper(String searchTerm, List<Video> relevant_videos){
    if(relevant_videos.size() == 0){
      System.out.println("No search results for " + searchTerm);
      return;
    }

    relevant_videos.sort(Comparator.comparing(Video::getTitle));

    System.out.println("Here are the results for " + searchTerm + ":");
    for(int i = 1; i <= relevant_videos.size(); i++){
      System.out.println("\t" + i + ") " + getVideoString(relevant_videos.get(i - 1)));
    }
    System.out.println("Would you like to play any of the above? If yes, " +
            "specify the number of the video.\nIf your answer is not a valid " +
            "number, we will assume it's a no.");

    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();

    int selection;
    try{
      selection = Integer.parseInt(input);
    } catch (NumberFormatException e){
      return;
    }

    if(selection > relevant_videos.size()){
      return;
    }
    playVideo(relevant_videos.get(selection - 1).getVideoId());
  }

  public void searchVideos(String searchTerm) {
    List<Video> videos_with_term = new ArrayList<>();

    for(Video video : videoLibrary.getVideos()){
      if(video.getIsFlagged()){
        continue;
      }
      if(video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
          videos_with_term.add(video);
      }
    }

    searchVideosHelper(searchTerm, videos_with_term);
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> videos_with_tag = new ArrayList<>();

    for (Video video : videoLibrary.getVideos()) {
      if(video.getIsFlagged()){
        continue;
      }

      for (String tag : video.getTags()) {
        if (tag.toLowerCase().equals(videoTag.toLowerCase())) {
          videos_with_tag.add(video);
          break;
        }
      }
    }

    searchVideosHelper(videoTag, videos_with_tag);
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    Video video = videoLibrary.getVideo(videoId);
    if(video == null){
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }

    if(video.getIsFlagged()){
      System.out.println("Cannot flag video: Video is already flagged");
      return;
    }

    video.setIsFlagged(true);
    video.setFlagReason(reason);

    if(video.getVideoId().equals(videoPlaying)){
      stopVideo();
      isPaused = false;
    }

    System.out.println("Successfully flagged video: " + video.getTitle() +
            " (reason: " + reason + ")");
  }

  public void allowVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if(video == null){
      System.out.println("Cannot remove flag from video: Video does not exist");
      return;
    }

    if(!video.getIsFlagged()){
      System.out.println("Cannot remove flag from video: Video is not flagged");
      return;
    }

    video.setIsFlagged(false);
    video.setFlagReason(null);

    System.out.println("Successfully removed flag from video: " + video.getTitle());
  }
}