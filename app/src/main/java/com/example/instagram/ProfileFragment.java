package com.example.instagram;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends FeedFragment {

    public static final String TAG = "ProfileFragment";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSwipeContainer(view);

        rvPosts = view.findViewById(R.id.rvPosts);
        int columns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), columns, GridLayoutManager.VERTICAL, false);
        rvPosts.setLayoutManager(gridLayoutManager);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
        rvPosts.setAdapter(adapter);

        queryPosts(null);
    }

    @Override
    protected void queryPosts(String maxId) {
        Log.e(TAG, "query happens");
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        Log.e("current user", ParseUser.getCurrentUser().getUsername());
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}