package com.searchbook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//import cz.msebera.android.httpclient.entity.mime.Header;
import cz.msebera.android.httpclient.Header;
import netclient.BookClient;

public class BookListActivity extends AppCompatActivity {

    private ListView bookListView;
    private BookAdapter bookAdapter;
    private BookClient client;
    private List<Book> bookList;
    String objectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Search for Books");
        bookListView=(ListView)findViewById(R.id.bookListView);

        bookList=new ArrayList<>();
        //initialize adapter
        //fetch the data remotely
        fetchBooks("the lord of the rings");

        bookAdapter =new BookAdapter(this,bookList);
        //bind adapter to listview
        bookListView.setAdapter(bookAdapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    //Executes an API Call to the OpenLibrary search endpoint, parses the results and converts them into an
    //array of book objects and binds them to adapter
    public void fetchBooks(String query){
        client =new BookClient();
        client.getBooks(query,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseObj){
                try{
                    JSONArray jsonArray;
                    if(responseObj !=null){
                        //get the docs json array
                        jsonArray=responseObj.getJSONArray("docs");
                        bookList=Book.buildBookFromJson(jsonArray);
                        //remove all books from the adapter
                        bookAdapter.clear();
                        //Load model objects into the adapter
                        for(Book book :bookList){
                            bookAdapter.add(book);//add book through the adapter
                        }
                        bookListView.setAdapter(bookAdapter);
                        bookAdapter.notifyDataSetChanged();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);
        MenuItem searchItem=menu.findItem(R.id.menuSearch);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                setTitle("Books by "+query);
                fetchBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //bookAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
