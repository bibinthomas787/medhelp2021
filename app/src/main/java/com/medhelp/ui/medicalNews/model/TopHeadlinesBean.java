package com.medhelp.ui.medicalNews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopHeadlinesBean {
    public class Article {

        @SerializedName("source")
        @Expose
        public Source source;
        @SerializedName("author")
        @Expose
        public String author;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("urlToImage")
        @Expose
        public String urlToImage;
        @SerializedName("publishedAt")
        @Expose
        public String publishedAt;
        @SerializedName("content")
        @Expose
        public String content;

    }


        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("totalResults")
        @Expose
        public Integer totalResults;
        @SerializedName("articles")
        @Expose
        public List<Article> articles ;




    public class Source {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;

    }

}
