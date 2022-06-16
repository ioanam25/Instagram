package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    Post post;

    TextView tvDetailUsername;
    ImageView ivDetailImage;
    TextView tvDetailDescription;
    TextView tvDetailTimestamp;
    ConstraintLayout constraintLayout;
    FloatingActionButton fabComment;
    TextView tvLikeCount;
    ImageButton ibIsLiked;

    RecyclerView rvComments;
    CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        tvDetailUsername = findViewById(R.id.tvDetailUsername);
        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailTimestamp = findViewById(R.id.tvDetailTimestamp);
        constraintLayout = findViewById(R.id.cl);
        fabComment = findViewById(R.id.fabComment);
        rvComments = findViewById(R.id.rvComments);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        ibIsLiked = findViewById(R.id.ibIsLiked);

        adapter = new CommentsAdapter();
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(adapter);
        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("post");

        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        tvDetailTimestamp.setText(timeAgo);
        tvDetailUsername.setText(post.getUser().getUsername());
        tvDetailTimestamp.setText(timeAgo);
        tvDetailDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(PostDetailsActivity.this).load(image.getUrl()).into(ivDetailImage);
        }
        if (post.getLikedBy().contains(ParseUser.getCurrentUser())) {
            ibIsLiked.setBackgroundResource(R.drawable.ufi_heart_active);
        }
        else {
            ibIsLiked.setBackgroundResource(R.drawable.ufi_heart);
        }

        fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailsActivity.this, ComposeCommentActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });

        ibIsLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ParseUser> likedBy = post.getLikedBy();
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (likedBy.contains(currentUser)) {
                    likedBy.remove(currentUser);
                    ibIsLiked.setBackgroundResource(R.drawable.ufi_heart);
                }
                else {
                    ibIsLiked.setBackgroundResource(R.drawable.ufi_heart_active);
                    likedBy.add(currentUser);
                }

                tvLikeCount.setText( String.valueOf( post.getLikedBy().size()) );
                post.setLikedBy(likedBy);
                post.saveInBackground(); // uploads new value back to parse
            }
        });


        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo(Comment.KEY_POST, post);
        query.orderByDescending("createdAt");
        query.include(Comment.KEY_AUTHOR);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Log.e("failed to comment", e.getMessage());
                    return;
                }
                adapter.mComments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });

    }
}