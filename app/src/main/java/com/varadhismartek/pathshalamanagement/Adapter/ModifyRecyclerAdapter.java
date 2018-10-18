package com.varadhismartek.pathshalamanagement.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Stop_Address;
import com.varadhismartek.pathshalamanagement.R;
import com.varadhismartek.pathshalamanagement.Utilclasses.ArryalistClasses;
import com.varadhismartek.pathshalamanagement.Utilclasses.Constants;

import java.util.ArrayList;

/**
 * Created by varadhi5 on 12/12/17.
 */

public class ModifyRecyclerAdapter extends RecyclerView.Adapter<ModifyRecyclerAdapter.ViewHolder> {

    ArrayList<AddStop> addStopArrayList;
    Context context;
    ArrayList<Marker> markerArrayList;
    int val;
    Marker marker;
    ArrayList<Stop_Address> stop_addressArrayList;
    GoogleMap googleMap;

    public ModifyRecyclerAdapter(Context context, ArrayList<AddStop> addStopArrayList, int num, Marker marker, ArrayList<Stop_Address> stop_addressList)
    {
        this.context=context;
        this.addStopArrayList=addStopArrayList;
        this.val=num;

    }


    public ModifyRecyclerAdapter(Context context, ArrayList<AddStop> arrayList, int i, Marker marker, ArrayList<Stop_Address> stop_addressList, GoogleMap map, ArrayList<Marker> markerArrayList) {

        this.context=context;
        this.addStopArrayList=arrayList;
        this.val=i;
        this.marker=marker;
        this.stop_addressArrayList=stop_addressList;
        this.googleMap=map;
        this.markerArrayList=markerArrayList;


    }

    @Override
    public ModifyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ModifyRecyclerAdapter.ViewHolder holder, final int position) {

       if (val==10)
       {
           final AddStop addStop = addStopArrayList.get(position);

           holder.view_stop_no.setText(String.valueOf(position + 1));
           holder.view_stop_name.setText(addStop.getStop_name());
           holder.view_stop_distance.setText(addStop.getStop_distance());
           holder.view_stop_time.setText(addStop.getStop_time());

           holder.imDelete.setVisibility(View.GONE);

       }

        else if (val==20)
        {

                final AddStop addStop1=addStopArrayList.get(position);

                holder.view_stop_no.setText(String.valueOf(position+1));
                holder.view_stop_name.setText(addStop1.getStop_name());
                holder.view_stop_distance.setText(addStop1.getStop_distance());
                holder.view_stop_time.setText(addStop1.getStop_time());
                holder.imDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());

                        builder.setTitle("Warning!!");
                        builder.setMessage("Are you Sure want to delete..?");

                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                //deleting the data from the arraylist
                                addStopArrayList.remove(position);
                             //   stop_addressArrayList.remove(position);

                                try {
                                    //for getting the marker and performing delete options.
                                    marker=markerArrayList.get(position);
                                    markerArrayList.remove(position);
                                    marker.remove();
                                }catch (IndexOutOfBoundsException e){
                                    Toast.makeText(view.getContext(),"place not added",Toast.LENGTH_LONG).show();

                                }

                                //for removing the data from the arraylist in the stopaddress arraylist to create a new waypoints.



                               // ModifyRouteAdapter.addStopArrayList.remove(position);
                                notifyDataSetChanged();
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,addStopArrayList.size());
                              //  notifyItemChanged(position,ArryalistClasses.addressArrayList.size());


                                Constants.number_of_counts-=1;

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.show();

                    }
                });
        }




    }

    @Override
    public int getItemCount() {
        return addStopArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView view_stop_no,view_stop_name,view_stop_distance,view_stop_time;
        ImageView imDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            view_stop_no=itemView.findViewById(R.id.card_stop_no);
            view_stop_name=itemView.findViewById(R.id.card_stop_name);
            view_stop_distance=itemView.findViewById(R.id.card_distance);
            view_stop_time=itemView.findViewById(R.id.card_time);
            imDelete=itemView.findViewById(R.id.card_img_delete);


        }
    }

    public void notifydata(ArrayList<AddStop> addStops, ArrayList<Marker> markerArrayList,ArrayList<Stop_Address> stop_addresses){
        this.addStopArrayList=addStops;
        this.markerArrayList=markerArrayList;
        ArryalistClasses.addressArrayList=stop_addresses;

        notifyDataSetChanged();
    }
}
