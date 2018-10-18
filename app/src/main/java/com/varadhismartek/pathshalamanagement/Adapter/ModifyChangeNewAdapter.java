package com.varadhismartek.pathshalamanagement.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.varadhismartek.pathshalamanagement.POJO_Classes.AddStop;
import com.varadhismartek.pathshalamanagement.R;

import java.util.ArrayList;

/**
 * Created by varadhi5 on 18/12/17.
 */

public class ModifyChangeNewAdapter extends RecyclerView.Adapter<ModifyChangeNewAdapter.ViewHolder> {

    Context context;
    ArrayList<AddStop> arrayList;

    public ModifyChangeNewAdapter(Context context, ArrayList<AddStop> arrayList) {
        this.context=context;
        this.arrayList=arrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final AddStop addStop = arrayList.get(position);

        holder.view_stop_no.setText(String.valueOf(position + 1));
        holder.view_stop_name.setText(addStop.getStop_name());
        holder.view_stop_distance.setText(addStop.getStop_distance());
        holder.view_stop_time.setText(addStop.getStop_time());

        holder.imDelete.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
}
