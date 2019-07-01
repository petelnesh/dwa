package com.dwakenya.techsavanna.newdwa.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwakenya.techsavanna.newdwa.R;
import com.dwakenya.techsavanna.newdwa.interfaces.ItemClickListener;


/**
 * Created by Padie on 8/28/2017.
 */

public class EmployeeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView img;
    public TextView nameTxt;
    public TextView statusTxt;
    public TextView preferenceTxt;
    public TextView locTxt;
    ItemClickListener itemClickListener;
    public EmployeeHolder(View itemView) {
        super(itemView);
        this.img= (ImageView) itemView.findViewById(R.id.employee_photo);
        this.nameTxt= (TextView) itemView.findViewById(R.id.employee_name);
        this.statusTxt= (TextView) itemView.findViewById(R.id.employee_status);
        this.preferenceTxt= (TextView) itemView.findViewById(R.id.employee_prefernce);
        this.locTxt= (TextView) itemView.findViewById(R.id.employee_loc);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}
