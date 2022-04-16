package org.me.gcu.campbell_innes_s2125573;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;




public class MainActivity extends AppCompatActivity implements
        OnClickListener
{
    private Button currentIncidentsButton;
    private Button plannedRWButton;
    private Button roadworksButton;
    private ProgressBar progressBar;
    private ListView listview;
    private String ciResult = "";
    private String pRWResult = "";
    private String rwResult = "";
    private String[] ciItems;
    private String[] pRWItems;
    private String[] rwItems;
    private String cIUrlSource ="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String pRWUrlSource ="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String roadworksUrlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    LinkedList<ItemClass> alist;
    LinkedList<ItemClass> curIncList;
    LinkedList<ItemClass> planRWList;
    LinkedList<ItemClass> roadworksList;

    Boolean listisPRW =false;
    Boolean listisRW = false;
    Boolean listisCI = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components

        currentIncidentsButton = (Button)findViewById(R.id.currentIncidentsButton);
        plannedRWButton=(Button)findViewById(R.id.plannedRWButton);
        roadworksButton=(Button)findViewById(R.id.roadworksButton);
        progressBar = (ProgressBar)findViewById(R.id.pBar);
        listview=(ListView)findViewById(R.id.listView);
        progressBar.setVisibility(View.GONE);

        searchWidget();
        currentIncidentsButton.setOnClickListener(this);
        plannedRWButton.setOnClickListener(this);
        roadworksButton.setOnClickListener(this);

        Log.e("MyTag","after startButton");
    }

    private void searchWidget(){
        SearchView searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LinkedList<ItemClass> filteredList = new LinkedList<ItemClass>();

                if(listisRW){
                    for(ItemClass item: roadworksList){
                        if(item.getTitle().toLowerCase().contains(s.toLowerCase())){
                            filteredList.add(item);
                        }
                    }
                }else if (listisPRW){
                    for(ItemClass item: planRWList){
                        if(item.getTitle().toLowerCase().contains(s.toLowerCase())){
                            filteredList.add(item);
                        }
                    }
                } else if (listisCI){
                    for(ItemClass item: curIncList){
                        if(item.getTitle().toLowerCase().contains(s.toLowerCase())){
                            filteredList.add(item);
                        }
                    }
                }

                ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.item_layout, filteredList);
                listview.setAdapter(adapter);

                return false;
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        progressBar.setVisibility(v.VISIBLE);
        switch(v.getId()){
            case R.id.roadworksButton:
                //urlSource="https://trafficscotland.org/rss/feeds/roadworks.aspx";
                listisRW=true;
                new Thread(new RoadworksDownload(roadworksUrlSource)).start();
                break;
            case R.id.plannedRWButton:
                //urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
                listisPRW=true;
                new Thread(new PlannedRoadworksDownload(pRWUrlSource)).start();
                break;
            case R.id.currentIncidentsButton:
                //urlSource="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
                listisCI=true;
                new Thread(new CurrentIncidentsDownload(cIUrlSource)).start();
                break;
        }
        Log.e("MyTag","in onClick");
        //Log.e("MyTag","after progressbar INVISIBLE");
    }

    private class CurrentIncidentsDownload implements Runnable
    {
        private String url;
        private Integer itemCounter=0;
        public CurrentIncidentsDownload(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            if (curIncList != null){
                curIncList.clear();
            }
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            Log.e("MyTag","in run");
            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new
                        InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                while ((inputLine = in.readLine()) != null)
                {
                    ciResult = ciResult + inputLine;
                    Log.e("MyTag",inputLine);
                    itemCounter=itemCounter+1;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            System.out.println(ciResult);
            //parsing starts here
            ciItems = new String[itemCounter];

            //Make call to parsing code
            //Note this is not the best location
            curIncList = parseData(ciResult);

            // Write list to Log for testing
            if (curIncList != null)
            {
                Log.e("MyTag","List not null");
                int count = 0;
                for (Object o : curIncList)
                {
                    Log.e("MyTag",o.toString());
                    ciItems[count] = o.toString();
                    count = count + 1;
                }
                Log.e("My Tag", "Array is " + ciItems.toString());
            }
            else
            {
                Log.e("MyTag","List is null");
            }




            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    progressBar.setVisibility(View.GONE);
                    //rawDataDisplay.setText(result);
                    listview.setAdapter(null);
                    ListAdapter adapter =new ListAdapter(getApplicationContext(), R.layout.item_layout, curIncList);
                    listview.setAdapter(adapter);
                }
            });
        }
    }

    private class PlannedRoadworksDownload implements Runnable
{
    private String url;
    private Integer itemCounter=0;
    public PlannedRoadworksDownload(String aurl)
    {
        url = aurl;
    }
    @Override
    public void run()
    {
        if (planRWList != null){
            planRWList.clear();
        }
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        Log.e("MyTag","in run");
        try
        {
            Log.e("MyTag","in try");
            aurl = new URL(url);
            yc = aurl.openConnection();
            in = new BufferedReader(new
                    InputStreamReader(yc.getInputStream()));
            Log.e("MyTag","after ready");
            while ((inputLine = in.readLine()) != null)
            {
                pRWResult = pRWResult + inputLine;
                Log.e("MyTag",inputLine);
                itemCounter=itemCounter+1;
            }
            in.close();
        }
        catch (IOException ae)
        {
            Log.e("MyTag", "ioexception in run");
        }

        System.out.println(pRWResult);
        //parsing starts here
        pRWItems = new String[itemCounter];

        //Make call to parsing code
        //Note this is not the best location
        planRWList = parseData(pRWResult);

        // Write list to Log for testing
        if (planRWList != null)
        {
            Log.e("MyTag","List not null");
            int count = 0;
            for (Object o : planRWList)
            {
                Log.e("MyTag",o.toString());
                pRWItems[count] = o.toString();
                count = count + 1;
            }
            Log.e("My Tag", "Array is " + pRWItems.toString());
        }
        else
        {
            Log.e("MyTag","List is null");
        }




            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    progressBar.setVisibility(View.GONE);
                    //rawDataDisplay.setText(result);
                    listview.setAdapter(null);
                    ListAdapter adapter =new ListAdapter(getApplicationContext(), R.layout.item_layout, planRWList);
                    listview.setAdapter(adapter);
                }
            });
    }
}


    private class RoadworksDownload implements Runnable
    {
        private String url;
        private Integer itemCounter=0;
        public RoadworksDownload(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            if (roadworksList != null){
                roadworksList.clear();
            }
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            Log.e("MyTag","in run");
            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new
                        InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                while ((inputLine = in.readLine()) != null)
                {
                    rwResult = rwResult + inputLine;
                    Log.e("MyTag",inputLine);
                    itemCounter=itemCounter+1;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            System.out.println(rwResult);
            //parsing starts here
            rwItems = new String[itemCounter];

            //Make call to parsing code
            //Note this is not the best location
            roadworksList = parseData(rwResult);

            // Write list to Log for testing
            if (roadworksList != null)
            {
                Log.e("MyTag","List not null");
                int count = 0;
                for (Object o : roadworksList)
                {
                    Log.e("MyTag",o.toString());
                    rwItems[count] = o.toString();
                    count = count + 1;
                }
                Log.e("My Tag", "Array is " + rwItems.toString());
            }
            else
            {
                Log.e("MyTag","List is null");
            }




            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    //rawDataDisplay.setText(result);
                    progressBar.setVisibility(View.GONE);
                    listview.setAdapter(null);
                    ListAdapter adapter =new ListAdapter(getApplicationContext(), R.layout.item_layout, roadworksList);
                    listview.setAdapter(adapter);
                }
            });
        }
    }





    public LinkedList<ItemClass> parseData(String dataToParse)
    {

        LinkedList <ItemClass> alist = null;
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            boolean withinItem = false;

            ItemClass item= null;

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        alist  = new LinkedList<ItemClass>();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        withinItem=true;
                        Log.e("MyTag","Item Start Tag found");
                        item = new ItemClass();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        if (withinItem) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag", "Title is " + temp);
                            item.setTitle(temp);
                        }
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            if (withinItem) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Description is " + temp);
                                item.setDescription(temp);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                                //if (!inCI){
                                String[] descriptionSplit = temp.split("/>");
                                String[] startTimeSplit = descriptionSplit[0].split("e: ");
                                if(startTimeSplit[0]==temp){
                                    try {
                                        Date startDate = formatter.parse(formatter.format(new Date()));
                                        Date endDate = formatter.parse(formatter.format(new Date()));
                                        item.setStartDate(startDate);
                                        item.setEndDate(endDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    startTimeSplit = startTimeSplit[1].split("<br");
                                    startTimeSplit = startTimeSplit[0].split(", ");
                                    startTimeSplit = startTimeSplit[1].split(" - ");
                                    //above [0] is in good format for date

                                    try {
                                        Date startDate = formatter.parse(startTimeSplit[0]);
                                        System.out.println(startDate);
                                        item.setStartDate(startDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    String[] endTimeSplit = descriptionSplit[1].split("e: ");
                                    endTimeSplit = endTimeSplit[1].split(", ");
                                    endTimeSplit = endTimeSplit[1].split(" - ");
                                    //above [0] is in good format for date
                                    try {
                                        Date endDate = formatter.parse(endTimeSplit[0]);
                                        System.out.println(endDate);
                                        item.setEndDate(endDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                /*for (String a : endTimeSplit){
                                    System.out.println(a);
                                }*/
                                    // }
                                }

                            }
                        }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("link"))
                            {
                                if(withinItem) {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag", "link is " + temp);
                                    item.setLink(temp);
                                }
                            }
                            else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("point"))
                                {
                                    if(withinItem) {
                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        // Do something with text
                                        Log.e("MyTag", "GeoRSS:Point is " + temp);
                                        item.setGeoPoint(temp);
                                        String[] coordSplit = temp.split(" ");
                                        Float lat = Float.valueOf(coordSplit[0]);
                                        Float longi = Float.valueOf(coordSplit[1]);
                                        item.setLatitude(lat);
                                        item.setLongitude(longi);

                                        //for (String a : coordSplit)
                                            //System.out.println(a);
                                    }
                                }
                                else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("pubDate"))
                                    {
                                        if(withinItem) {
                                            // Now just get the associated text
                                            String temp = xpp.nextText();
                                            // Do something with text
                                            Log.e("MyTag", "PubDate is " + temp);
                                            item.setPubDate(temp);
                                        }
                                    }


                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        Log.e("MyTag", "item is " + item.toString());
                        alist.add(item);


                    }else if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = alist.size();
                        Log.e("MyTag","channel size is " + size);
                    }


                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            return alist;
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        return alist;

    }

}