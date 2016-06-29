package com.bignerdranch.android.initialtwittersyncadapter.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TweetSearchResponse {
    @SerializedName("statuses")
    private List<Tweet> mTweetList;

    public List<Tweet> getTweetList() {
        return mTweetList;
    }
}
