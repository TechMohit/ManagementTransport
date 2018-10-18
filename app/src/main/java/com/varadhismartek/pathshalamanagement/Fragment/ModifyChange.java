package com.varadhismartek.pathshalamanagement.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varadhismartek.pathshalamanagement.Activity.BottomBarPage;
import com.varadhismartek.pathshalamanagement.Adapter.ModifyChangeNewAdapter;
import com.varadhismartek.pathshalamanagement.Adapter.ModifyRecyclerAdapter;
import com.varadhismartek.pathshalamanagement.Adapter.ModifyRouteAdapter;
import com.varadhismartek.pathshalamanagement.Adapter.PlaceArrayAdapter;
import com.varadhismartek.pathshalamanagement.Adapter.StopviewRecyclerAdapter;
import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Submit;
import com.varadhismartek.pathshalamanagement.R;
import com.varadhismartek.pathshalamanagement.Utilclasses.ArryalistClasses;
import com.varadhismartek.pathshalamanagement.Utilclasses.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.varadhismartek.pathshalamanagement.Fragment.Transport.latLngBounds;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyChange extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    EditText ed_route_no,ed_bus_no,ed_driver_name,
            ed_asstnt_name, ed_care_taker_name,
            ed_driver_mobno,ed_trnsport_mgr_mobno,
            ed_trnsport_mgr_name,ed_gps_details,ed_stop_count,
            ed_bus_seating_capacity,ed_bus_name;

    Spinner spn_enter_type;
    Button btn_Submit;

    RecyclerView rv_stoplist;
    StopviewRecyclerAdapter recyclerAdapter;
    ModifyRecyclerAdapter modifyRecyclerAdapter;


    ArrayList<AddStop> arrayList;
    Bundle passbundle;

    static LatLng origin,destiny;
    AutoCompleteTextView actStartingpnt,actEndingpoint;
    Bundle bundle;

    Submit submit;
    ArrayAdapter<String> adapter;
    String[] types={"--select type--","Maunally","Fetch Location"};

    String route_no,bus_no,seating_cpcity,bus_name,driver_name,assistant_name,driver_mobno,transport_mgr_mobno,gps_details,care_taker,transport_mgr_name,stop_counts,starting,ending;


    public GoogleApiClient googleApiClient;
    public PlaceArrayAdapter mPlacearrayadpater;
    Double orglat,orglng,dstlat,dstlng;
    Geocoder geocoder;
    List<Address> addresses,addresses1;

    int old_counts;


    //Declaring database
    DatabaseReference mDatabaseReference;
    DatabaseReference mref,smref;


    public ModifyChange() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //getting the ids
        View view=inflater.inflate(R.layout.fragment_modify_change,container,false);

        ed_route_no=view.findViewById(R.id.mdy_routeno);
        ed_bus_no=view.findViewById(R.id.mdy_busno);
        ed_bus_seating_capacity=view.findViewById(R.id.mdy_busseating);
        ed_bus_name=view.findViewById(R.id.mdy_busname);
        ed_driver_name=view.findViewById(R.id.mdy_drivername);
        ed_driver_mobno=view.findViewById(R.id.mdy_drivermobno);
        ed_asstnt_name=view.findViewById(R.id.mdy_asstname);
        ed_care_taker_name=view.findViewById(R.id.mdy_caretakername);
        ed_trnsport_mgr_name=view.findViewById(R.id.mdy_trnsprtmgrname);
        ed_trnsport_mgr_mobno=view.findViewById(R.id.mdy_trnsprtmgrmobno);
        ed_gps_details=view.findViewById(R.id.mdy_gpsdetails);
        ed_stop_count=view.findViewById(R.id.mdy_stopcount);
        actStartingpnt=view.findViewById(R.id.mdy_startpoint);
        actEndingpoint=view.findViewById(R.id.mdy_destpoint);
        btn_Submit=view.findViewById(R.id.mdy_submit);
        rv_stoplist=view.findViewById(R.id.rv_mdy_stoplists);

        //setting up the spinner with adapter
        spn_enter_type=view.findViewById(R.id.mdy_enter_type);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,types);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spn_enter_type.setAdapter(adapter);

        //loading for the recyclerview
        rv_stoplist.setHasFixedSize(true);
        rv_stoplist.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));


     //   Log.d("modifyarraylist",arrayList.size()+"");

      //  recyclerAdapter=new StopviewRecyclerAdapter(getContext(), ModifyRouteAdapter.addStopArrayList);


        Toast.makeText(getContext(),"modify page",Toast.LENGTH_LONG).show();

        geocoder=new Geocoder(getActivity());

        //getting the bundle that passed from bottombarpage
        bundle=getArguments();

        Log.d("modifyget",bundle.toString());
        assistant_name=bundle.getString("assit_name");
        route_no=bundle.getString("routeno");
        bus_name=bundle.getString("busname");
        bus_no=bundle.getString("busno");
        care_taker=bundle.getString("caretaker_name");
        ending=bundle.getString("destiny");
        driver_mobno=bundle.getString("driver_mobno");
        driver_name=bundle.getString("driver_name");
        gps_details=bundle.getString("gps_details");
        seating_cpcity=bundle.getString("seating");
        starting=bundle.getString("starting");
        stop_counts=bundle.getString("stop_counts");
        transport_mgr_mobno=bundle.getString("trspt_mgr_mobno");
        transport_mgr_name=bundle.getString("trspt_mgr_name");



        //setting the old stop count to the constant value
        old_counts=Integer.parseInt(stop_counts);

        Log.d("originmodify",bundle.getString("starting")+" "+bundle.getString("destiny"));

        //setting the firebase till the route no
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("School/SchoolId/Transport/Routes/"+route_no);

        //setting up the text for the edittext
        ed_route_no.setText(route_no);
        ed_asstnt_name.setText(assistant_name);
        ed_bus_name.setText(bus_name);
        ed_bus_no.setText(bus_no);
        ed_care_taker_name.setText(care_taker);
        actEndingpoint.setText(ending);
        ed_driver_mobno.setText(driver_mobno);
        ed_driver_name.setText(driver_name);
        ed_gps_details.setText(gps_details);
        ed_bus_seating_capacity.setText(seating_cpcity);
        actStartingpnt.setText(starting);
        ed_stop_count.setText(stop_counts);
        ed_trnsport_mgr_mobno.setText(transport_mgr_mobno);
        ed_trnsport_mgr_name.setText(transport_mgr_name);


        //setting the foucasable and edittable false for the route edittext
        ed_route_no.setClickable(false);
        ed_route_no.setFocusable(false);
        ed_route_no.setShowSoftInputOnFocus(false);
        ed_route_no.setFocusableInTouchMode(false);

        try {

            geolocate();

        } catch (IOException e) {
            e.printStackTrace();
        }


        arrayList =  (ArrayList<AddStop>) getArguments().getSerializable("arraylist");
        //modifyRecyclerAdapter=new ModifyRecyclerAdapter(getContext(),arrayList,10);
        ModifyChangeNewAdapter modifyChangeNewAdapter=new ModifyChangeNewAdapter(getContext(),arrayList);
        rv_stoplist.setAdapter(modifyChangeNewAdapter);


        //setting for the button click;
        btn_Submit.setOnClickListener(this);

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

        //setting an adapter for the autocomplete textview
        mPlacearrayadpater=new PlaceArrayAdapter(getContext(),android.R.layout.simple_list_item_1,latLngBounds,null);
        actStartingpnt=view.findViewById(R.id.mdy_startpoint);
        actEndingpoint=view.findViewById(R.id.mdy_destpoint);

        //setting the threshold for the autocomplete textview
        actStartingpnt.setThreshold(3);
        actEndingpoint.setThreshold(3);

        //placing the adapter for the autocomplete textview
        actStartingpnt.setAdapter(mPlacearrayadpater);
        actEndingpoint.setAdapter(mPlacearrayadpater);

        Log.d("modifyarraylist1",arrayList.size()+"");

        actStartingpnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                origin=null;
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
                destiny=null;
                actEndingpoint.setOnItemClickListener(mAutocompleteItemclick);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return view;
    }

    //geolocate for to pass the start nd destination latlng to the view route module
    private void geolocate() throws IOException {


         List<Address>   addresses = geocoder.getFromLocationName(bundle.getString("starting"), 1);
         List<Address>   addresses1 = geocoder.getFromLocationName(bundle.getString("destiny"), 1);

            if (addresses!=null||addresses.size()>0||addresses1!=null||addresses1.size()>0)
            {
                try {


                    Address address=addresses.get(0);
                    Address address1=addresses1.get(0);

                  Double  orglat = address.getLatitude();
                  Double  orglng = address.getLongitude();
                  Double  dstlat = address1.getLatitude();
                  Double  dstlng = address1.getLongitude();

                    origin = new LatLng(orglat, orglng);
                    destiny = new LatLng(dstlat, dstlng);

                   /* Constants.AddrouteOrigin=new LatLng(orglat,orglng);
                    Constants.AddrouteDestiny=new LatLng(dstlat,dstlng);*/


                    Log.d("origindest",Constants.AddrouteOrigin+" "+Constants.AddrouteDestiny+"");

                    Log.d("origindest",origin+" "+destiny+"");

                }catch (IndexOutOfBoundsException e){
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(getContext(),"no address found",Toast.LENGTH_LONG).show();
            }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Modifyroute", "Google Places API connection failed with error code: "
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
            if (origin==null){
                LatLng latLng=place.getLatLng();
                origin=new LatLng(latLng.latitude,latLng.longitude);
                Log.d("ORIGINVALUE", ""+origin);
            }

            //for getting the destiny value if the origin value is not null
            else if (origin!=null){
                LatLng latLng1 = place.getLatLng();
                destiny = new LatLng(latLng1.latitude , latLng1.longitude);
                Log.d("DESTINYVALUE", ""+destiny);
            }

            if (attributions != null) {
                Toast.makeText(getContext(),"Some error while fetching",Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    public void onPause() {
        super.onPause();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage((FragmentActivity) getContext());
            googleApiClient.disconnect();
        }


    }


    @Override
    public void onClick(View view)
    {
        int id=view.getId();
        switch (id)
        {

            case R.id.mdy_submit:

                if (spn_enter_type.getSelectedItemId()==1)
                {

                    if (old_counts < Integer.parseInt(ed_stop_count.getText().toString()))
                    {
                        Toast.makeText(getContext(),"Please add the new route",Toast.LENGTH_LONG).show();

                        Constants.AddrouteOrigin=origin;
                        Constants.AddrouteDestiny=destiny;


                        stop_counts=ed_stop_count.getText().toString();
                        route_no=ed_route_no.getText().toString();

                        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("Alert!!");
                        builder.setMessage("Please Add the newly entered stop routes..");
                        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                BottomBarPage bottomBarPage = (BottomBarPage) getActivity();
                                Addroute addroute=new Addroute();

                                passbundle=new Bundle();
                                passbundle.putInt("TAG", Constants.TAG_MODIFYCHANGE);
                                passbundle.putParcelable("originlatlng",origin);
                                passbundle.putParcelable("destinylatlng",destiny);
                                passbundle.putString("stop_counts",stop_counts);
                                passbundle.putInt("stop_old_counts",old_counts);
                                passbundle.putString("routeno",route_no);

                                Log.d("origindest",origin+" "+destiny+"");

                                passbundle.putSerializable("arraylist",arrayList);

                                addroute.setArguments(passbundle);
                                bottomBarPage.loadFragments(passbundle,addroute);

                              /*  FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.bottom_container,addroute).commit();*/


                               /* arrayList.clear();*/


                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.show();



                    }
                    else if (arrayList.size()>Integer.parseInt(ed_stop_count.getText().toString())){
                        Toast.makeText(getContext(),"Stops counts are wrong",Toast.LENGTH_LONG).show();
                    }

                }

                else if (spn_enter_type.getSelectedItemId()==2)
                {


                    if (Constants.number_of_counts<Integer.parseInt(ed_stop_count.getText().toString()))
                    {
                        Toast.makeText(getContext(),"Please add the new route",Toast.LENGTH_LONG).show();


                        stop_counts=ed_stop_count.getText().toString();
                        route_no=ed_route_no.getText().toString();

                        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("Alert!!");
                        builder.setMessage("Details Saved.. Please Add the newly entered stop routes..");
                        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                BottomBarPage bottomBarPage = (BottomBarPage) getActivity();
                                ByLocation byLocation1=new ByLocation();

                                passbundle=new Bundle();
                                passbundle.putInt("TAG", Constants.TAG_MODIFYCHANGE);
                                passbundle.putParcelable("originlatlng",origin);
                                passbundle.putParcelable("destinylatlng",destiny);
                                passbundle.putString("stop_counts",stop_counts);
                                passbundle.putInt("stop_old_counts",Constants.number_of_counts);
                                passbundle.putString("routeno",route_no);

                                Log.d("origindest",origin+" "+destiny+"");

                                passbundle.putSerializable("arraylist",arrayList);

                                byLocation1.setArguments(passbundle);
                                bottomBarPage.loadFragments(passbundle,byLocation1);

                            }
                        });


                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.show();

                    }

                    else if (arrayList.size()>Integer.parseInt(ed_stop_count.getText().toString()))
                    {
                        Toast.makeText(getContext(),"Stops counts are wrong",Toast.LENGTH_LONG).show();
                    }


                }

                else {

                    //getting the main databasereference for the mref
                    mref = mDatabaseReference;

                    //setting up the value for all the childs which we modified
                    mref.child("assit_name").setValue(ed_asstnt_name.getText().toString());
                    mref.child("busname").setValue(ed_bus_name.getText().toString());
                    mref.child("busno").setValue(ed_bus_no.getText().toString());
                    mref.child("caretaker_name").setValue(ed_care_taker_name.getText().toString());
                    mref.child("destiny").setValue(actEndingpoint.getText().toString());
                    mref.child("driver_mobno").setValue(ed_driver_mobno.getText().toString());
                    mref.child("driver_name").setValue(ed_driver_name.getText().toString());
                    mref.child("gps_details").setValue(ed_gps_details.getText().toString());
                    mref.child("seating").setValue(ed_bus_seating_capacity.getText().toString());
                    mref.child("starting").setValue(actStartingpnt.getText().toString());
                    mref.child("stop_counts").setValue(ed_stop_count.getText().toString());
                    mref.child("trspt_mgr_mobno").setValue(ed_trnsport_mgr_mobno.getText().toString());
                    mref.child("trspt_mgr_name").setValue(ed_trnsport_mgr_name.getText().toString());

                    Log.d("modifymrefkey", mref.toString());

                    //Submit submit=new Submit(route_no,bus_no,bus_name,seating_cpcity,actStartingpnt.getText().toString(),actEndingpoint.getText().toString(),driver_name,driver_mobno,assistant_name,care_taker,transport_mgr_name,transport_mgr_mobno,gps_details,stop_counts);


                    Toast.makeText(getContext(), "modified succesfully", Toast.LENGTH_LONG).show();




                }

                break;

        }

    }

}
