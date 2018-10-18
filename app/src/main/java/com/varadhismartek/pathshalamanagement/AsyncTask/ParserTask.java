package com.varadhismartek.pathshalamanagement.AsyncTask;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.varadhismartek.pathshalamanagement.Fragment.Addroute;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by varadhi5 on 4/12/17.
 */

public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    Context mContext;
    GoogleMap googleMap;

    public ParserTask(Context context, GoogleMap map) {

        this.mContext=context;
        this.googleMap=map;
    }


    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {

            //here we are using the jsonobject becoz all the points are in the objects
            jObject = new JSONObject(strings[0]);
            Log.d("ParserTask",strings[0].toString());

            //calling the jparser class in the addroute fragment
            Addroute.JParser callingIneerClass = new Addroute().new JParser(mContext, googleMap);
            Log.d("ParserTask", callingIneerClass.toString());

            //inside the jparser here we are calling the parse method
            routes = callingIneerClass.parse(jObject);
            Log.d("ParserTask","Executing routes");
            Log.d("ParserTask",routes.toString());

        } catch (Exception e) {
            Log.d("ParserTask", e.toString());
            e.printStackTrace();
        }

        //returning the routes to the postexcute method
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
        super.onPostExecute(lists);

        //here on postexcute we are passing all the latlng points and storing in arraylist
        ArrayList<LatLng> points;

        //adding the polyline option
        PolylineOptions lineOptions = null;

        //getting the passed routes and loading its points
        for (int i = 0; i < lists.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = lists.get(i);
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            lineOptions.addAll(points);
            lineOptions.width(11);
            lineOptions.color(Color.BLUE);
        }
        if(lineOptions != null) {
            googleMap.addPolyline(lineOptions);
        }
        else {
            Log.d("onPostExecute","without Polylines drawn");

        }
    }
}
