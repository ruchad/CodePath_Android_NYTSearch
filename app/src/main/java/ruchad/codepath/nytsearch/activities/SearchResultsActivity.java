package ruchad.codepath.nytsearch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import ruchad.codepath.nytsearch.R;
import ruchad.codepath.nytsearch.adapters.ArticlesAdapter;
import ruchad.codepath.nytsearch.models.Article;
import ruchad.codepath.nytsearch.models.ArticleNoThumbnail;
import ruchad.codepath.nytsearch.models.ArticleResponse;
import ruchad.codepath.nytsearch.models.Doc;
import ruchad.codepath.nytsearch.models.Multimedium;
import ruchad.codepath.nytsearch.util.EndlessRecyclerViewScrollListener;
import ruchad.codepath.nytsearch.util.GridDividerDecoration;

public class SearchResultsActivity extends AppCompatActivity {

    ArrayList<Object> articles;
    @Bind(R.id.tvSearchQuery) TextView tvSearchQuery;
    @Bind(R.id.rvArticles) RecyclerView rvArticles;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    ArticlesAdapter adapter;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (!isNetworkAvailable() || !isOnline()) {
            setContentView(R.layout.activity_search_results_error);
            return;
        }

        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(SearchResultsActivity.this);

        //Setup recycler view
        articles = new ArrayList<>();
        adapter = new ArticlesAdapter(articles);
        rvArticles.setAdapter(adapter);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(staggeredGridLayoutManager);
        rvArticles.setOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
        rvArticles.addItemDecoration(new GridDividerDecoration(this));
        rvArticles.setItemAnimator(new FadeInAnimator());

        //Fetch Articles
        Intent intent = getIntent();
        query = intent.getStringExtra(getApplicationContext().getResources().getString(R.string.search_string));
        tvSearchQuery.setText("Results for : " + query);

        articles.clear();
        fetchArticles(query, 0);
    }

    public void fetchArticles(String query, final int offset) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        final RequestParams requestParams = new RequestParams();
        requestParams.put("api-key", "355a18a16809bb01bcb9a3512d658720:14:74335415");
        requestParams.put("page", offset);
        requestParams.put("q", query);

        // Search filter preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String begin_date = sharedPref.getString(getString(R.string.key_beginDate), "");
        if (!begin_date.isEmpty()){
            begin_date = begin_date.replaceAll("/", "");
            requestParams.put("begin_date", begin_date);
        }
        String sort = sharedPref.getString(getString(R.string.key_sortOrder), "");
        if (!sort.isEmpty()) requestParams.put("sort", sort);
        String news_desk = sharedPref.getString(getString(R.string.news_desk), "");
        if(!news_desk.isEmpty()) requestParams.put("fq", news_desk);


        client.get(url, requestParams, new TextHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                int pos = articles.size();
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                ArticleResponse response = gson.fromJson(responseString, ArticleResponse.class);
                List<Doc> docs = response.getResponse().getDocs();
                for(int i=0; i<docs.size(); i++){
                    Doc doc = docs.get(i);
                    List<Multimedium> multimediums = doc.getMultimedia();
                    if(multimediums.size()==0){
                        ArticleNoThumbnail articleNoThumbnail = new ArticleNoThumbnail();
                        articleNoThumbnail.setHeadline(doc.getHeadline().getMain());
                        articleNoThumbnail.setWebUrl(doc.getWebUrl());
                        articles.add(articleNoThumbnail);
                    }else{
                        Article article = new Article();
                        article.setHeadline(doc.getHeadline().getMain());
                        article.setWebUrl(doc.getWebUrl());
                        Multimedium m = multimediums.get(0);
                      //  for(Multimedium m : multimediums) {
                        //    if(m.getSubtype().equalsIgnoreCase("thumbnail")) {
                                article.setThumbNail("http://www.nytimes.com/" + m.getUrl());
                                article.setThumbNailHeight(m.getHeight());
                                article.setThumbNailWidth(m.getWidth());
                           // }
                        //}
                        articles.add(article);
                    }
                }
                adapter.notifyItemRangeInserted(pos, articles.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("NYTSearch", responseString + " :" + statusCode);
            }
        });
    }

    public void customLoadMoreDataFromApi(int offset) {
        int curSize = adapter.getItemCount();
        fetchArticles(query, offset);
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
