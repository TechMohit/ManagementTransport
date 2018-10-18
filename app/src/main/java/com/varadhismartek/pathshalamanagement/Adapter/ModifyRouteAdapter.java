package com.varadhismartek.pathshalamanagement.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varadhismartek.pathshalamanagement.Activity.BottomBarPage;
import com.varadhismartek.pathshalamanagement.Fragment.Modify;
import com.varadhismartek.pathshalamanagement.Fragment.ModifyChange;
import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Points;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Submit;
import com.varadhismartek.pathshalamanagement.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by varadhi5 on 8/12/17.
 */

public class ModifyRouteAdapter extends RecyclerView.Adapter<ModifyRouteAdapter.ViewHolder>  {

    Context context;
    String[] key;
    ArrayList<String> arrayList;
    DatabaseReference databaseReference;
    DatabaseReference mRef;
    DatabaseReference childref;

    Submit submit;
    BottomBarPage bottomBarPage;

    ArrayList<Submit> submitArrayList;
    String routes;
    Bundle bundle;
    ModifyChange modifyChange;
    AddStop addStop;
    public static ArrayList<AddStop> addStopArrayList;
    String name,dist,time,no;
    ArrayList<String> stopslists;
    int i=0;
    ArrayList<Points> pointsArrayList;

    Dialog dialog;



    public ModifyRouteAdapter(Context context, ArrayList<String> keys, BottomBarPage bottomBarPage, ArrayList<Points> pointsArrayList)
    {


        this.bottomBarPage =bottomBarPage;

        this.context=context;
        //this.key=keys;
        this.arrayList=keys;

        submitArrayList=new ArrayList<>();

        this.pointsArrayList=pointsArrayList;


    }

