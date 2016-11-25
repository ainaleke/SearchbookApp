package com.searchbook;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Olasumbo Ogunyemi on 11/17/2016.
 */
public class Book {
    private String openLibraryId;
    private String author;
    private String title;
    private String ISBN;

    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    public String getOpenLibraryId(){
        return openLibraryId;
    }
    public String getTitle(){

        return title;
    }
    public String getAuthor(){

        return author;
    }



    //Get book cover from covers API
    public String getCoverURL(){
        return "http://covers.openlibrary.org/b/olid/"+ openLibraryId+ "-M.jpg?default=false";
    }
    public static Book buildBookFromJson(JSONObject jsonObject){
        Book book=new Book();
        try{
            //deserialize json into object fields
            //check if a cover edition is available
            if(jsonObject.has("cover_edition_key")){
                book.openLibraryId=jsonObject.getString("cover_edition_key");
            }
            //edition_key comes as an array so we prepare to receive it
            else if(jsonObject.has("edition_key")){
                final JSONArray jsonArray=jsonObject.getJSONArray("edition_key");
                book.openLibraryId=jsonArray.getString(0);
            }

            //final JSONArray isbnArray=jsonObject.getJSONArray("isbn");
            book.ISBN=getISBN(jsonObject);

            book.title=jsonObject.has("title_suggest")? jsonObject.getString("title_suggest"):"";
            book.author=getAuthor(jsonObject);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return book;
    }
    private static String getISBN(JSONObject jsonObject) {
        try{
            final JSONArray isbnArray=jsonObject.getJSONArray("isbn");
            int numOfISBNVals=isbnArray.length();
            final String[] ISBNArray=new String[numOfISBNVals];
            for (int i = 0; i < numOfISBNVals; ++i) {
                ISBNArray[i] = isbnArray.getString(i);
            }
            return ISBNArray[0];
        }catch (JSONException ex){
            return "";
        }
    }
    // Return comma separated author list when there is more than one author
    private static String getAuthor(JSONObject jsonObject) {
        try{
            final JSONArray authors=jsonObject.getJSONArray("author_name");
            int numAuthors=authors.length();
            final String[] authorStrings=new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        }catch (JSONException ex){
            return "";
        }
    }

    // Decodes array of book json results into business model objects
    public static ArrayList<Book> buildBookFromJson(JSONArray jsonArray){
        ArrayList<Book> bookList = new ArrayList<>();
        //process each result in json array, decode and convert to business
        //object
        for(int i=0;i<jsonArray.length();i++){
            JSONObject bookJson;
            try{
                bookJson=jsonArray.getJSONObject(i);
            }catch(JSONException ex){
                ex.printStackTrace();
                continue;
            }
            //build each entry in the array into a Book object
            Book book=Book.buildBookFromJson(bookJson);
            if(book!=null){
                bookList.add(book);
            }
        }
        return bookList;
    }
}
