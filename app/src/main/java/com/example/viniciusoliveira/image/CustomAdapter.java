package com.example.viniciusoliveira.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<Spacecraft> spacecrafts;
    MainActivity mainActivity = new MainActivity();





    public CustomAdapter(Context c, ArrayList<Spacecraft> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    public CustomAdapter() {

    }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int i) {
        return spacecrafts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Boolean delete(int position)
    {
        try {
            spacecrafts.remove(position);

            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;

        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            //INFLATE CUSTOM LAYOUT
            view = LayoutInflater.from(c).inflate(R.layout.gridviewitem, viewGroup, false);
        }

        final Spacecraft s = (Spacecraft) this.getItem(i);
//
//        TextView nameTxt = (TextView) view.findViewById(R.id.nameTxt);
        ImageView img = (ImageView) view.findViewById(R.id.imageviewgrid);
        Button removePhoto = (Button) view.findViewById(R.id.remove_photo_button);
//        nameTxt.setText(s.getName());
        Picasso.with(c).load(s.getUri()).placeholder(R.drawable.ic_camera).into(img);

        //VIEW ITEM CLICK
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
                mainActivity.displayInputDialog(-1);
            }
        });

        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("eueehee" + i);
                spacecrafts.remove(i);
            }
        });



        return view;
    }
}