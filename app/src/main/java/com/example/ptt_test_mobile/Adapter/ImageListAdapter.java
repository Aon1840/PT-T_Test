package com.example.ptt_test_mobile.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ptt_test_mobile.Model.Image;
import com.example.ptt_test_mobile.R;

import java.util.ArrayList;

public class ImageListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Image> imageList;

    public ImageListAdapter(Context context, int layout, ArrayList<Image> imageList) {
        this.context = context;
        this.layout = layout;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.imageView = (ImageView) row.findViewById(R.id.imgFood);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Image image = imageList.get(position);

        byte[] selectImage = image.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(selectImage,0, selectImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
