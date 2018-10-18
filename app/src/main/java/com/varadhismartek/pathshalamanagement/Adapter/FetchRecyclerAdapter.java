package com.varadhismartek.pathshalamanagement.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.POJO_Classes.MarkerLists;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Stop_Address;
import com.varadhismartek.pathshalamanagement.R;
import com.varadhismartek.pathshalamanagement.Utilclasses.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varadhi5 on 8/12/17.
 */

public class FetchRecyclerAdapter extends RecyclerView.Adapter<FetchRecyclerAdapter.ViewHolder> {


    Context context;
    ArrayList<Marker> markerArrayList;
    ArrayList<AddStop> addStopArrayList;
    ArrayList<Stop_Address> stop_addressArrayList;
    Marker marker;

    public FetchRecyclerAdapter(Context context, ArrayList<Marker> markerArrayList, ArrayList<AddStop> addStopArrayList)
    {
        this.context=context;
        this.markerArrayList=markerArrayList;
        this.addStopArrayList=addStopArrayList;
    }

    public FetchRecyclerAdapter(Context context, ArrayList<Marker> markerArrayList, ArrayList<AddStop> arrayList, Marker marker, GoogleMap map) {
        this.context=context;
        this.markerArrayList=markerArrayList;
        this.addStopArrayList=arrayList;
        this.marker=marker;
        this.markerArrayList=markerArrayList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final AddStop addStop=addStopArrayList.get(position);

        holder.view_stop_no.setText(String.valueOf(position+1));
        holder.view_stop_name.setText(addStop.getStop_name());
        holder.view_stop_distance.setText(addStop.getStop_distance());
        holder.view_stop_time.setText(addStop.getStop_time());
        holder.imDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());

                        builder.setTitle("Warning!!");
                        builder.setMessage("Are you Sure want to delete..?");

                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {



                                //deleting the data from the arraylist
                                addStopArrayList.remove(position);
                              //  stop_addressArrayList.remove(position);

                                marker=markerArrayList.get(position);
                                markerArrayList.remove(position);
                                marker.remove();


                                notifyDataSetChanged();
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,addStopArrayList.size());
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

    @Override
    public int getItemCount() {
        return addStopArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

    public void notifyData(ArrayList<AddStop> arrayList, ArrayList<Marker> markerArrayList, ArrayList<Stop_Address> stop_addressArrayList) {

        Log.d("DataUpdated",arrayList.size()+"");
        this.addStopArrayList=arrayList;
        this.markerArrayList=markerArrayList;
        this.stop_addressArrayList=stop_addressArrayList;
        notifyDataSetChanged();

    }
}
