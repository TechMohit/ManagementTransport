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
import java.util.HashMap;
import java.util.List;

/**
 * Created by varadhi5 on 30/11/17.
 */

public class StopviewRecyclerAdapter extends RecyclerView.Adapter<StopviewRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<AddStop> arrayList;
    ArrayList<Stop_Address> stop_addressArrayList;
    int counts;
    int pos;
    HashMap<Integer,Marker> markerHashMap;
    ArrayList<Marker> markerArrayList;

    ArrayList<MarkerLists> markerLists;

    GoogleMap googleMap;
    Marker marker;

    public StopviewRecyclerAdapter(Context context, ArrayList<AddStop> arrayList, int number_of_counts, ArrayList<Stop_Address> stop_addressList, ArrayList<Marker> markerArrayList, GoogleMap map, ArrayList<MarkerLists> markerLists, Marker marker)
    {
        this.context=context;
        this.arrayList=arrayList;
        this.counts=number_of_counts;
        this.stop_addressArrayList=stop_addressList;
        //this.markerHashMap=markerHashMap;
        this.markerArrayList=markerArrayList;
        this.googleMap=map;
        this.markerLists=markerLists;
        this.marker=marker;
    }

    public StopviewRecyclerAdapter(Context context, ArrayList<AddStop> addStopArrayList)
    {

        this.arrayList=addStopArrayList;
        this.context=context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //getting the position and fetching to the cardview
        final AddStop addStop=arrayList.get(position);

        holder.view_stop_no.setText(String.valueOf(position+1));
        holder.view_stop_name.setText(addStop.getStop_name());
        holder.view_stop_distance.setText(addStop.getStop_distance());
        holder.view_stop_time.setText(addStop.getStop_time());

        //for deleting the data
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
                        arrayList.remove(position);
                        stop_addressArrayList.remove(position);

                        markerLists.remove(position);

                        marker=markerArrayList.get(position);
                        markerArrayList.remove(position);
                        marker.remove();


                        notifyDataSetChanged();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,arrayList.size());
                        notifyItemChanged(position,markerLists.size());

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

        //for modifying the data
        Log.d("Recyclerview",addStop.getStop_number()+" "+addStop.getStop_name()+"");

        /*holder.imModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    //here i am creating the dialog for the modifying the stop name
                    final Dialog dialog=new Dialog(view.getContext());
                    //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.modify_dialog);
                    dialog.setContentView(R.layout.modifylayout);
                    dialog.setTitle("Modify Here!");
                    final EditText ed_tomodify=dialog.findViewById(R.id.modify_dialog_edittext);
                    Button btModify=dialog.findViewById(R.id.modify_button);
                    Button btcancel=dialog.findViewById(R.id.modify_cancel_btn);
                    ed_tomodify.setText(addStop.getStop_name());

                    //for modifying the text in the dialog box
                    btModify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String modify_txt=ed_tomodify.getText().toString();
                            AddStop addStop1=new AddStop(position+1,modify_txt);
                            arrayList.set(position,addStop1);
                            notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });

                    //for cancelling the dialog box
                    btcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


                }catch (Exception e){
                    Log.d("stoprecycler",e.getMessage());
                }


            }
        });*/

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void notifyData(ArrayList<AddStop> arrayList, List<Stop_Address> stop_addressList, ArrayList<Marker> markerArrayList, ArrayList<MarkerLists> markerLists) {

        Log.d("DataUpdated",arrayList.size()+"");
        this.arrayList=arrayList;
        this.markerArrayList=markerArrayList;
        this.markerLists=markerLists;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView view_stop_no,view_stop_name,view_stop_distance,view_stop_time;
        ImageView imDelete;
        //ImageView imModify;

        public ViewHolder(View itemView)
        {
            super(itemView);

            view_stop_no=itemView.findViewById(R.id.card_stop_no);
            view_stop_name=itemView.findViewById(R.id.card_stop_name);
            view_stop_distance=itemView.findViewById(R.id.card_distance);
            view_stop_time=itemView.findViewById(R.id.card_time);

           // imModify=itemView.findViewById(R.id.card_img_modify);
            imDelete=itemView.findViewById(R.id.card_img_delete);

        }


    }

}
