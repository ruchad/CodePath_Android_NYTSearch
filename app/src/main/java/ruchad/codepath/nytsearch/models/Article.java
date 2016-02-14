package ruchad.codepath.nytsearch.models;

public class Article {

    private String webUrl;
    private String headline;
    private String thumbNail;
    private int thumbNailWidth;
    private int thumbNailHeight;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public int getThumbNailWidth() {
        return thumbNailWidth;
    }

    public void setThumbNailWidth(int thumbNailWidth) {
        this.thumbNailWidth = thumbNailWidth;
    }

    public int getThumbNailHeight() {
        return thumbNailHeight;
    }

    public void setThumbNailHeight(int thumbNailHeight) {
        this.thumbNailHeight = thumbNailHeight;
    }
}
