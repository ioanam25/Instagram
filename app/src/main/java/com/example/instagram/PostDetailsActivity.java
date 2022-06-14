package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {
    Post post;

    TextView tvDetailUsername;
    ImageView ivDetailImage;
    TextView tvDetailDescription;
    TextView tvDetailTimestamp;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        tvDetailUsername = findViewById(R.id.tvDetailUsername);
        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailTimestamp = findViewById(R.id.tvDetailTimestamp);
        constraintLayout = findViewById(R.id.cl);

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
    }
}