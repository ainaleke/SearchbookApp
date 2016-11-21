package netclient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Olasumbo Ogunyemi on 11/17/2016.
 */
public class BookClient {

    private static final String API_BASE_URL="http://openlibrary.org/";
    private AsyncHttpClient client;

    public BookClient(){
        this.client=new AsyncHttpClient();
    }
    private String getApiUrl(String relativeUrl){
        return API_BASE_URL + relativeUrl;
    }

    public void getBooks(String query, JsonHttpResponseHandler handler){
        try{
            String url=getApiUrl("search.json?q=");
            client.get(url + URLEncoder.encode(query,"utf-8"),handler);
        }catch(UnsupportedEncodingException ex){
            ex.printStackTrace();
        }

    }
}
