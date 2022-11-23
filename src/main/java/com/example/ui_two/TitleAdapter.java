package com.example.ui_two;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TitleAdapter extends ArrayAdapter<Title> {

    //storing all the names in the list
    private List<Title> products_list_item_layout;

    //context object
    private Context context;

    //constructor
    public TitleAdapter(Context context, int resource, List<Title> products_list_item_layout) {
        super(context, resource, products_list_item_layout);
        this.context = context;
        this.products_list_item_layout = products_list_item_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.products_list_item_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.product_title);
       // ImageView imageViewStatus = (ImageView) listViewItem.findViewById(R.id.imageViewStatus);

        //getting the current name
        Title title = products_list_item_layout.get(position);

        //setting the name to textview
        textViewName.setText(title.getTitle());

        //if the synced status is 0 displaying
        //queued icon
        //else displaying synced icon
//        if (name.getStatus() == 0)
//            imageViewStatus.setBackgroundResource(R.drawable.sync);
//        else
//            imageViewStatus.setBackgroundResource(R.drawable.ok);

        return listViewItem;
    }
}


