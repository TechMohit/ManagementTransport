package com.varadhismartek.pathshalamanagement.Fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.varadhismartek.pathshalamanagement.Activity.BottomBarPage;
import com.varadhismartek.pathshalamanagement.Adapter.ConfirmationAdapter;
import com.varadhismartek.pathshalamanagement.Adapter.PlaceArrayAdapter;
import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Submit;
import com.varadhismartek.pathshalamanagement.R;
import com.varadhismartek.pathshalamanagement.Utilclasses.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Transport extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    String same_words = null;
    Button mAdd_stop_button, btn_Submit;
    EditText ed_route_no, ed_bus_no, ed_driver_name,
            ed_asstnt_name, ed_care_taker_name,
            ed_driver_mobno, ed_trnsport_mgr_mobno,
            ed_trnsport_mgr_name, ed_gps_details, ed_stop_count,
            ed_bus_seating_capacity, ed_bus_name;


    int stop_take_count = 0;
    int button_click_counts;
    static LatLng origin, destiny;
    LatLng sample, sample2;
    Double latitue, longitude = null;


    Spinner spn_type;
    ArrayAdapter<String> spn_adapter;
    String[] types = {"--Select Type to Enter Stops--", "Manually", "By Location"};

    Bundle bundle;

    String[] routes = {"1", "2", "3", "4", "5"};
    DatabaseReference mDatabaseRef;
    DatabaseReference route_ref;
    ArrayList<AddStop> arrayList;
    int key = 1;
    public static LatLngBounds latLngBounds;
    public GoogleApiClient googleApiClient;
    public PlaceArrayAdapter mPlacearrayadpater;

    String route_no, bus_no, seating_cpcity, bus_name, driver_name, assistant_name, driver_mobno, transport_mgr_mobno, gps_details, care_taker, transport_mgr_name, stop_counts;

    Dialog dialog;

    AutoCompleteTextView actStartingpnt, actEndingpoint;
    LocationManager locationManager;
    Location location;
    boolean isGPSEnabled = false;
    LatLng mylocate;
    Geocoder geocoder;

    View view;
    int value;
    // Place origin_place,destiny_place;


    public static Transport newInstance() {

        Transport transport = new Transport();
        return transport;

    }

    public Transport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_transport, container, false);


        //accessing firebase database
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("School/SchoolId/Transport/Routes");


        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //setting up the spinner for the selection types to enter the stops
        spn_type = view.findViewById(R.id.enter_type);
        spn_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, types);
        spn_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spn_type.setAdapter(spn_adapter);


        //for setting the autocomplete textview
        latLngBounds = new LatLngBounds(new LatLng(7.798000, 68.14712), new LatLng(37.090000, 97.34466));

        //setting the googleapi client for the autocomplete places
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage((FragmentActivity) getContext(), 0, this)
                .addConnectionCallbacks(this)
                .build();

        // googleApiClient.connect();

        //setting an adapter for the autocomplete textview
        mPlacearrayadpater = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, latLngBounds, null);
        actStartingpnt = view.findViewById(R.id.start_point);
        actEndingpoint = view.findViewById(R.id.destiny_point);

        //setting the threshold for the autocomplete textview
        actStartingpnt.setThreshold(3);
        actEndingpoint.setThreshold(3);

        //placing the adapter for the autocomplete textview
        actStartingpnt.setAdapter(mPlacearrayadpater);
        actEndingpoint.setAdapter(mPlacearrayadpater);

        actStartingpnt.setFocusable(true);
        actStartingpnt.setCursorVisible(false);

        geocoder=new Geocoder(getActivity());

        actStartingpnt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (actStartingpnt.getRight() - actStartingpnt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(getContext(), "clicked drawable", Toast.LENGTH_LONG).show();

                        currentlocation();


                        return true;
                    }
                }

                return false;
            }
        });


        actStartingpnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                origin = null;
                actStartingpnt.setOnItemClickListener(mAutocompleteItemclick);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        actEndingpoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                destiny = null;
                actEndingpoint.setOnItemClickListener(mAutocompleteItemclick);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //add button id
        //mAdd_stop_button=view.findViewById(R.id.add_stop_button);
        btn_Submit = view.findViewById(R.id.Submit_transport_details);
        btn_Submit.setOnClickListener(this);



        //this is for all the edittext fields id
        ed_route_no = view.findViewById(R.id.route_no);
        ed_bus_no = view.findViewById(R.id.bus_no);
        ed_bus_seating_capacity = view.findViewById(R.id.bus_seating);
        ed_bus_name = view.findViewById(R.id.bus_name);
        ed_driver_name = view.findViewById(R.id.driver_name);
        ed_driver_mobno = view.findViewById(R.id.driver_mobno);
        ed_asstnt_name = view.findViewById(R.id.assistant_name);
        ed_care_taker_name = view.findViewById(R.id.caretaker_name);
        ed_trnsport_mgr_name = view.findViewById(R.id.trnsprt_mgr_name);
        ed_trnsport_mgr_mobno = view.findViewById(R.id.trnsprt_mgr_mobno);
        ed_gps_details = view.findViewById(R.id.gps_details);
        ed_stop_count = view.findViewById(R.id.stop_count);


        //this for to put the softinputmode off
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return view;

    }

    public void currentlocation()
    {
        // isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled)
        {
            Toast.makeText(getContext(), "Check your network settings", Toast.LENGTH_LONG).show();
        }
        else {

            //THIS CODE FOR THE GPS NETWORK PROVIDER
            if (isGPSEnabled)
            {
                if (location == null)
                {

                    Log.d("activity", "RLOC: GPS Enabled");
                    if (locationManager != null)
                    {

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

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        Log.d("location&manager",location+" "+locationManager.toString());

                        if (location != null )
                        {
                            Log.d("activity", "RLOC: loc by GPS");

                           Double Fetchlatitude = location.getLatitude();
                           Double Fetchlongitude = location.getLongitude();

                            mylocate=new LatLng(Fetchlatitude,Fetchlongitude);

                            List<Address> addresses;
                            String name;
                            //geocoder=new Geocoder(getActivity(),Locale.getDefault());

                            Constants.AddrouteOrigin = mylocate;

                            try {

                                addresses=geocoder.getFromLocation(mylocate.latitude,mylocate.longitude,1);
                                //addresses.get(0).getAddressLine(0);
                                name=addresses.get(0).getAddressLine(0);

                                    actStartingpnt.setText(name);
                                    Toast.makeText(getContext(), name + " " + mylocate, Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //  map.addMarker(new MarkerOptions().position(mylocate).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(name));

                            //recyclerAdater.notifyData(arrayList, stop_addressList,markerArrayList);

                        }
                        location=null;

                        /*else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    if (location != null)
                                    {
                                        Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
                                        locationManager.removeUpdates(this);
                                    }
                                }

                                @Override
                                public void onStatusChanged(String s, int i, Bundle bundle) {

                                }

                                @Override
                                public void onProviderEnabled(String s) {

                                }

                                @Override
                                public void onProviderDisabled(String s) {

                                }
                            });
                        }*/

                    }

                }
            }

        }


    }

    //This is for confirmation dialog which comes after submitting
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cfmdialog()
    {

        RecyclerView recyclerView;
        Button btn_confirm,btn_cancel;
        ArrayList<Submit> arrayList;
        ConfirmationAdapter confirmationAdapter;
        LinearLayoutManager linearLayoutManager;

        /*Confirmdialog confirmdialog=new Confirmdialog();
        confirmdialog.setArguments(bundle);*/


        arrayList=new ArrayList<>();
        arrayList.add(0,new Submit("Route no",route_no));
        arrayList.add(1,new Submit("Bus no",bus_no));
        arrayList.add(2,new Submit("Bus name",bus_name));
        arrayList.add(3,new Submit("Seating",seating_cpcity));
        arrayList.add(4,new Submit("Starting Point",actStartingpnt.getText().toString()));
        arrayList.add(5,new Submit("Ending Point",actEndingpoint.getText().toString()));
        arrayList.add(6,new Submit("Driver name",driver_name));
        arrayList.add(7,new Submit("Assist name",assistant_name));
        arrayList.add(8,new Submit("Cartaker Name",care_taker));
        arrayList.add(9,new Submit("Driver mobno",driver_mobno));
        arrayList.add(10,new Submit("Transport mgr name",transport_mgr_name));
        arrayList.add(11,new Submit("Transport mgr mobno",transport_mgr_mobno));
        arrayList.add(12,new Submit("Gps Details",gps_details));
        arrayList.add(13,new Submit("Stop Counts",stop_counts));

        confirmationAdapter =new ConfirmationAdapter(getContext(),arrayList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.confirmlayout);


        //setting  up an animation for the dialog
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialoganimation;
        dialog.setTitle("Confirm Again..");

        //for resizing the window for the tab versiongs
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        //recyclerview
        recyclerView=dialog.findViewById(R.id.rv_confirmlayout);

        //button
        btn_confirm=dialog.findViewById(R.id.btn_confirmlayout);
        btn_cancel=dialog.findViewById(R.id.btn_cancel_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(confirmationAdapter);

        dialog.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(final View view) {
        int id=view.getId();

        switch (id)
        {

            case R.id.Submit_transport_details:

                 route_no=ed_route_no.getText().toString();
                 bus_no=ed_bus_no.getText().toString();
                 seating_cpcity=ed_bus_seating_capacity.getText().toString();
                 bus_name=ed_bus_name.getText().toString();
                 driver_name=ed_driver_name.getText().toString();
                 assistant_name=ed_asstnt_name.getText().toString();
                 care_taker=ed_care_taker_name.getText().toString();
                 driver_mobno=ed_driver_mobno.getText().toString();
                 transport_mgr_name=ed_trnsport_mgr_name.getText().toString();
                 transport_mgr_mobno=ed_trnsport_mgr_mobno.getText().toString();
                 gps_details=ed_gps_details.getText().toString();
                 stop_counts=ed_stop_count.getText().toString();

                int stop_num=0;

                try {
                    stop_num=Integer.parseInt(stop_counts);
                }catch (NumberFormatException e){
                    Toast.makeText(getContext(),"enter all the fields",Toast.LENGTH_LONG).show();
                }

                //List<AddStop> list=new ArrayList<>();
                //list.addAll(arrayList);

                if (route_no.isEmpty()){
                    ed_route_no.setError("Enter route no");
                }
                else if (bus_no.isEmpty()){
                    ed_bus_no.setError("Enter bus no");
                }
                else if (seating_cpcity.isEmpty()){
                    ed_bus_seating_capacity.setError("Enter capacity");
                }
                else if (bus_name.isEmpty()){
                    ed_bus_name.setError("Enter bus name");
                }
                else if (driver_name.isEmpty()){
                    ed_driver_name.setError("Enter driver name");
                }
                else if (driver_mobno.isEmpty()||driver_mobno.length()<=9){
                    ed_driver_mobno.setError("Enter correct mobno");
                }
                else if (assistant_name.isEmpty()){
                    ed_asstnt_name.setError("Enter assistant name");
                }
                else if (care_taker.isEmpty()){
                    ed_care_taker_name.setError("Enter caretaker name");
                }
                else if (transport_mgr_name.isEmpty()){
                    ed_trnsport_mgr_name.setError("Enter transport mngr name");
                }
                else if (transport_mgr_mobno.isEmpty()||transport_mgr_mobno.length()<=9){
                    ed_trnsport_mgr_mobno.setError("Enter correct mobno");
                }
                else if (gps_details.isEmpty()){
                    ed_gps_details.setError("Enter gps details");
                }
                else if (stop_counts.isEmpty()||stop_num<1||stop_num>6)
                {
                    ed_stop_count.setError("Count limit is 6");
                }
                else if (actStartingpnt.getText().toString().isEmpty()){
                    actStartingpnt.setError("Enter the starting point");
                }
                else if (actEndingpoint.getText().toString().isEmpty()){
                    actEndingpoint.setError("Enter the ending point");
                }
                else if (spn_type.getSelectedItemId()==0){
                    Toast.makeText(getContext(),"Select your type to insert",Toast.LENGTH_LONG).show();
                }
                else {



                    bundle=new Bundle();
                    bundle.putString("routeno",route_no);
                    bundle.putString("busno",bus_no);
                    bundle.putString("seating_capacity",seating_cpcity);
                    bundle.putString("busname",bus_name);
                    bundle.putString("starting",actStartingpnt.getText().toString());
                    bundle.putString("ending",actEndingpoint.getText().toString());
                    bundle.putString("driver_name",driver_name);
                    bundle.putString("assist_name",assistant_name);
                    bundle.putString("caretaker_name",care_taker);
                    bundle.putString("driver_mobno",driver_mobno);
                    bundle.putString("trnsprt_mgrno",transport_mgr_mobno);
                    bundle.putString("trnsprt_mgrname",transport_mgr_name);
                    bundle.putString("gps_details",gps_details);
                    bundle.putString("stop_counts",stop_counts);
                    bundle.putParcelable("originlatlng",Constants.AddrouteOrigin);
                    bundle.putParcelable("destinylatlng",Constants.AddrouteDestiny);
                    bundle.putInt("TAG", Constants.TAG_TRANSPORT);


                    Log.d("origintransport",Constants.AddrouteOrigin+" "+Constants.AddrouteDestiny);

                   /* //calling the confirmdialog fragment
                    Confirmdialog confirmdialog=new Confirmdialog();
                    confirmdialog.setArguments(bundle);*/

                    //adding the arguments in the add route class
                    Addroute addroute=new Addroute();
                    addroute.setArguments(bundle);

                    ByLocation byLocation=new ByLocation();
                    byLocation.setArguments(bundle);

                    /*FragmentTransaction ft=getFragmentManager().beginTransaction();
                    ft.replace(R.id.bottom_container,confirmdialog).commit();*/

                    cfmdialog();


                    // List<AddStop> stoplists=new ArrayList<>();
                    // stoplists.addAll(arrayList);


                    // Submit submit=new Submit(route_no,bus_no,bus_name,seating_cpcity,starting_point,destiny_point,driver_name,driver_mobno,assistant_name,care_taker,transport_mgr_name,transport_mgr_mobno,gps_details,stop_counts,stop_distance,stop_approx_time,stoplists);
                    // route_ref.child(route_no).setValue(submit);

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Redirect");
                    builder.setMessage("Proceed to add the Stops?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            Addroute addroute = new Addroute();
                            Bundle args = new Bundle();
                            Log.d("latlng", String.valueOf(origin) + "");
                            Log.d("latlng", String.valueOf(destiny) + "");

                            args.putParcelable("originlatlng", origin);
                            args.putParcelable("destinylatlng", destiny);
                            args.putInt("counts", stop_counts_pass);
                            addroute.setArguments(args);


                            Intent in = new Intent(getContext(), BottomBarPage.class);
                            in.putExtras(args);

                            BottomBarPage bottomBarPage = (BottomBarPage) getActivity();
                            bottomBarPage.loadFragments(args, addroute);

                            Log.d("arguments", args + "");


                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.show();*/


                }

                break;

            case R.id.btn_confirmlayout:

                Intent in = new Intent(getContext(), BottomBarPage.class);
                in.putExtras(bundle);

                Addroute addroute=new Addroute();
                ByLocation byLocation=new ByLocation();

                BottomBarPage bottomBarPage = (BottomBarPage) getActivity();

                //loading the fragments based upon the spinner
                if (spn_type.getSelectedItemId()==1)
                {
                    addroute.setArguments(bundle);
                    bottomBarPage.loadFragments(bundle, addroute);

                }
                else if (spn_type.getSelectedItemId()==2){
                    bottomBarPage.loadFragments(bundle, byLocation);
                    byLocation.setArguments(bundle);
                }
                else
                {

                    Toast.makeText(getContext(),"Select your entry type",Toast.LENGTH_LONG).show();
                }

                //sending the values to the firebase
                route_ref=mDatabaseRef.child(route_no);
                Submit submit=new Submit(route_no,bus_no,bus_name,seating_cpcity,actStartingpnt.getText().toString(),actEndingpoint.getText().toString(),driver_name,driver_mobno,assistant_name,care_taker,transport_mgr_name,transport_mgr_mobno,gps_details,stop_counts);
                route_ref.setValue(submit);

                dialog.dismiss();

                break;


                case R.id.btn_cancel_confirm:
                    dialog.dismiss();


        }

    }


    private AdapterView.OnItemClickListener mAutocompleteItemclick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {


                final PlaceArrayAdapter.PlaceAutoComplete item = mPlacearrayadpater.getItem(i);
                final String placeId = String.valueOf(item.PlaceId);

                Log.i("LocationSelected", "Selected: " + item.Description);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(googleApiClient, placeId);

                //write the setback call method here for loading the results we clicked

                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


                Log.i("Fetchingdetails", "Fetching details for ID: " + item.PlaceId);

        }
    };



    ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback=new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()){
                Log.e("ResultError", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            //for getting the origin value
           // if (origin==null){
            if (Constants.AddrouteOrigin==null){
                LatLng latLng=place.getLatLng();
                //origin=new LatLng(latLng.latitude,latLng.longitude);
                Constants.AddrouteOrigin=new LatLng(latLng.latitude,latLng.longitude);
                Log.d("ORIGINVALUE", ""+Constants.AddrouteOrigin);
            }

            //for getting the destiny value if the origin value is not null
          //  else if (origin!=null){
            else if (Constants.AddrouteOrigin!=null)
            {
                LatLng latLng1 = place.getLatLng();
                //destiny = new LatLng(latLng1.latitude , latLng1.longitude);
                Constants.AddrouteDestiny=new LatLng(latLng1.latitude,latLng1.longitude);
                Log.d("DESTINYVALUE", Constants.AddrouteOrigin+" "+Constants.AddrouteDestiny);
            }

            if (attributions != null) {
                Toast.makeText(getContext(),"Some error while fetching",Toast.LENGTH_LONG).show();
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

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage((FragmentActivity) getContext());
            googleApiClient.disconnect();
        }
    }



    }

