package com.bignerdranch.android.initialtwittersyncadapter.web;

import com.bignerdranch.android.initialtwittersyncadapter.model.TweetSearchResponse;

import retrofit.http.GET;
import retrofit.http.Query;

public interface TweetInterface {
    @GET("/search/tweets.json")
    TweetSearchResponse searchTweets(@Query("q") String query);
}
