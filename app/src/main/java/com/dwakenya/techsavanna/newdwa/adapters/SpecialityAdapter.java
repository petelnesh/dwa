package com.dwakenya.techsavanna.newdwa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dwakenya.techsavanna.newdwa.R;
import com.dwakenya.techsavanna.newdwa.holders.SpecialityHolder;
import java.util.ArrayList;

/**
 * Created by Padie on 9/4/2017.
 */

public class SpecialityAdapter extends ArrayAdapter<SpecialityHolder> {

    ArrayList<SpecialityHolder> employeeList;
    LayoutInflater vi;
    int Resource;
    Context context;

    public SpecialityAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SpecialityAdapter(Context context, int resource, ArrayList<SpecialityHolder> items) {
        super(context, resource, items);
        this.context = context;
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        employeeList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.speciality_list_item, null);
        }

        SpecialityHolder p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.txt_speciality_name);
            TextView tt2 = (TextView) v.findViewById(R.id.txt_where_learnt);


            if (tt1 != null) {
                tt1.setText(p.getSpeciality_name());
            }

            if (tt2 != null) {


                    tt2.setText(p.getWhere_learnt());


            }

        }

        return v;
    }

}
