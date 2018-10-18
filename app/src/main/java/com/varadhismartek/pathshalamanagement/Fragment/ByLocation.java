package com.varadhismartek.pathshalamanagement.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.varadhismartek.pathshalamanagement.Adapter.FetchRecyclerAdapter;
import com.varadhismartek.pathshalamanagement.AsyncTask.FetchUrl;
import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.POJO_Classes.MarkerLists;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Stop_Address;
import com.varadhismartek.pathshalamanagement.R;
import com.varadhismartek.pathshalamanagement.Utilclasses.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.varadhismartek.pathshalamanagement.Fragment.Transport.destiny;
import static com.varadhismartek.pathshalamanagement.Fragment.Transport.origin;

/**
 * A simple {@link Fragment} subclass.
 */
public class ByLocation extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    GoogleMap map;
    RecyclerView rv_bylocation;
    Button btn_fetch;
    SupportMapFragment supportMapFragment;
    Dialog dialog;

    Bundle bundle;
    String stop_counts;
    int stop_num;
    LocationManager locationManager;
    Location location;
    Double latitude,longitude;
    boolean isGPSEnabled = false;

    LatLng origin,dest;

    Marker marker=null;
    ArrayList<Marker> markerArrayList;
    ArrayList<Stop_Address> stop_addressArrayList;

    ArrayList<AddStop> addStopArrayList;

    //this is for the dialog box views
    EditText ed_location_name,ed_approx_dist,ed_approx_time;
    Button btn_insert;

    LatLng mylocate;
    String name;
    String same_name=null,temp_name=null;

    FetchRecyclerAdapter adapter;

    static String points_url;

    DatabaseReference databaseReference;
    DatabaseReference stops_ref;

    ArrayList<AddStop> arrayList;
    int id;
    private String Origin_Name,Dest_Name;
    private String Latlng_Stop;

    public static ByLocation newInstance(){
        ByLocation byLocation=new ByLocation();
        byLocation.getArguments();
        return byLocation;
    }

    public ByLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_by_location, container, false);

        bundle = getArguments();

        String stops=bundle.getString("routeno");
        databaseReference= FirebaseDatabase.getInstance().getReference("School/SchoolId/Transport/Routes/"+stops);
        //stops_ref=databaseReference.child(stops);

       // stops_ref=databaseReference.child("stops");

        arrayList=new ArrayList<>();
        if (bundle==null)
        {
            ByLocation.newInstance();
        }
        else if (bundle!=null){
            stop_counts=bundle.getString("stop_counts");
            stop_num=Integer.parseInt(stop_counts);
            origin=bundle.getParcelable("originlatlng");
            dest=bundle.getParcelable("destinylatlng");
            Origin_Name = bundle.getString("starting");
            Dest_Name = bundle.getString("ending");

            id=bundle.getInt("TAG");
            arrayList.addAll((ArrayList<AddStop>)getArguments().getSerializable("arraylist"));
            Log.d("bylocationarraylist",arrayList.size()+"");

        }

        supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.bylocation_map);
        supportMapFragment.getMapAsync(this);

        addStopArrayList=new ArrayList<>();

        stop_addressArrayList=new ArrayList<>();

        //creatig the marker arraylists
        markerArrayList=new ArrayList<>();

        //this for the fetching the current location
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        btn_fetch = view.findViewById(R.id.btn_fetch_location);
        btn_fetch.setOnClickListener(this);

        //setting up the recyclerview
        rv_bylocation=view.findViewById(R.id.rv_by_location);
        rv_bylocation.setHasFixedSize(true);
        rv_bylocation.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        if (arrayList.size()!=0) {
            adapter = new FetchRecyclerAdapter(getContext(), markerArrayList, arrayList,marker,map);
            rv_bylocation.setAdapter(adapter);
        }
        else {
            Toast.makeText(getContext(),"created from transport module",Toast.LENGTH_LONG).show();
        }


     /*   if (bundle.getInt("TAG")==Constants.TAG_MODIFYCHANGE)
        {
            //setting up the adapter
            adapter = new FetchRecyclerAdapter(getContext(), markerArrayList, arrayList);
            adapter.notifyData(addStopArrayList, markerArrayList, stop_addressArrayList);
            rv_bylocation.setAdapter(adapter);
        }
        else
        {
            adapter = new FetchRecyclerAdapter(getContext(), markerArrayList, addStopArrayList);
            adapter.notifyData(addStopArrayList, markerArrayList, stop_addressArrayList);
        }
*/


        return view;
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.btn_fetch_location:

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                map.setMyLocationEnabled(true);

                //if the gpsenabled is not active then toast
                if (!isGPSEnabled)
                {
                    Toast.makeText(getContext(), "Check your network settings", Toast.LENGTH_LONG).show();
                }

                else
                {

                    if (Constants.number_of_counts<stop_num)
                    {
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null)
                            {
                                Log.d("activity", "RLOC: loc by GPS");

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                mylocate=new LatLng(latitude,longitude);

                                Latlng_Stop = mylocate.toString();
                                Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses;

                                try
                                {

                                    addresses=geocoder.getFromLocation(mylocate.latitude,mylocate.longitude,1);
                                    name=addresses.get(0).getAddressLine(0);


                                    //adding the marker on the map;
                                    marker = map.addMarker(new MarkerOptions().position(mylocate).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(name));
                                    markerArrayList.add(marker);
                                    confirmdialog(name);


                                }

                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                    else
                    {

                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("Message!!");
                        builder.setMessage("Do you want to view the waypoints");
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {



                                String origin_val = "origin=" + origin.latitude + "," + origin.longitude;
                                String dest_value = "destination=" + dest.latitude + "," + dest.longitude;
                                String sensor = "sensor=false";


                                StringBuilder builder = new StringBuilder("");
                                builder.append("waypoints=");

                                Double lat, lng;

                                for (int point = 0; point < stop_addressArrayList.size(); point++) {

                                    Stop_Address stop_address = stop_addressArrayList.get(point);
                                    lat = stop_address.getLatitude();
                                    lng = stop_address.getLongitude();
                                    builder.append(lat).append(",").append(lng).append("").append("|");

                                    if (point == stop_addressArrayList.size() - 1)
                                    {
                                        builder.append(lat).append(",").append(lng);
                                    }

                                }


                                String parameters = origin_val + "&" + dest_value + "&" + sensor + "&" + builder;
                                String output = "json";
                                String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;



                                Log.d("viewstops", url);
                                points_url=url;

                                stops_ref=databaseReference.child("Routestops");
                                stops_ref.setValue(points_url);

                                stops_ref.child("stoplist").setValue(addStopArrayList);


                                //fetching the detailste of that url
                                FetchUrl fetchUrl = new FetchUrl(getContext(), map);
                                fetchUrl.execute(url);

                                map.moveCamera(CameraUpdateFactory.newLatLng(origin));
                                map.animateCamera(CameraUpdateFactory.zoomTo(11));


                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.show();


                    }



                }



                break;


            case R.id.btn_fetch_add:

                if (Constants.number_of_counts<stop_num)
                {

                    String location_name = ed_location_name.getText().toString();
                    String location_dist = ed_approx_dist.getText().toString();
                    String location_time = ed_approx_time.getText().toString();

                    if (location_name.isEmpty()) {
                        ed_location_name.setError("");
                    } else if (location_dist.isEmpty()) {
                        ed_approx_dist.setError("Enter the Approx Distance");
                    } else if (location_time.isEmpty()) {
                        ed_approx_time.setError("Enter the Approx Time");
                    } else {

                        AddStop addStop = new AddStop(String.valueOf(Constants.number_of_counts), location_name, location_dist, location_time, Origin_Name,Dest_Name,Latlng_Stop);
                        addStopArrayList.add(addStop);

                        marker = map.addMarker(new MarkerOptions().position(mylocate).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(name));
                        markerArrayList.add(marker);

                        Stop_Address stop_address = new Stop_Address(name, latitude, longitude);
                        stop_addressArrayList.add(stop_address);


                        dialog.dismiss();

                        Constants.number_of_counts += 1;

                    }

                }
                else
                {
                    Toast.makeText(getContext(),"Already enterd "+stop_counts+" stops",Toast.LENGTH_LONG).show();
                }


                break;

        }

    }


    private void confirmdialog(String name)
    {

        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.fetch_layout);

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialoganimation;
        dialog.setTitle("Insert Details");

        ed_location_name=dialog.findViewById(R.id.ed_fetch_name);
        ed_approx_dist=dialog.findViewById(R.id.ed_fetch_distance);
        ed_approx_time=dialog.findViewById(R.id.ed_fetch_time);

        ed_location_name.setText(name);
        btn_insert=dialog.findViewById(R.id.btn_fetch_add);
        btn_insert.setOnClickListener(this);

        dialog.show();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        try {
            if (origin != null && dest != null) {

                map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Start Address"));
                map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("End Address"));

                map.moveCamera(CameraUpdateFactory.newLatLng(origin));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "You have not selected the origin and destination", Toast.LENGTH_LONG).show();
        }

        Geocoder geocoder1=new Geocoder(getActivity());
        if (bundle.getInt("TAG")==Constants.TAG_MODIFYCHANGE) {

            for (int i = 0; i < arrayList.size(); i++) {
                AddStop addStop = arrayList.get(i);

                try {

                    List<Address> addresses = geocoder1.getFromLocationName(addStop.getStop_name(), 1);
                    String stp_name = addStop.getStop_name();

                    double lat1;
                    double lng1;
                    try {

                        if (addresses != null) {
                            lat1 = addresses.get(0).getLatitude();
                            lng1 = addresses.get(0).getLongitude();


                            //for getting up the marker i have used stop addresslist arraylist in that i am storing it the value got from the addstop arraylist.
                            stop_addressArrayList.add(new Stop_Address(stp_name, lat1, lng1));


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

                  //  Log.d("returnlatlng", lat1 + " " + lng1 + "");

                    Log.d("returnname", addStop.getStop_name());

                    Log.d("stopaddresslist", stop_addressArrayList.size() + "");


                    //declaring marker and storing the current marker


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            map.animateCamera(CameraUpdateFactory.zoomTo(11));



        }

        else
            {

            if (markerArrayList.size() < 1) {
                Toast.makeText(getContext(), "add the routes", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < markerArrayList.size(); i++) {
                    Marker marker = markerArrayList.get(i);
                    map.addMarker(new MarkerOptions().position(marker.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(marker.getTitle()));

                }
            }
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
