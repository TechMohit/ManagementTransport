package com.varadhismartek.pathshalamanagement.Fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.varadhismartek.pathshalamanagement.Activity.BottomBarPage;
import com.varadhismartek.pathshalamanagement.Adapter.ModifyRecyclerAdapter;
import com.varadhismartek.pathshalamanagement.Adapter.PlaceArrayAdapter;
import com.varadhismartek.pathshalamanagement.Adapter.StopviewRecyclerAdapter;
import com.varadhismartek.pathshalamanagement.AsyncTask.FetchUrl;
import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.POJO_Classes.JSONParsePojo;
import com.varadhismartek.pathshalamanagement.POJO_Classes.MarkerLists;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Stop_Address;
import com.varadhismartek.pathshalamanagement.R;
import com.varadhismartek.pathshalamanagement.Utilclasses.ArryalistClasses;
import com.varadhismartek.pathshalamanagement.Utilclasses.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Addroute extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener, OnMapReadyCallback {

    static GoogleMap map;
    public static LatLngBounds latLngBounds;
    public GoogleApiClient googleApiClient;
    public PlaceArrayAdapter mPlacearrayadpater;
    AutoCompleteTextView mAutoCompleteTextView;
    EditText ed_route_dist, ed_route_time;

    Button btn_Add, btn_view, btn_fetch;
    RecyclerView rv_stopview;
    StopviewRecyclerAdapter recyclerAdapter;
    ArrayList<AddStop> arrayList;
    HashMap<Integer, Marker> markerHashMap;

    ArrayList<Marker> markerArrayList;
    ArrayList<Marker> markerArrayList1;
    Marker marker;

    Marker marker1;

    static int button_clik_count = 0;
    static String stop_counts;
    static int stop_num;


    LatLng stops;
    LatLng latLng;
    String names;
    Double latitude, longitutde;
    ArrayList<Stop_Address> stop_addressList;

    ArrayList<JSONParsePojo> jsonParsePojo;

    ArrayList<MarkerLists> markerLists;


    LatLng origin, dest;
    Geocoder geocoder;
    Geocoder geocoder1;

    String Fetch_waypoints;

    Bundle bundle;


    LocationManager locationManager;
    Location fetchlocate;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    Location location;
    Double Fetchlatitude, Fetchlongitude;


    SupportMapFragment supportMapFragment;

    DatabaseReference databaseReference;
    DatabaseReference stops_ref;
    static String Waypoints_Url;

    int tag_transport = 20;
    int tag_modifychange = 10;
    LatLng returnlatlng;
    ModifyRecyclerAdapter modifyRecyclerAdapter;
    List<Address>  addresses;
    Double lat1,lng1;

    String routeno,Origin_Name=" ",Dest_Name=" ",Stop_Latlng;



    /*public static Addroute newInstance() {

        Addroute addroute = new Addroute();
        addroute.getArguments();
        return addroute;

    }*/

    public Addroute() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_addroute, container, false);

        bundle = getArguments();

        Log.d("arguments_getting", bundle + "");

        if (bundle == null)
        {
            Toast.makeText(getContext(),"on bundle null",Toast.LENGTH_LONG).show();
            //Addroute.newInstance();
            //  Toast.makeText(getContext(), "Please create the route", Toast.LENGTH_LONG).show();

        } else if (bundle != null) {

            Toast.makeText(getContext(),"on bundle value",Toast.LENGTH_LONG).show();

            stop_counts = bundle.getString("stop_counts");
            stop_num=Integer.parseInt(stop_counts);
            Origin_Name = bundle.getString("starting");
            Dest_Name = bundle.getString("ending");

/*            //getting the origin and destination
            Constants.AddrouteOrigin=bundle.getParcelable("originlatlng");
            Constants.AddrouteDestiny=bundle.getParcelable("destinylatlng");*/

            routeno=bundle.getString("routeno");
            databaseReference = FirebaseDatabase.getInstance().getReference("School/SchoolId/Transport/Routes/" + routeno);

            Log.d("bundletransport",Constants.AddrouteOrigin+" "+Constants.AddrouteDestiny+" "+routeno+ " "+Origin_Name+" "+Dest_Name);


        }

        Toast.makeText(getContext(),"created the view",Toast.LENGTH_LONG).show();


        mAutoCompleteTextView = view.findViewById(R.id.add_stop_name);
        ed_route_dist = view.findViewById(R.id.add_stop_distance);
        ed_route_time = view.findViewById(R.id.add_stop_time);
        btn_Add = view.findViewById(R.id.add_routes);
        btn_view = view.findViewById(R.id.viewstops);
        btn_fetch = view.findViewById(R.id.current_location);
        rv_stopview = view.findViewById(R.id.add_route_recyclerview);


        rv_stopview.setHasFixedSize(true);
        rv_stopview.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));

        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapfragid);
        supportMapFragment.getMapAsync(this);

        //setting up the latlng for the latlngbounds for the places search
        latLngBounds = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));


        if (googleApiClient==null||!googleApiClient.isConnected())
        {
            try {

                googleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Places.GEO_DATA_API)
                        .enableAutoManage((FragmentActivity) getContext(), 1, this)
                        .addConnectionCallbacks(this)
                        .build();
            }catch (Exception e)
            {
                Log.d("exception",e.getMessage());
            }
        }


        //setting up for autocomplete textview
        mAutoCompleteTextView.setThreshold(2);

        //setting an adapter for the autocompletextview
        mPlacearrayadpater = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, latLngBounds, null);
        mAutoCompleteTextView.setAdapter(mPlacearrayadpater);
        mAutoCompleteTextView.setOnItemClickListener(mAutocompleteItemclick);


        int id=bundle.getInt("TAG");

                if (id==Constants.TAG_TRANSPORT)
                {
                    //getting the origin and destination
                    Constants.AddrouteOrigin=bundle.getParcelable("originlatlng");
                    Constants.AddrouteDestiny=bundle.getParcelable("destinylatlng");

                    //method to call the dialogbox
                    //createRouteDialog();

                    Toast.makeText(getContext(), "Passed from the transport", Toast.LENGTH_LONG).show();

                    //putting the marker on the map
                    markerHashMap = new HashMap<>();

                    //arraylist for map
                    markerArrayList = new ArrayList<>();

                    markerLists = new ArrayList<>();

                    stop_addressList = new ArrayList<>();

                    //this is for the recyclerview;
                    arrayList = new ArrayList<>();


                    byTransport();


                }

            else if (id==Constants.TAG_MODIFYCHANGE)
            {
                //getting the origin and destination
                Constants.AddrouteOrigin=bundle.getParcelable("originlatlng");
                Constants.AddrouteDestiny=bundle.getParcelable("destinylatlng");

                arrayList = new ArrayList<>();
                stop_addressList = new ArrayList<>();
                markerArrayList = new ArrayList<>();

                if (map == null) {

                        Toast.makeText(getContext(), "map is null", Toast.LENGTH_LONG).show();
                        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapfragid);
                        supportMapFragment.getMapAsync(this);

                    }

                Constants.number_of_counts=bundle.getInt("stop_old_counts");


                geocoder1 = new Geocoder(getActivity());

                arrayList.addAll((ArrayList<AddStop>) getArguments().getSerializable("arraylist"));
                loadingStops();

                    Toast.makeText(getContext(), "Passed from the modifychange", Toast.LENGTH_LONG).show();

                   /* supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapfragid);
                    supportMapFragment.getMapAsync(this);*/


                    Log.d("arraylistclasses", arrayList.size() + "");



                /* passing the values to the adapter

                arrylist is for passing the addstop list
                20 is the value for loading the cardview with image visibility gone
                marker is for deleting the marker
                stopaddress list is for the while deleting the marker the waypoints also want to delete

                */
                    Log.d("arrylistndstopaddress",arrayList.size()+" "+stop_addressList.size()+"");

                    modifyRecyclerAdapter = new ModifyRecyclerAdapter(getContext(), arrayList, 20, marker, ArryalistClasses.addressArrayList,map,markerArrayList);
                    modifyRecyclerAdapter.notifydata(arrayList, markerArrayList,ArryalistClasses.addressArrayList);
                    rv_stopview.setAdapter(modifyRecyclerAdapter);

                    btn_Add.setOnClickListener(this);
                    btn_view.setOnClickListener(this);
                    btn_fetch.setOnClickListener(this);


                    // byTransport();
                }


        return view;

    }


    private void byTransport() {

        //getting the routeno from the bundle
        String stops;
        stops=bundle.getString("routeno");

        Constants.number_of_counts= 0;

        databaseReference= FirebaseDatabase.getInstance().getReference("School/SchoolId/Transport/Routes/"+stops);


        geocoder1 = new Geocoder(getActivity());


        //arraylist for map
        markerArrayList = new ArrayList<>();


        Log.d("arguments_getting", bundle + "");

        stop_addressList = new ArrayList<>();


        rv_stopview.setHasFixedSize(true);
        rv_stopview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        btn_Add.setOnClickListener(this);
        btn_view.setOnClickListener(this);
        btn_fetch.setOnClickListener(this);


        //setting the map
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragid);
        supportMapFragment.getMapAsync(this);


        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        latLngBounds = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

        if (googleApiClient==null||!googleApiClient.isConnected())
        {
            try {

                googleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Places.GEO_DATA_API)
                        .enableAutoManage((FragmentActivity) getContext(), 1, this)
                        .addConnectionCallbacks(this)
                        .build();
            }catch (Exception e)
            {
                Log.d("exception",e.getMessage());
            }
        }


        //setting up for autocomplete textview
        mAutoCompleteTextView.setThreshold(2);

        //setting an adapter for the autocompletextview
        mPlacearrayadpater = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, latLngBounds, null);
        mAutoCompleteTextView.setAdapter(mPlacearrayadpater);
        mAutoCompleteTextView.setOnItemClickListener(mAutocompleteItemclick);


    }


    public void loadingStops()
    {
        for (int i = 0; i < arrayList.size(); i++)
        {
            AddStop addStop = arrayList.get(i);

            try {

                addresses = geocoder1.getFromLocationName(addStop.getStop_name(), 1);
                String stp_name = addStop.getStop_name();

                try {

                    if (addresses != null) {
                        lat1 = addresses.get(0).getLatitude();
                        lng1 = addresses.get(0).getLongitude();


                        //for getting up the marker i have used stop addresslist arraylist in that i am storing it the value got from the addstop arraylist.
                        ArryalistClasses.addressArrayList.add(new Stop_Address(stp_name, lat1, lng1));

                    } else {

                        Toast.makeText(getContext(), "No address found", Toast.LENGTH_LONG).show();

                    }
                } catch (IndexOutOfBoundsException e) {

                    Toast.makeText(getContext(), e.getMessage() + " ", Toast.LENGTH_LONG).show();

                }
                //LatLng returnlatlng=new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());

                Log.d("returnlatlng", lat1 + " " + lng1 + "");

                Log.d("returnname", addStop.getStop_name());

                Log.d("stopaddresslist3", ArryalistClasses.addressArrayList.size() + "");


                //declaring marker and storing the current marker


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.add_routes:

                String stop_name;
                String stop_dist;
                String stop_time;

                Log.d("addroutebuttontag",bundle.getInt("TAG")+"");

                    if (bundle.getInt("TAG")==Constants.TAG_TRANSPORT)
                    {

                        Log.d("addroutebuttonget","in transport tag");

                        if (Constants.number_of_counts < stop_num)
                        {
                            stop_name = mAutoCompleteTextView.getText().toString();
                            stop_dist = ed_route_dist.getText().toString();
                            stop_time = ed_route_time.getText().toString();
                            if (stop_name.isEmpty()) {
                                mAutoCompleteTextView.setError("Enter the route name");
                            }
                            else if (stop_dist.isEmpty()) {
                                ed_route_dist.setError("Enter the route distance");
                            }
                            else if (stop_time.isEmpty()) {
                                ed_route_time.setError("Enter the time");
                            }
                            else {


                                Constants.number_of_counts += 1;
                                //this is for the recyclerview card data
                                AddStop addStop = new AddStop(String.valueOf(Constants.number_of_counts), stop_name, stop_dist, stop_time,Origin_Name,Dest_Name,Stop_Latlng);
                                arrayList.add(addStop);

                                //this is for the waypoints
                                Stop_Address stop_address = new Stop_Address(names, latitude, longitutde);
                                stop_addressList.add(stop_address);


                                markerLists.add(new MarkerLists(names, latitude, longitutde, latLng));

                                //markerready(markerLists);

                                //declaring marker and storing the current marker
                                marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(names));

                                //markerHashMap.put(button_clik_count,marker);
                                markerArrayList.add(marker);


                                //   map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(names));


                                recyclerAdapter = new StopviewRecyclerAdapter(getContext(), arrayList, Constants.number_of_counts, stop_addressList, markerArrayList, map, markerLists,marker);

                                recyclerAdapter.notifyData(arrayList, stop_addressList,markerArrayList,markerLists);
                                mAutoCompleteTextView.setText("");
                                ed_route_dist.setText("");
                                ed_route_time.setText("");

                                rv_stopview.setAdapter(recyclerAdapter);


                            }

                        }
                        else
                        {
                            Toast.makeText(getContext(), "you have already entered " + stop_num + " stops", Toast.LENGTH_SHORT).show();
                        }
                    }


                    else if (bundle.getInt("TAG")==Constants.TAG_MODIFYCHANGE)
                    {
                        Log.d("addroutebuttonget","in modify tag");

                        if (Constants.number_of_counts < stop_num)
                        {
                            stop_name = mAutoCompleteTextView.getText().toString();
                            stop_dist = ed_route_dist.getText().toString();
                            stop_time = ed_route_time.getText().toString();
                            if (stop_name.isEmpty()) {
                                mAutoCompleteTextView.setError("Enter the route name");
                            } else if (stop_dist.isEmpty()) {
                                ed_route_dist.setError("Enter the route distance");
                            } else if (stop_time.isEmpty()) {
                                ed_route_time.setError("Enter the time");
                            } else {

                                arrayList.add(new AddStop(String.valueOf(Constants.number_of_counts),names,stop_dist,stop_time,Origin_Name,Dest_Name,Stop_Latlng));

                                //to add the newly added data into the stopaddress list
                                Stop_Address stop_address = new Stop_Address(names, latitude, longitutde);
                                ArryalistClasses.addressArrayList.add(stop_address);

                                Log.d("stopaddresslist4", ArryalistClasses.addressArrayList.size() + "");

                                Log.d("addroutebutton",ArryalistClasses.addressArrayList.size()+"");


                                marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(names));

                                markerArrayList.add(marker);


                                modifyRecyclerAdapter.notifydata(arrayList,markerArrayList,ArryalistClasses.addressArrayList);
                                modifyRecyclerAdapter.notifyDataSetChanged();

                                Constants.number_of_counts+=1;

                            }

                        }

                        else
                        {
                            Toast.makeText(getContext(), "you have already entered " + stop_num + " stops", Toast.LENGTH_SHORT).show();
                        }

                    }

                break;

            //for viewing the waypoints
            case R.id.viewstops:

                if (bundle.getInt("TAG")==Constants.TAG_TRANSPORT) {

                    try {

                        String origin_val = "origin=" + Constants.AddrouteOrigin.latitude + "," + Constants.AddrouteOrigin.longitude;
                        String dest_value = "destination=" + Constants.AddrouteDestiny.latitude + "," + Constants.AddrouteDestiny.longitude;
                        String sensor = "sensor=false";


                        StringBuilder builder = new StringBuilder("");
                        builder.append("waypoints=");
                        String stpname;

                        Double lat, lng;

                        for (int i = 0; i < stop_addressList.size(); i++) {

                            Stop_Address stop_address = stop_addressList.get(i);
                            lat = stop_address.getLatitude();
                            lng = stop_address.getLongitude();
                            builder.append(lat).append(",").append(lng).append("").append("|");

                            if (i == stop_addressList.size() - 1) {
                                builder.append(lat).append(",").append(lng);
                            }

                        }


                        String parameters = origin_val + "&" + dest_value + "&" + sensor + "&" + builder;
                        String output = "json";
                        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


                        Log.d("viewstops", url);
                        Waypoints_Url = url;

                        stops_ref = databaseReference.child("Routestops");
                        stops_ref.child("url").setValue(Waypoints_Url);

                        stops_ref.child("stoplist").setValue(arrayList);


                        //fetching the detailste of that url
                        FetchUrl fetchUrl = new FetchUrl(getContext(), map);
                        fetchUrl.execute(url);

                        // map.moveCamera(CameraUpdateFactory.newLatLng(origin));

                        map.moveCamera(CameraUpdateFactory.newLatLng(Constants.AddrouteOrigin));
                        map.animateCamera(CameraUpdateFactory.zoomTo(11));

                    } catch (Exception e) {

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                else if (bundle.getInt("TAG")==Constants.TAG_MODIFYCHANGE)
                {

                    try {

                        String origin_val = "origin=" + Constants.AddrouteOrigin.latitude + "," + Constants.AddrouteOrigin.longitude;
                        String dest_value = "destination=" + Constants.AddrouteDestiny.latitude + "," + Constants.AddrouteDestiny.longitude;
                        String sensor = "sensor=false";


                        StringBuilder builder = new StringBuilder("");
                        builder.append("waypoints=");

                        Double lat, lng;

                        Log.d("stopaddresslist1", ArryalistClasses.addressArrayList.size() + "");

                        for (int i = 0; i < ArryalistClasses.addressArrayList.size(); i++) {

                            Stop_Address stop_address = ArryalistClasses.addressArrayList.get(i);
                            lat = stop_address.getLatitude();
                            lng = stop_address.getLongitude();
                            builder.append(lat).append(",").append(lng).append("").append("|");

                            if (i == ArryalistClasses.addressArrayList.size() - 1) {
                                builder.append(lat).append(",").append(lng);
                            }

                        }

                        Log.d("stopaddresslist1.2", ArryalistClasses.addressArrayList.size() + "");


                        String parameters = origin_val + "&" + dest_value + "&" + sensor + "&" + builder;
                        String output = "json";
                        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


                        Log.d("viewstops", url);
                        Waypoints_Url = url;

                        stops_ref = databaseReference.child("Routestops");
                        stops_ref.child("url").setValue(Waypoints_Url);

                        stops_ref.child("stoplist").setValue(arrayList);


                        //fetching the detailste of that url
                        FetchUrl fetchUrl = new FetchUrl(getContext(), map);
                        fetchUrl.execute(url);

                        // map.moveCamera(CameraUpdateFactory.newLatLng(origin));

                        map.moveCamera(CameraUpdateFactory.newLatLng(Constants.AddrouteOrigin));
                        map.animateCamera(CameraUpdateFactory.zoomTo(11));

                    } catch (IllegalStateException e) {

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                }

                break;

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map=googleMap;

        Toast.makeText(getContext(),"map loaded",Toast.LENGTH_LONG).show();


        //setting up the marker for origin and destination

            if (bundle.getParcelable("originlatlng")!=null&&bundle.getParcelable("destinylatlng")!=null)
            {
                map.addMarker(new MarkerOptions().position((LatLng) bundle.getParcelable("originlatlng")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("start address"));
                map.addMarker(new MarkerOptions().position((LatLng) bundle.getParcelable("destinylatlng")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("End address"));

                //map.moveCamera(CameraUpdateFactory.newLatLng(origin));

                map.moveCamera(CameraUpdateFactory.newLatLng((LatLng) bundle.getParcelable("originlatlng")));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));
            }

           else if (Constants.AddrouteOrigin!=null && Constants.AddrouteDestiny!=null)
            {
              //  List<Address> addresses=geocoder.getFromLocation(origin.latitude,origin.longitude,1);
             //   List<Address> addresses1=geocoder.getFromLocation(dest.latitude,dest.longitude,1);

             //   map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("start address"));
             //   map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("End address"));



                map.addMarker(new MarkerOptions().position(Constants.AddrouteOrigin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("start address"));
                map.addMarker(new MarkerOptions().position(Constants.AddrouteDestiny).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("End address"));

                //map.moveCamera(CameraUpdateFactory.newLatLng(origin));

                map.moveCamera(CameraUpdateFactory.newLatLng(Constants.AddrouteOrigin));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));
            }

            if (bundle.getInt("TAG")==Constants.TAG_MODIFYCHANGE)
            {

                mapByModifyPage();

               /* map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));*/


                Log.d("checkkk", markerArrayList.size() + "");
            }

       /* if (markerArrayList.size()!=0) {

            for (int i = 0; i < markerArrayList.size(); i++)
            {
                AddStop addStop=arrayList.get(i);
                Marker marker = markerArrayList.get(i);
                map.addMarker(new MarkerOptions().position(marker.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(addStop.getStop_name()));

               // Toast.makeText(getContext(), "Marker loaded", Toast.LENGTH_SHORT).show();

            }

        }
        else {

            Toast.makeText(getContext(),"no marker found",Toast.LENGTH_LONG).show();
        }*/


        /*for (int i=0;i<markerArrayList.size();i++)
        {
            MarkerLists markerLists1=markerLists.get(i);
            map.addMarker(new MarkerOptions().position(markerLists1.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(markerLists1.getStop_name()));

        }*/

        /*if (marker!=null){

            map.clear();

            for (int i=0;i<markerLists.size();i++)
            {
                MarkerLists markerLists1=markerLists.get(i);
                map.addMarker(new MarkerOptions().position(markerLists1.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(markerLists1.getStop_name()));

            }
        }*/

    }


    //for loading the map passed by the modifypage
    public  void mapByModifyPage()
    {
        Log.d("addstoparraylistgetting",arrayList.size()+"");

        Log.d("stopaddresslist2", ArryalistClasses.addressArrayList.size() + "");

        for (int i = 0; i < arrayList.size(); i++)
        {
            AddStop addStop = arrayList.get(i);

            try {

                addresses = geocoder1.getFromLocationName(addStop.getStop_name(), 1);
                String stp_name = addStop.getStop_name();

                try {

                    if (addresses != null) {
                        lat1 = addresses.get(0).getLatitude();
                        lng1 = addresses.get(0).getLongitude();


                        //for getting up the marker i have used stop addresslist arraylist in that i am storing it the value got from the addstop arraylist.
                      //  ArryalistClasses.addressArrayList.add(new Stop_Address(stp_name, lat1, lng1));


                        //for the map
                        if (map != null) {
                            marker = map.addMarker(new MarkerOptions().position(new LatLng(lat1, lng1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(stp_name));
                            markerArrayList.add(marker);
                        } else {
                            //  Toast.makeText(getContext(),"no maps found",Toast.LENGTH_LONG).show();
                        }


                    } else {

                        Toast.makeText(getContext(), "No address found", Toast.LENGTH_LONG).show();

                    }
                } catch (IndexOutOfBoundsException e) {

                    Toast.makeText(getContext(), e.getMessage() + " ", Toast.LENGTH_LONG).show();

                }
                //LatLng returnlatlng=new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());

                Log.d("returnlatlng", lat1 + " " + lng1 + "");

                Log.d("returnname", addStop.getStop_name());

                Log.d("stopaddresslist3", ArryalistClasses.addressArrayList.size() + "");


                //declaring marker and storing the current marker


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
          map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));


    }



    private AdapterView.OnItemClickListener mAutocompleteItemclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
             */

            final PlaceArrayAdapter.PlaceAutoComplete item = mPlacearrayadpater.getItem(i);
            final String placeId = String.valueOf(item.PlaceId);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */

            Log.i("LocationSelected", "Selected: " + item.Description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i("Fetchingdetails", "Fetching details for ID: " + item.PlaceId);


        }
    };


    ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {

                // Request did not complete successfully

                Log.e("ResultError", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

            // Get the Place object from the buffer.
           final Place place = places.get(0);

            // Format details of the place for display and show it in a autocomplete textview as well as fetching the origin and destiny.
            CharSequence attributions = places.getAttributions();
            names = place.getName().toString();

            //mAutoCompleteTextView.setText(place.getName());
            latLng = place.getLatLng();
            latitude = latLng.latitude;
            longitutde = latLng.longitude;

            Stop_Latlng = latitude+","+longitutde;

            Log.d("latlng lati", latLng.latitude + "");
            Log.d("latlng longi", latLng.longitude + "");

            //mPlace.setText(Html.fromHtml(place.getName() + ""));
            //mAddress.setText(Html.fromHtml(place.getAddress() + ""));

            //if the charsequence attribution is null
            if (attributions != null) {
                Toast.makeText(getContext(), "Some error while fetching", Toast.LENGTH_LONG).show();
            }

        }
    };


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Addroute", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getContext(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlacearrayadpater.setmGoogleApiClient(googleApiClient);
        Log.i("Addroute", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlacearrayadpater.setmGoogleApiClient(null);
        Log.e("ConnectionSuspended", "Google Places API connection suspended.");
    }


    //this pause method is used for the google api purpose to get the values for autocomplete
     @Override
    public void onPause() {
        super.onPause();


         //this is for loading and setting up the bottombar page item to be selected
        /* Bundle bundle=null;
         Fragment fragment=null;
         BottomBarPage bottomBarPage=(BottomBarPage)getActivity();
         bottomBarPage.loadFragments(bundle,fragment);*/

         if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage((FragmentActivity) getContext());
            googleApiClient.disconnect();
        }

        ArryalistClasses.addressArrayList.clear();

    }


    public class JParser {
        GoogleMap map;
        Context mContext;
        String distance;
        String duration;
        String start_address;
        String end_address;


        public JParser(Context mContext, GoogleMap googleMap) {
            jsonParsePojo = new ArrayList();
            this.map = googleMap;
            this.mContext = mContext;

        }

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
            JSONArray jRoutes;
            JSONArray jSteps;
            JSONArray jLegs;

            try {
                jRoutes=jObject.getJSONArray("routes");
                for (int i=0;i<jRoutes.length();i++){
                    JSONObject jsonRoute = jRoutes.getJSONObject(i);
                    JSONArray jsonLegs = jsonRoute.getJSONArray("legs");

                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    for(int m =0; m<jsonLegs.length(); m++){
                        JSONObject jsonLeg = jsonLegs.getJSONObject(m);

                        JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                        JSONObject jsonTime = jsonLeg.getJSONObject("duration");

                        distance = jsonDistance.getString("text");
                        duration = jsonTime.getString("text");
                        start_address = jsonLeg.getString("start_address");
                        end_address =jsonLeg.getString("end_address");
                        //jsonParsePojo.add(distance);
                        Log.d("Distance" , "Distance: "+distance+"\n"+"Duration: "+duration+"\n"+"Start Address: "+start_address+"\n"+"End Address: "+end_address);
                     //   Info.mapArrayList.add(new JSONParsePojo(distance , duration , start_address, end_address));
                    }

                    List path=new ArrayList();

                    for(int j=0;j<jLegs.length();j++){
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");


                        for(int k=0;k<jSteps.length();k++){
                            JSONObject obj = jSteps.getJSONObject(i);
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            for(int l=0;l<list.size();l++){
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude) );
                                hm.put("lng", Double.toString((list.get(l)).longitude) );
                                path.add(hm);

                            }
                        }
                        routes.add(path);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){

            }


            return routes;

        }
    }

    private List<LatLng> decodePoly(String polyline) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = polyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;

    }


}
