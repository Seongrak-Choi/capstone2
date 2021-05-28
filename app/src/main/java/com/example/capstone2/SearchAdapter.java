package com.example.capstone2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<String> potionList = null;
    private ArrayList<String> arrayList;
    public SearchAdapter(Context context, List<String> potionList) {
        this.context = context;
        this.potionList = potionList;
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<String>();
        this.arrayList.addAll(potionList);
    }

    public class ViewHolder {
        TextView tv_name;
    }

    @Override
    public int getCount() {
        return potionList.size();
    }

    @Override
    public String getItem(int position) {
        return potionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        final String potion = potionList.get(position);


        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_listview, null);
            // Locate the TextViews in listview_item.xml
            holder.tv_name = (TextView) view.findViewById(R.id.label);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tv_name.setText(potion);

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int index=0;
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).equals(potionList.get(position))){
                        index=i;
                    }
                }
                Intent intent = new Intent(context, LibraryContentsActivity.class);
                intent.putExtra("position", index);
                context.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        potionList.clear();
        if (charText.length() == 0) {
            potionList.addAll(arrayList);
        } else {
            for (String potion : arrayList) {
                String name = potion;
                if (name.toLowerCase().contains(charText)) {
                    potionList.add(potion);
                }
            }
        }
        notifyDataSetChanged();
    }

}

