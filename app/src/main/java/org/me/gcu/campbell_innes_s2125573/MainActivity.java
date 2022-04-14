package org.me.gcu.campbell_innes_s2125573;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;




public class MainActivity extends AppCompatActivity implements
        OnClickListener
{
    private Button currentIncidentsButton;
    private Button plannedRWButton;
    private Button roadworksButton;
    private Button mapsButton;
    private TextView rawDataDisplay;
    private ListView listview;
    private Button startButton;
    private String result = "";
    private String[] items;
    private String url1="";
    private Integer itemCounter=0;
    XmlPullParser parser = Xml.newPullParser();
    // Traffic Scotland Planned Roadworks XML link
    private String
            urlSource;
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
        listview=(ListView)findViewById(R.id.listView);


        currentIncidentsButton.setOnClickListener(this);
        plannedRWButton.setOnClickListener(this);
        roadworksButton.setOnClickListener(this);
        Log.e("MyTag","after startButton");
        // More Code goes here
    }
    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //
    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.roadworksButton:
                urlSource="https://trafficscotland.org/rss/feeds/roadworks.aspx";
                break;
            case R.id.plannedRWButton:
                urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
                break;
            case R.id.currentIncidentsButton:
                urlSource="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
                break;
        }
        Log.e("MyTag","in onClick");
        startProgress();
        Log.e("MyTag","after startProgress");
    }
    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;
        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
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
                //
                // Now read the data. Make sure that there are no specific hedrs

                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries

                //

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);
                    itemCounter=itemCounter+1;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            System.out.println(result);
            //parsing starts here
            LinkedList<ItemClass> alist = null;
            items = new String[itemCounter];

            //Make call to parsing code
            //Note this is not the best location
            alist = parseData(result);

            // Write list to Log for testing
            if (alist != null)
            {
                Log.e("MyTag","List not null");
                int count = 0;
                for (Object o : alist)
                {
                    Log.e("MyTag",o.toString());
                    items[count] = o.toString();
                    count = count + 1;
                }
                Log.e("My Tag", "Array is " + items.toString());
            }
            else
            {
                Log.e("MyTag","List is null");
            }



            LinkedList<ItemClass> finalAlist = alist;
            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    //rawDataDisplay.setText(result);
                    listview.setAdapter(null);
                    ListAdapter adapter =new ListAdapter(getApplicationContext(), R.layout.item_layout, finalAlist);
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