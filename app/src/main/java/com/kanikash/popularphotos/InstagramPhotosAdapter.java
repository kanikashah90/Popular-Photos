package com.kanikash.popularphotos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
        ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvComment1 = (TextView) convertView.findViewById(R.id.tvComment1);
        TextView tvComment2 = (TextView) convertView.findViewById(R.id.tvComment2);
        //Return the view for the data item
        //Formulate the date to show
        Date postDate = new Date(photo.photo_time * (long) 1000 );
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        tvTime.setText(postDate.getDate() + " " + months[postDate.getMonth()] + " " + postDate.getHours() + ":" + postDate.getMinutes());
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvLikes.setText("Likes" + " " + Integer.toString(photo.likes_count));
        if(photo.comment1.length() != 0) {
            tvComment1.setText(photo.comment1);
        }
        if(photo.comment2.length() != 0) {
            tvComment2.setText(photo.comment2);
        }
        //Reset the image from the recycled view
        ivImage.setImageResource(0);
        Picasso.with(getContext()).load(photo.url).into(ivImage);
        ivUserImage.setImageResource(0);
        Picasso.with(getContext()).load(photo.userImageUrl).into(ivUserImage);
        return convertView;
    }
}
