package com.searchbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;
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
    private TextView publishedPlacesView;
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
        publishedPlacesView =(TextView)findViewById(R.id.book_details_publish_place);
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

                        publishDateView.setText("Published in: "+details.getString("publish_date"));

                        if(!details.has("number_of_pages")){
                            pages.setText("Number of Pages: N/A");
                        }else{
                            pages.setText("Number of Pages: "+details.getString("number_of_pages"));}
                        if(!details.has("title")){
                            bookTitle.setText(title);
                        }
                        else{
                            bookTitle.setText(details.getString("title"));
                        }
                        if(!details.has("publish_places")){
                            publishedPlacesView.setText("Published in: N/A");
                        }else{
                            JSONArray published_places=details.getJSONArray("publish_places");
                            final String[] publishedPlacesArray=new String[published_places.length()];
                            for(int i=0;i<published_places.length();i++){
                                publishedPlacesArray[i]=published_places.getString(i);
                            }
                            String pub= android.text.TextUtils.join(",",publishedPlacesArray);
                            publishedPlacesView.setText("Published in: "+ pub);
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_share_book,menu);
        final MenuItem searchItem=menu.findItem(R.id.menuShare);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will automatically
        //handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml
        int id=item.getItemId();
        if(id==R.id.menuShare){
            processShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void processShareIntent() {
        //grab the image view itself
        ImageView bookCoverImgView=(ImageView)findViewById(R.id.bookCoverImg);
        final TextView tvTitle=(TextView)findViewById(R.id.book_details_title);
        //Get access to the uRI for the image/Bitmap- the image must be converted to bitmap for us to share it
        Uri bitmapUri=getLocalBitmapUri(bookCoverImgView);
        //COnstruct a share intent with link to image
        Intent shareIntent = new Intent();
        //construct a share Intent with link to image.
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT,(String)tvTitle.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM,bitmapUri);
        //Launch the share menu
        startActivity(Intent.createChooser(shareIntent,"Share Image"));
    }
    //returns the URI path to the Bitmap displayed in cover imageView
    //getLocaBitmapUri() extracts the Bitmap from the IMageView and loads it into a file in the default external storage directory
    private Uri getLocalBitmapUri(ImageView bookCoverImgView) {
        //Extract Bitmap from ImageView drawable
        Drawable drawable = bookCoverImgView.getDrawable();
        Bitmap bmp=null;
        if(drawable instanceof BitmapDrawable){
            bmp=((BitmapDrawable)bookCoverImgView.getDrawable()).getBitmap();
        }
        //store image to default external storage directory
        Uri bmpUri=null;
        try{
            //get the location of the pictures directory
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_"+ System.currentTimeMillis()+".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
