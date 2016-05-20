package com.greddit.sam.greddit.dummy;

import android.media.Image;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;

import com.greddit.sam.greddit.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 5/17/2016.
 */
public class PostAdapter extends ArrayAdapter<HashMap<String,String>>{
    public PostAdapter(Context context, ArrayList<HashMap<String,String>> postData) {
        super(context, R.layout.custom_row, postData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row,parent,false);

        String postTitle = getItem(position).get("title");
        String numComments = getItem(position).get("num_comments");
        TextView titleLabel = (TextView) customView.findViewById(R.id.titleLabel);
        TextView commentsLabel = (TextView) customView.findViewById(R.id.commentLabel);
        ImageView thumbnailImage = (ImageView) customView.findViewById(R.id.postThumbnail);

        titleLabel.setText(postTitle);
        commentsLabel.setText(String.format("%s comments", numComments));
        thumbnailImage.setImageResource(R.drawable.snoo);

        return customView;
    }
}
