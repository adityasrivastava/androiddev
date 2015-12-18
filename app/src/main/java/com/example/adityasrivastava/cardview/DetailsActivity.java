package com.example.adityasrivastava.cardview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adityasrivastava.cardview.modal.PostDetails;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends Activity {

    private TextView mTextView;
    private TextView mTextContent;
    private ImageView mImageView;
    private PostDetails mPostDetails;
    private FloatingActionButton shareButton;
    private FloatingActionButton browserButton;

    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);

        setContentView(R.layout.details_main);

        hideStatusBar();


        mPostDetails = (PostDetails) getIntent().getSerializableExtra("post");

        shareButton = (FloatingActionButton) findViewById(R.id.share_button);
        browserButton = (FloatingActionButton) findViewById(R.id.browser_button);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, mPostDetails.getUrl());
                share.setType("text/plain");
                startActivity(share);
            }
        });

        browserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browser = new Intent();
                browser.setAction(Intent.ACTION_VIEW);
                browser.addCategory(Intent.CATEGORY_BROWSABLE);
                browser.setData(Uri.parse(mPostDetails.getUrl()));
                startActivity(browser);

            }
        });

        mTextView = (TextView) findViewById(R.id.post_title);
        mTextContent = (TextView) findViewById(R.id.post_details);
        mImageView = (ImageView) findViewById(R.id.content_image);

        mTextView.setText(mPostDetails.getTitle());
        mTextContent.setText(Html.fromHtml(mPostDetails.getContent()));
        Picasso.with(this).load(mPostDetails.getThumbnail()).into(mImageView);

    }

    private void hideStatusBar(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

}
