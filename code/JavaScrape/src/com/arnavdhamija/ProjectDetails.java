package com.arnavdhamija;

public class ProjectDetails {
    String id;
    int imageCount;
    String aboutText;
    String risksText;
    boolean hasVideo;

    public ProjectDetails(String id, int imageCount, String aboutText, String risksText, boolean hasVideo) {
        this.id = id;
        this.imageCount = imageCount;
        this.aboutText = aboutText;
        this.risksText = risksText;
        this.hasVideo = hasVideo;
    }
}