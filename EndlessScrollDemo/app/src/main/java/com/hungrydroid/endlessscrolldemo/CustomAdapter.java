package com.hungrydroid.endlessscrolldemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {
    ArrayList<String> result;
    Context context;
    private static LayoutInflater inflater = null;


    public CustomAdapter(Activity mainActivity,   ArrayList<String> prgmNameList) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        context = mainActivity;
        //imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class ViewHolder {
        TextView tv;
        ImageView img;
        //TextView dt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.textView1);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tv.setText((position+1)+": "+result.get(position));


        //System.out.println(Constants.img_url+imageId[position].replace(".jpg","")+"_thumb");
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, result.get(position), Toast.LENGTH_LONG).show();


            }
        });
        return convertView;
    }

}