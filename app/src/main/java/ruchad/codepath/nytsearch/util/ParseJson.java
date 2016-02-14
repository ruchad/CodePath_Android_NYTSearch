package ruchad.codepath.nytsearch.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ruchad.codepath.nytsearch.models.Article;
import ruchad.codepath.nytsearch.models.ArticleNoThumbnail;

public class ParseJson {

    public static ArrayList<Object> fromJsonArray(JSONArray array){
        ArrayList<Object> results = new ArrayList<>();
        for(int i=0; i<array.length(); i++){
            try{
                JSONObject jsonObject = array.getJSONObject(i);
                String webUrl = jsonObject.getString("web_url");
                String headline = jsonObject.getJSONObject("headline").getString("main");

                JSONArray multimedia = jsonObject.getJSONArray("multimedia");
                if(multimedia.length()>0){
                    JSONObject multimediaJson = multimedia.getJSONObject(0);
                    String thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
                    int thumbNailWidth = multimediaJson.getInt("width");
                    int thumbNailHeight = multimediaJson.getInt("height");
                    Article article = new Article();
                    article.setHeadline(headline);
                    article.setWebUrl(webUrl);
                    article.setThumbNail(thumbNail);
                    article.setThumbNailWidth(thumbNailWidth);
                    article.setThumbNailHeight(thumbNailHeight);
                    results.add(article);
                }else {
                    ArticleNoThumbnail article = new ArticleNoThumbnail();
                    article.setHeadline(headline);
                    article.setWebUrl(webUrl);
                    results.add(article);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
