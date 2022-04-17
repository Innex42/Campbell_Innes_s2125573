package org.me.gcu.campbell_innes_s2125573;
//Innes Campbell
//S2125573
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ItemClass> {


    private Context mContext;
    int mResource;

    public ListAdapter(@NonNull Context context, int resource, LinkedList<ItemClass> item) {
        super(context, resource, item);
        mContext = context;
        mResource = resource;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        //Put these here for now will add to listview once it starts working
        String description = getItem(position).getDescription();
        Date startDate = getItem(position).getStartDate();
        Date endDate = getItem(position).getEndDate();
        String link = getItem(position).getLink();
        String geoPoint = getItem(position).getGeoPoint();
        String pubDate = getItem(position).getPubDate();
        Float latitude = getItem(position).getLatitude();
        Float longitude = getItem(position).getLongitude();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.titleText);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.descriptionText);
        TextView tvLink = (TextView) convertView.findViewById(R.id.linkText);
        TextView tvGeoPoint = (TextView) convertView.findViewById(R.id.geoPointText);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.pubDateText);
        TextView tvTotalDays = (TextView) convertView.findViewById(R.id.totalDaysText);
        Button mapsButton = (Button) convertView.findViewById(R.id.mapsButton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Hey look this button was clicked" + title);
                Intent mapsIntent = new Intent(getContext(),MapsActivity.class);
                mapsIntent.putExtra("latitude", latitude);
                mapsIntent.putExtra("longitude", longitude);
                mapsIntent.putExtra("title", title);
                mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getContext().startActivity(mapsIntent);
            }
        });

        Long difference = endDate.getTime() - startDate.getTime();
        Float daysBetween = (difference.floatValue() / (1000*60*60*24));
        Integer roundedDay = daysBetween.intValue();


        tvName.setText(title);
        tvDescription.setText(description);
        tvLink.setText(link);
        tvGeoPoint.setText(geoPoint);
        tvPubDate.setText(pubDate);
        if (roundedDay==0){
            tvTotalDays.setText("Today");
            tvTotalDays.setBackgroundColor(Color.GREEN);
            tvTotalDays.setTextColor(Color.BLACK);
        }else if(roundedDay <=10) {
            tvTotalDays.setText(roundedDay.toString() + " Days");
            tvTotalDays.setBackgroundColor(Color.GREEN);
            tvTotalDays.setTextColor(Color.BLACK);
        }else if(roundedDay <=60 && roundedDay >= 10) {
            tvTotalDays.setText(roundedDay.toString() + " Days");
            tvTotalDays.setBackgroundColor(Color.YELLOW);
            tvTotalDays.setTextColor(Color.BLACK);
        }else if(roundedDay >=60) {
            tvTotalDays.setText(roundedDay.toString() + " Days");
            tvTotalDays.setBackgroundColor(Color.RED);
            tvTotalDays.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}
