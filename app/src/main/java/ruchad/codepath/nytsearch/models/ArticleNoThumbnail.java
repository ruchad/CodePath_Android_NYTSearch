package ruchad.codepath.nytsearch.models;

/**
 * Extra model class (ONLY) for using Heterogeneous layouts within recycler view
 */
public class ArticleNoThumbnail {

    private String webUrl;
    private String headline;

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
}
