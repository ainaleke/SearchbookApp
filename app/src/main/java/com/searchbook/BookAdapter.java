package com.searchbook;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olasumbo Ogunyemi on 11/21/2016.
 */
 public class BookAdapter extends ArrayAdapter<Book> {

    public List<Book> bookList;
    public Context context;
    public List<Book> arraylist;


    //View lookup cache
    private static class ViewHolder{
        public ImageView bookCoverImg;
        public TextView bookTitle;
        public TextView bookAuthor;
    }

    public BookAdapter(Context context, List<Book> booksList){
        super(context, 0, booksList);//
        this.bookList = booksList;
        this.context = context;
        arraylist = new ArrayList<Book>();
        arraylist.addAll(booksList);
    }

    //translates a particular 'Book' given a position
    //into a relevant row within an AdapterView

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Book getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the data item for this position

        //Check if an existing view is being reused, otherwise inflate the view

        ViewHolder viewHolder;
        //The adapters are built to reuse Views, when a View is scrolled so that is no longer visible, it can be used for one of the new Views appearing. This reused View is the convertView.
        // If this is null it means that there is no recycled View and we have to create a new one, otherwise we should use it to avoid creating a new.
        if(convertView==null){
            //we must create a new view
            viewHolder=new ViewHolder();
           // LayoutInflater inflater=(LayoutInflater)LayoutInflater.from(context);  //context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
            //convertView =inflater.inflate(R.layout.book_item, null);
            viewHolder.bookCoverImg=(ImageView)convertView.findViewById(R.id.imageBookCover);
            viewHolder.bookTitle=(TextView)convertView.findViewById(R.id.bookTitle);
            viewHolder.bookAuthor=(TextView)convertView.findViewById(R.id.bookAuthor);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        final Book book=(Book)getItem(position);
        //populate data into the template view using the data object
        viewHolder.bookTitle.setText(book.getTitle());
        viewHolder.bookAuthor.setText(book.getAuthor());
        //load book cover image using Picasso
        Picasso.with(context).load(Uri.parse(book.getCoverURL()))
                .placeholder(R.drawable.ic_nocover).into(viewHolder.bookCoverImg);
        convertView.setFocusable(false);
        convertView.setFocusableInTouchMode(false);
        //Return the completed viee to render on screen
        return convertView;
    }
    public void filter(String charText){
//        if(charText==null){
//            return;
//        }
        charText = charText.toLowerCase();
        bookList.clear();
        if(charText.length()!=0){
            for(Book book:bookList){
                if(book.getTitle().toLowerCase().contains(charText)){
                    bookList.add(book);
                }else if(book.getAuthor().toLowerCase().contains(charText)){
                    bookList.add(book);
                }
            }
            notifyDataSetChanged();
        }
//        else{
//            bookList.addAll(arrayList);
//            notifyDataSetChanged();
//        }
    }

}
