package com.varadhismartek.pathshalamanagement.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.varadhismartek.pathshalamanagement.Fragment.Confirmdialog;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Submit;
import com.varadhismartek.pathshalamanagement.R;

import java.util.ArrayList;

/**
 * Created by varadhi5 on 5/12/17.
 */

public class ConfirmationAdapter extends RecyclerView.Adapter<ConfirmationAdapter.ViewHolder> {

    ArrayList<Submit> arrayList;
    Context context;

    public ConfirmationAdapter(Context confirmdialog, ArrayList<Submit> arrayList) {
        this.context=confirmdialog;
        this.arrayList=arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_support_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Submit submit=arrayList.get(position);
        holder.tv_label.setText(submit.getLabel());
        holder.tv_details.setText(submit.getDatas());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_label,tv_details;
        public ViewHolder(View itemView) {
            super(itemView);

            tv_label=itemView.findViewById(R.id.tv_Label);
            tv_details=itemView.findViewById(R.id.tv_details);


        }
    }
}
