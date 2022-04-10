package org.me.gcu.campbell_innes_s2125573;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ItemClass> {


    private Context mContext;
    int mResource;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<ItemClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        //Put these here for now will add to listview once it starts working
        String description = getItem(position).getDescription();
        String link = getItem(position).getLink();
        String geoPoint = getItem(position).getGeoPoint();
        String date = getItem(position).getDate();




        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource, parent, false);

        TextView tvName =(TextView) convertView.findViewById(R.id.titleText);

        tvName.setText(title);

        return convertView;
    }
}
