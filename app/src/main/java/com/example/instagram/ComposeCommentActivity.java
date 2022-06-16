package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;


public class ComposeCommentActivity extends AppCompatActivity {

    public String TAG = "compose comment activity";
    Post post;
    ImageButton btSave;
    EditText etBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_comment);

        post = getIntent().getParcelableExtra("post");

        Toast.makeText(this, post.getDescription(), Toast.LENGTH_SHORT).show();

        btSave = findViewById(R.id.btSave);
        etBody = findViewById(R.id.etBody);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etBody.getText().toString();

                Comment comment = new Comment();
                comment.setBody(body);
                comment.setPost(post);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                            return;
                        }
                        finish();
                    }
                });
            }
        });
    }
}