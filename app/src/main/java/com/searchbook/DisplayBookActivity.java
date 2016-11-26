package com.searchbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayBookActivity extends AppCompatActivity {
    String coverUrl="";
    String ISBN="";
    String title="";
    private ImageView bookCoverImg;
    private TextView bookTitle;
    //private Context context=getApplicationContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        ISBN=bundle.getString("ISBN");
        title=bundle.getString("Title");
        coverUrl=bundle.getString("Cover URL");
        //System.out.println(coverUrl);
        bookCoverImg=(ImageView)findViewById(R.id.bookCoverImg);
        bookTitle=(TextView)findViewById(R.id.book_details_title);
        bookTitle.setText(title);
        setTitle(title);
        Picasso.with(this).load(Uri.parse(coverUrl)).placeholder(R.drawable.ic_nocover).into(bookCoverImg);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
