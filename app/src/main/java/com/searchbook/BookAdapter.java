package com.searchbook;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Olasumbo Ogunyemi on 11/21/2016.
 */
 public class BookAdapter extends ArrayAdapter<Book>{

    //View lookup cache
    private static class ViewHolder{
        public ImageView bookCoverImg;
        public TextView bookTitle;
        public TextView bookAuthor;
    }

    public BookAdapter(Context context, ArrayList<Book> booksList){
        super(context,0,booksList);
    }

    //translates a particular 'Book' given a position
    //into a relevant row within an AdapterView

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the data item for this position
        final Book book=getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view

        ViewHolder viewHolder;
        //The adapters are built to reuse Views, when a View is scrolled so that is no longer visible, it can be used for one of the new Views appearing. This reused View is the convertView.
        // If this is null it means that there is no recycled View and we have to create a new one, otherwise we should use it to avoid creating a new.
        if(convertView==null){
            //we must create a new view
            viewHolder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.book_item, parent, false);
            viewHolder.bookCoverImg=(ImageView)convertView.findViewById(R.id.imageBookCover);
            viewHolder.bookTitle=(TextView)convertView.findViewById(R.id.bookTitle);
            viewHolder.bookAuthor=(TextView)convertView.findViewById(R.id.bookAuthor);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //populate data into the template view using the data object
        viewHolder.bookTitle.setText(book.getTitle());
        viewHolder.bookAuthor.setText(book.getAuthor());
        Picasso.with(getContext()).load(Uri.parse(book.getCoverURL()))
                .placeholder(R.drawable.ic_nocover).into(viewHolder.bookCoverImg);

        //Return the completed viee to render on screen
        return convertView;
    }

}
