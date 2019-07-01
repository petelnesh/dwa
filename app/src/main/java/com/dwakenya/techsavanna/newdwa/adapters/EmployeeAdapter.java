package com.dwakenya.techsavanna.newdwa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dwakenya.techsavanna.newdwa.DetailsActivity;
import com.dwakenya.techsavanna.newdwa.R;
import com.dwakenya.techsavanna.newdwa.helpers.EmployeeFilter;
import com.dwakenya.techsavanna.newdwa.holders.Employee;
import com.dwakenya.techsavanna.newdwa.holders.EmployeeHolder;
import com.dwakenya.techsavanna.newdwa.interfaces.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Padie on 8/28/2017.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeHolder> implements Filterable {
    Context c;
    public ArrayList<Employee> employees;
    ArrayList<Employee> filterList;
    EmployeeFilter filter;
    public EmployeeAdapter(Context ctx, ArrayList<Employee> players)
    {
        this.c=ctx;
        this.employees=players;
        this.filterList=players;
    }
    @Override
    public EmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //CONVERT XML TO VIEW ONBJ
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_options_item,null);
        //HOLDER
        EmployeeHolder holder=new EmployeeHolder(v);
        return holder;
    }
    //DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(EmployeeHolder holder, int position) {
        //BIND DATA
//        holder.statusTxt.setText(employees.get(position).getStatus());

//        if (employees.get(position).getStatus().equals("0")) {
//            holder.statusTxt.setText("Available");
//        } else {
//            holder.statusTxt.setText("Occupied");
//        }

        holder.nameTxt.setText(employees.get(position).getFirst_name() + " " + employees.get(position).getLast_name());
        holder.statusTxt.setText(employees.get(position).getKey_skills());
        holder.preferenceTxt.setText(employees.get(position).getPreference_skill());
        holder.locTxt.setText(employees.get(position).getLocname());
//        holder.img.setImageResource(players.get(position).getImage_url());

        Glide.with(c).load(employees.get(position).getServer_url()+employees.get(position).getImage_url())
                .crossFade()
                .thumbnail(0.5f)
               // .bitmapTransform(new CircleTransform(c))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(holder.img);




        //IMPLEMENT CLICK LISTENET
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                Intent intent = new Intent(c, DetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

              intent.putExtra("id", employees.get(pos).getId());
                        intent.putExtra("candidate_id", employees.get(pos).getCandidate_id());
                        intent.putExtra("first_name", employees.get(pos).getFirst_name());
                        intent.putExtra("last_name", employees.get(pos).getLast_name());
                        intent.putExtra("id_no", employees.get(pos).getId_no());
                        intent.putExtra("phone_cell", employees.get(pos).getPhone_cell());
                        intent.putExtra("email1", employees.get(pos).getEmail1());
                        intent.putExtra("region_id", employees.get(pos).getRegion_id());
                        intent.putExtra("region_name", employees.get(pos).getRegion_name());
                        intent.putExtra("age", employees.get(pos).getAge());
                        intent.putExtra("gender", employees.get(pos).getGender());
                        intent.putExtra("company_id", employees.get(pos).getCompany_id());
                        intent.putExtra("company_name", employees.get(pos).getCompany_name());
                        intent.putExtra("company_phone", employees.get(pos).getCompany_phone());
                        intent.putExtra("company_city", employees.get(pos).getCompany_city());
                        intent.putExtra("company_state", employees.get(pos).getCompany_state());
                        intent.putExtra("marital_status", employees.get(pos).getMarital_status());
                        intent.putExtra("tribe", employees.get(pos).getTribe());
                        intent.putExtra("candidate_city", employees.get(pos).getCandidate_city());
                        intent.putExtra("candidate_state", employees.get(pos).getCandidate_state());
                        intent.putExtra("date_available", employees.get(pos).getDate_available());
                        intent.putExtra("can_relocate", employees.get(pos).getCan_relocate());
                        intent.putExtra("current_employer", employees.get(pos).getCurrent_employer());
                        intent.putExtra("desired_pay", employees.get(pos).getDesired_pay());
                        intent.putExtra("current_pay", employees.get(pos).getCurrent_pay());
                        intent.putExtra("best_time_to_call", employees.get(pos).getBest_time_to_call());

                intent.putExtra("length", employees.get(pos).getLength());
                intent.putExtra("reason", employees.get(pos).getReason());
                intent.putExtra("referee", employees.get(pos).getReferee());
                intent.putExtra("religion", employees.get(pos).getReligion());
                intent.putExtra("children", employees.get(pos).getChildren());
                intent.putExtra("preference_skill", employees.get(pos).getPreference_skill());



                        intent.putExtra("image_url", employees.get(pos).getImage_url());
                        intent.putExtra("status", employees.get(pos).getStatus());

                intent.putExtra("server_url", employees.get(pos).getServer_url());
                intent.putExtra("coordinates", employees.get(pos).getCoordinates());
                intent.putExtra("locname", employees.get(pos).getLocname());


                c.startActivity(intent);

//                Snackbar.make(v,players.get(pos).getName(), Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(c, players.get(pos).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return employees.size();
    }
    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new EmployeeFilter(filterList,this);
        }
        return filter;
    }


}