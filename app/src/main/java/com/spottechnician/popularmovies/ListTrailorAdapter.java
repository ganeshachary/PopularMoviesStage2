package com.spottechnician.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by OnesTech on 23/04/2016.
 */
public class ListTrailorAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    int img_id = R.drawable.ic_arrow;
    List<String> trailorlist;
    Context context;

    ListTrailorAdapter(Context context, List<String> trailorlist) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.trailorlist = trailorlist;
    }

    @Override
    public int getCount() {
        return trailorlist.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        int count = position;
        View MyConvertView = convertView;
        if (MyConvertView == null) {
            MyConvertView = inflater.inflate(R.layout.list_trailor, null);
            holder.tv = (TextView) MyConvertView.findViewById(R.id.list_trailor_text);
            holder.img = (ImageView) MyConvertView.findViewById(R.id.list_trailor_img);
            int trailorposition = count + 1;
            // Log.e("ERror lsit",count+"");
            holder.tv.setText("Trailor " + trailorposition);
            holder.img.setImageResource(img_id);


        } else {
            MyConvertView = convertView;
        }
        return MyConvertView;
    }

    class Holder {
        TextView tv;
        ImageView img;
    }


}
