package com.searchbook;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import netclient.BookClient;

public class BookListActivity extends AppCompatActivity {

    private ListView bookListView;
    private BookAdapter bookAdapter;
    private BookClient client;
    private List<Book> bookList;
    private static final String TAG = "BookListActivity";
    String queryString="";
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Lookup the swipeContainer and bind it to the View
        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        setTitle("Search for Books");
        bookListView=(ListView)findViewById(R.id.bookListView);

        bookList=new ArrayList<>();
        bookAdapter =new BookAdapter(this,bookList);
        bookListView.setAdapter(bookAdapter);

        queryString="The Lord of The Rings";
        fetchBooks(queryString);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                fetchBooks(BookListActivity.this.queryString);
                //Swipe Referesh is done
                //swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //when an item is clicked on the ListView, send the ISBN number and Book name to other activity
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(BookListActivity.this,DisplayBookActivity.class);
                Bundle bundle=new Bundle();
                Book book=(Book)bookListView.getItemAtPosition(position);
                bundle.putString("ISBN",book.getISBN());
                bundle.putString("Title",book.getTitle());
                bundle.putString("Cover URL",book.getCoverURL());

                Toast.makeText(BookListActivity.this,book.getTitle(),Toast.LENGTH_LONG).show();

                //start Activity
                Log.i(TAG,"Hello");
                Log.i(TAG,book.getAuthor());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //listener for nothing but it allow OnItemClickListener to work
        this.bookListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

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
        //show refresh animation before making HTTP Call
        swipeContainer.setRefreshing(true);
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
                        bookAdapter.addAll(bookList);

//                        for(Book book :bookList){
//                            bookAdapter.add(book);//add book through the adapter
//                        }
//                        bookListView.setAdapter(bookAdapter);
                        bookAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                }catch(JSONException jsonex){
                    jsonex.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // stopping swipe refresh
                swipeContainer.setRefreshing(false);
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
                BookListActivity.this.queryString=query;
                Log.i(TAG,"Query String: "+query);
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
