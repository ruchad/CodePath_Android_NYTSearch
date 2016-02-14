package ruchad.codepath.nytsearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import ruchad.codepath.nytsearch.R;
import ruchad.codepath.nytsearch.activities.ArticleActivity;
import ruchad.codepath.nytsearch.models.Article;
import ruchad.codepath.nytsearch.models.ArticleNoThumbnail;

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> Articles;
    private final int articleWithImage=0, articleNoThumbnail=1;

    public ArticlesAdapter(List<Object> articles){
        Articles = articles;
    }

    //Inflate the layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case(articleWithImage):
                View articleView = inflater.inflate(R.layout.item_article, parent, false) ;
                viewHolder = new ViewHolder(context, articleView);
                break;
            case(articleNoThumbnail):
                View articleNoThumbnailView = inflater.inflate(R.layout.item_article_no_thumbnail, parent, false);
                viewHolder = new ViewHolderNoThumbnail(context, articleNoThumbnailView);
                break;
            default:
                //ToDo: Implement default behavior
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    //Populate the data from viewHolder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case articleWithImage:
                ViewHolder v1 = (ViewHolder) holder;
                Article article = (Article) Articles.get(position);
                TextView tvHeadline = v1.tvHeadline;
                tvHeadline.setText(article.getHeadline());
                ImageView ivThumbnail = v1.ivThumbnail;
                String imageUri = article.getThumbNail();
                if (!imageUri.isEmpty()) {
                    //ToDo: Read complete documentation for Glide
                    Glide.with(v1.ivThumbnail.getContext()).load(imageUri).override(article.getThumbNailWidth(), article.getThumbNailHeight()).into(ivThumbnail);
                }
                break;
            case articleNoThumbnail:
                ViewHolderNoThumbnail v2 = (ViewHolderNoThumbnail) holder;
                ArticleNoThumbnail article2 = (ArticleNoThumbnail) Articles.get(position);
                TextView tvHeadlineOnly = v2.tvHeadlineOnly;
                tvHeadlineOnly.setText(article2.getHeadline());
                break;
            default:
                //ToDo: Implement default behavior
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(Articles.get(position) instanceof Article)
            return articleWithImage;
        else if(Articles.get(position) instanceof ArticleNoThumbnail)
            return articleNoThumbnail;
        return -1;
    }

    @Override
    public int getItemCount() {
        return Articles.size();
    }

    //View Holder for articles with thumbnails
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivThumbnail;
        public TextView tvHeadline;
        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ivThumbnail = ButterKnife.findById(itemView, R.id.ivThumbnail);
            tvHeadline = ButterKnife.findById(itemView, R.id.tvHeadline);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Article article = (Article) Articles.get(position);
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("webURL", article.getWebUrl());
            context.startActivity(intent);
        }
    }

    //View holder for articles without thumbnails
    public class ViewHolderNoThumbnail extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvHeadlineOnly;
        private Context context;

        public ViewHolderNoThumbnail(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvHeadlineOnly = ButterKnife.findById(itemView, R.id.tvHeadlineOnly);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            ArticleNoThumbnail article = (ArticleNoThumbnail) Articles.get(position);
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("webURL", article.getWebUrl());
            context.startActivity(intent);
        }
    }
}
