package com.searchbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import netclient.BookClient;

public class DisplayBookActivity extends AppCompatActivity {
    String coverUrl="";
    private String ISBN="";
    String title="";
    private ImageView bookCoverImg;
    private TextView bookTitle;
    private BookClient client;
    private String numOfPages="";
    private TextView pages;
    private TextView publishDateView;
    private String publishDateStr ="";
    private TextView ISBNView;

    private Book book=new Book();
    private Context context=this;
    private static final String TAG = "DisplayBookActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        title=bundle.getString("Title");
        setTitle(title);
        coverUrl=bundle.getString("Cover URL");
        ISBN=bundle.getString("ISBN");



        //System.out.println(coverUrl);
        bookCoverImg=(ImageView)findViewById(R.id.bookCoverImg);
        bookTitle=(TextView)findViewById(R.id.book_details_title);
        pages=(TextView)findViewById(R.id.book_details_pages);
        ISBNView=(TextView)findViewById(R.id.book_details_isbn);
        ISBNView.setText("ISBN:"+ISBN);
        //bookTitle.setText(title);
        publishDateView=(TextView)findViewById(R.id.book_details_publish_date);

        //fetch Book details from Open Library API
        fetchBookDetails(ISBN);




        Picasso.with(context).load(Uri.parse(coverUrl)).placeholder(R.drawable.ic_nocover).into(bookCoverImg);




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
    public void fetchBookDetails(final String isbnNumber){
        client =new BookClient();
        client.getBookDetails(isbnNumber,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseObj){
                try{
                    JSONObject jsonObject;
                    if(responseObj !=null){
                        //get the docs json array

                        jsonObject=responseObj.getJSONObject("ISBN:"+isbnNumber);
                        JSONObject details=jsonObject.getJSONObject("details");
                        System.out.println(details);
                        //publishDateStr = ;
                        //Log.i(TAG,"Form: "+ publishDateStr);

                        publishDateView.setText(details.getString("publish_date"));
                        if(!details.has("number_of_pages")){
                            pages.setText("N/A");
                        }else{
                            pages.setText(details.getString("number_of_pages"));}
                        if(!details.has("title")){
                            bookTitle.setText(title);
                        }
                        else{
                            bookTitle.setText(details.getString("title"));
                        }


                    }
                }catch(JSONException jsonex){
                    jsonex.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}