    @Override
    public ModifyRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.modify_cardlayout,parent,false);

        databaseReference= FirebaseDatabase.getInstance().getReference("School/SchoolId/Transport/Routes");
        //mRef=databaseReference.child();



        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ModifyRouteAdapter.ViewHolder holder, final int position)
    {

        Points points=pointsArrayList.get(position);
        holder.tv_start.setText(points.getStart());
        holder.tv_destiny.setText(points.getDestiny());

        //setting the data for the bundle and passing to the modifychange fragment
        submit=new Submit();
        bundle=new Bundle();
        modifyChange = new ModifyChange();
        addStop=new AddStop();
        addStopArrayList=new ArrayList<>();
        stopslists=new ArrayList<>();

        mRef=databaseReference.child(arrayList.get(position));

        Log.d("mref",mRef.toString());
        holder.tv_routes.setText(arrayList.get(position));

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mRef=databaseReference.child(arrayList.get(position));
                Log.d("mreftodelete",mRef.toString());

                dialog=new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.getWindow().getAttributes().windowAnimations=R.style.dialoganimation;
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                Button button=dialog.findViewById(R.id.custom_proceed);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mRef.removeValue();
                        notifyDataSetChanged();

                        BottomBarPage bottomBarPage=((BottomBarPage)context);
                        bottomBarPage.pushFragment(new Modify());

                        dialog.dismiss();

                    }
                });

                dialog.show();

                return true;
            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //calling the database reference again to call the particular database
                mRef=databaseReference.child(arrayList.get(position));

                mRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d("whichkey",mRef.toString());

                        if (dataSnapshot.exists()){

                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                if (snapshot.getKey().equals("Routestops"))
                                {
                                    for (DataSnapshot dataSnapshot1:snapshot.getChildren())
                                    {
                                        Log.d("routestops1",dataSnapshot1.getKey());

                                        
                                        if (dataSnapshot1.getKey().equals("stoplist"))
                                        {

                                            for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                                            {
                                                Log.d("routestops2",dataSnapshot2.getKey());

                                                for (DataSnapshot dataSnapshot3:dataSnapshot2.getChildren())
                                                {

                                                    Log.d("routestops3",dataSnapshot3.getKey());

                                                    if (dataSnapshot3.getKey().equals("stop_distance"))
                                                    {
                                                        dist=String.valueOf(dataSnapshot3.getValue());
                                                    }
                                                    if (dataSnapshot3.getKey().equals("stop_time"))
                                                    {
                                                        time=String.valueOf(dataSnapshot3.getValue());
                                                    }
                                                    if (dataSnapshot3.getKey().equals("stop_number"))
                                                    {
                                                        no=String.valueOf(dataSnapshot3.getValue());
                                                    }
                                                    if (dataSnapshot3.getKey().equals("stop_name"))
                                                    {
                                                        name=String.valueOf(dataSnapshot3.getValue());
                                                    }

                                                }

                                              //  addStopArrayList.add(new AddStop(no,name,dist,time));

                                                /*for (DataSnapshot dataSnapshot3:dataSnapshot2.getChildren())
                                                {
                                                    Log.d("routestops3",dataSnapshot3.getKey());




                                                    addStopArrayList.add(new AddStop(no,name,dist,time));

                                                    Log.d("routestops3",addStopArrayList.size()+"");

                                                }*/

                                            }

                                            /*mRef=databaseReference.child(arrayList.get(position));
                                            mRef.child("Routestops").child("stoplist").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot ds:dataSnapshot.getChildren())
                                                    {
                                                        Log.d("ds",ds.getKey());
                                                            for (DataSnapshot dataSnapshot2:ds.getChildren())
                                                            {
                                                                Log.d("ds2",dataSnapshot2.getKey());
                                                            }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });*/





                                        }

                                    }
                                }

                                if (snapshot.getKey().equals("routeno"))
                                {
                                    submit.setRouteno(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("busname"))
                                {
                                    submit.setBusname(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("busno"))
                                {
                                    submit.setBusno(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("caretaker_name"))
                                {
                                    submit.setCaretaker_name(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("destiny"))
                                {
                                    submit.setDestiny(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("driver_mobno"))
                                {
                                    submit.setDriver_mobno(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("driver_name"))
                                {
                                    submit.setDriver_name(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("gps_details"))
                                {
                                    submit.setGps_details(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("seating"))
                                {
                                    submit.setSeating(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("starting"))
                                {
                                    submit.setStarting(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("stop_counts"))
                                {
                                    submit.setStop_counts(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("trspt_mgr_mobno"))
                                {
                                    submit.setTrspt_mgr_mobno(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("trspt_mgr_name"))
                                {
                                    submit.setTrspt_mgr_name(String.valueOf(snapshot.getValue()));
                                }

                                if (snapshot.getKey().equals("assit_name"))
                                {
                                    submit.setAssit_name(String.valueOf(snapshot.getValue()));
                                }

                            }

                            bundle.putString("assit_name",submit.getAssit_name());
                            bundle.putString("busname",submit.getBusname());
                            bundle.putString("busno",submit.getBusno());
                            bundle.putString("caretaker_name",submit.getCaretaker_name());
                            bundle.putString("destiny",submit.getDestiny());
                            bundle.putString("driver_mobno",submit.getDriver_mobno());
                            bundle.putString("driver_name",submit.getDriver_name());
                            bundle.putString("gps_details",submit.getGps_details());
                            bundle.putString("routeno",submit.getRouteno());
                            bundle.putString("seating",submit.getSeating());
                            bundle.putString("starting",submit.getStarting());
                            bundle.putString("stop_counts",submit.getStop_counts());
                            bundle.putString("trspt_mgr_mobno",submit.getTrspt_mgr_mobno());
                            bundle.putString("trspt_mgr_name",submit.getTrspt_mgr_name());
                            bundle.putSerializable("arraylist",addStopArrayList);


                            Log.d("bundlesmodify",bundle.toString());

                            modifyChange.setArguments(bundle);


                            /*bottomBarPage.loadModify(bundle,modifyChange);*/

                            FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.bottom_container,modifyChange).commit();

                            Log.d("array",submit.getRouteno());


                        }
                        else {
                            Toast.makeText(context,"datasnapshot is not exists",Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                //Toast.makeText(context.getApplicationContext(),dist,Toast.LENGTH_LONG).show();



            }
        });

       // Log.d("nodes",mRef.getKey());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void notifydata(ArrayList<String> arrayList, ArrayList<Points> pointsArrayList) {
        this.arrayList=arrayList;
        this.pointsArrayList=pointsArrayList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_routes,tv_start,tv_destiny;
        CardView cardView;

        public ViewHolder(View itemView) {

            super(itemView);
            tv_routes=itemView.findViewById(R.id.tv_card_modify);
            tv_start=itemView.findViewById(R.id.tv_card_start);
            tv_destiny=itemView.findViewById(R.id.tv_card_destiny);
            cardView=itemView.findViewById(R.id.cv_modifychange);

        }
    }
}
