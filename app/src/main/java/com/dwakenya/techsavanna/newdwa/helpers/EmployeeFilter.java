package com.dwakenya.techsavanna.newdwa.helpers;

import android.widget.Filter;


import com.dwakenya.techsavanna.newdwa.adapters.EmployeeAdapter;
import com.dwakenya.techsavanna.newdwa.holders.Employee;

import java.util.ArrayList;

/**
 * Created by Padie on 9/1/2017.
 */

public class EmployeeFilter extends Filter {
    EmployeeAdapter adapter;
    ArrayList<Employee> filterList;
    public EmployeeFilter(ArrayList<Employee> filterList,EmployeeAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }
    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Employee> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getFirst_name().toUpperCase().contains(constraint)||
                        filterList.get(i).getPhone_cell().toUpperCase().contains(constraint)||
                        filterList.get(i).getCandidate_city().toUpperCase().contains(constraint)||
                        filterList.get(i).getLast_name().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.employees= (ArrayList<Employee>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
