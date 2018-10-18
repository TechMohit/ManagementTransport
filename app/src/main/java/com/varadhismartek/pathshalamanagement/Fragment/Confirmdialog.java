package com.varadhismartek.pathshalamanagement.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.varadhismartek.pathshalamanagement.Activity.BottomBarPage;
import com.varadhismartek.pathshalamanagement.Adapter.ConfirmationAdapter;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Submit;
import com.varadhismartek.pathshalamanagement.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Confirmdialog extends Fragment {

    RecyclerView recyclerView;
    Button btn_confirm;
    ArrayList<Submit> arrayList;
    ConfirmationAdapter confirmationAdapter;
    LinearLayoutManager linearLayoutManager;

    public Confirmdialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_confirmdialog,container,false);

        /*final Bundle bundle=getArguments();

        //getting the bundle from the transport fragment.
        arrayList=new ArrayList<>();
        arrayList.add(0,new Submit("Route no",bundle.getString("routeno")));
        arrayList.add(1,new Submit("Bus no",bundle.getString("busno")));
        arrayList.add(2,new Submit("Bus name",bundle.getString("busname")));
        arrayList.add(3,new Submit("Seating",bundle.getString("seating_capacity")));
        arrayList.add(4,new Submit("Starting Point",bundle.getString("starting")));
        arrayList.add(5,new Submit("Ending Point",bundle.getString("ending")));
        arrayList.add(6,new Submit("Driver name",bundle.getString("driver_name")));
        arrayList.add(7,new Submit("Assist name",bundle.getString("assist_name")));
        arrayList.add(8,new Submit("Cartaker Name",bundle.getString("caretaker_name")));
        arrayList.add(9,new Submit("Driver mobno",bundle.getString("driver_mobno")));
        arrayList.add(10,new Submit("Transport mgr name",bundle.getString("trnsprt_mgrname")));
        arrayList.add(11,new Submit("Transport mgr mobno",bundle.getString("trnsprt_mgrno")));
        arrayList.add(12,new Submit("Gps Details",bundle.getString("gps_details")));
        arrayList.add(13,new Submit("Stop Counts",bundle.getInt("stop_counts")));

        //setting up for the adapter
        confirmationAdapter=new ConfirmationAdapter(getContext(),arrayList);

        //button onclick
        btn_confirm=view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //passing intent for the bottompage
                Intent in = new Intent(getContext(), BottomBarPage.class);
                in.putExtras(bundle);

                //adding the bundle to the addroute
                Addroute addroute=new Addroute();

                //calling the bottombarpage loadfragment method
                BottomBarPage bottomBarPage = (BottomBarPage) getActivity();
                bottomBarPage.loadFragments(bundle, addroute);

            }
        });

        //setting the layoutmanager
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        //setting in the recyclerview
        recyclerView=view.findViewById(R.id.rv_confirm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confirmationAdapter);*/

        return view;

    }

}
