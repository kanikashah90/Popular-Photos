package com.kanikash.popularphotos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kanikash on 1/24/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    //getview method (int position)
    //by default is the toString() method

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item
        InstagramPhoto photo = getItem(position);
        //Check if we are using any recycle view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //Lookup the subview within the template
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        //Return the view for the data item
        tvCaption.setText(photo.username + "--" + photo.caption);
        ivImage.getLayoutParams().height = photo.height;
        //Reset the image from the recycled view
        ivImage.setImageResource(0);
        Picasso.with(getContext()).load(photo.url).into(ivImage);
        return convertView;
    }
}
