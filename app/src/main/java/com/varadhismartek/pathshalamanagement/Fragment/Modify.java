package com.varadhismartek.pathshalamanagement.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varadhismartek.pathshalamanagement.Activity.BottomBarPage;
import com.varadhismartek.pathshalamanagement.Adapter.ModifyRouteAdapter;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Points;
import com.varadhismartek.pathshalamanagement.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Modify extends Fragment {


    RecyclerView rv_modify;

    DatabaseReference databaseReference;
    DatabaseReference key_ref;
    String[] keys;
   // String[] name;
    ArrayList<String> arrayList;
    ArrayList<Points> pointsArrayList;
    ModifyRouteAdapter modifyRouteAdapter;

    BottomBarPage bottomBarPage;


    public Modify() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_modify,container,false);

        databaseReference= FirebaseDatabase.getInstance().getReference("School/SchoolId/Transport/Routes");

        arrayList=new ArrayList<>();
        pointsArrayList=new ArrayList<>();

      //  key_ref=databaseReference.orderByKey().getRef();
       // name=new String[20];

        bottomBarPage=(BottomBarPage)getActivity();

        rv_modify=view.findViewById(R.id.rv_modifyroute);
        rv_modify.setHasFixedSize(true);
        rv_modify.setLayoutManager(new  LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        modifyRouteAdapter=new ModifyRouteAdapter(getContext(),arrayList,bottomBarPage,pointsArrayList);
        rv_modify.setAdapter(modifyRouteAdapter);


       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               int i=0;
               String starting=null,destiny=null;
               //name=new String[(int) dataSnapshot.getChildrenCount()];

               //for getting the childs under root child
               for (DataSnapshot d:dataSnapshot.getChildren()){
                   //Log.d("keys",""+d.getValue(String.class));


                   String key_names=d.getKey();
                   Log.d("key",key_names);
                    arrayList.add(i,key_names);
                    i++;

                   //for getting the
                   for (DataSnapshot dataSnapshot1:d.getChildren())
                   {
                       if (dataSnapshot1.getKey().equals("starting")){
                           starting=String.valueOf(dataSnapshot1.getValue());
                       }
                       if (dataSnapshot1.getKey().equals("destiny")){
                           destiny=String.valueOf(dataSnapshot1.getValue());
                       }

                       Log.d("values",dataSnapshot1.getKey());

                       if (dataSnapshot1.getKey().equals("Routestops"))
                       {
                           for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                           {
                                String name=dataSnapshot2.getKey();
                                Log.d("routestops",name);
                                if (dataSnapshot2.getKey().equals("stoplist"))
                                {
                                    for (DataSnapshot dataSnapshot3:dataSnapshot2.getChildren())
                                    {
                                        String stops=dataSnapshot3.getKey();
                                        Log.d("stopslist",stops);

                                        for (DataSnapshot dataSnapshot4:dataSnapshot3.getChildren())
                                        {
                                            String list=dataSnapshot4.getKey();
                                            Log.d("listss",list);
                                        }

                                    }
                                }
                           }

                       }

                   }

                   pointsArrayList.add(new Points(starting,destiny));
                   modifyRouteAdapter.notifydata(arrayList,pointsArrayList);

                 //  modifyRouteAdapter.notifyDataSetChanged();

               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

               Log.d("keyerror",databaseError.getMessage()+"");

           }
       });

       /*databaseReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/






        return view;
    }

}
