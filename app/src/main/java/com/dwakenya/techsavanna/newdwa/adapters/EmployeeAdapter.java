package com.dwakenya.techsavanna.newdwa.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dwakenya.techsavanna.newdwa.DetailsActivity;
import com.dwakenya.techsavanna.newdwa.MpesaActivity;
import com.dwakenya.techsavanna.newdwa.MpesaActivityEmployee;
import com.dwakenya.techsavanna.newdwa.R;
import com.dwakenya.techsavanna.newdwa.SearchActivity;
import com.dwakenya.techsavanna.newdwa.helpers.CheckPay;
import com.dwakenya.techsavanna.newdwa.helpers.EmployeeFilter;
import com.dwakenya.techsavanna.newdwa.helpers.SQLiteHandler;
import com.dwakenya.techsavanna.newdwa.holders.CheckSubscriptions;
import com.dwakenya.techsavanna.newdwa.holders.Employee;
import com.dwakenya.techsavanna.newdwa.holders.EmployeeHolder;
import com.dwakenya.techsavanna.newdwa.interfaces.ItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Padie on 8/28/2017.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeHolder> implements Filterable {
    Context c;
    public ArrayList<Employee> employees;
    ArrayList<Employee> filterList;
    EmployeeFilter filter;
    private final String MPESA_SPREF = "MPESA_SPREF";
    SQLiteHandler db;

    //String mpesa_receipt_number, account_reference, amount, partya, result_code, confirm_date;
    //final String mpesa_receipt_number, account_reference, amount, partya, result_code, confirm_date;
    private ArrayList<String> mySubscription;
    private ArrayList<CheckSubscriptions> myData;

    String checkoutd, phoneNumbers, totalCostPrice;
    public  String phoney = "";
    public  String mpesa_receipt_number1 = "";
    public  String account_reference1 = "";
    public  String amount1 = "";
    public  String result_code1 = "";
    public  String confirm_date1 = "";



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
            public void onItemClick(final View v, final int pos) {

//                 List<CheckPay> orderAList=db.getCurrent();
//                for(CheckPay items:orderAList) {
//                    checkoutd=items.getCheckout();
//                    phoneNumbers=items.getPhonenumber();
//
//                }


                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(MPESA_SPREF, MODE_PRIVATE);
                String phonenumber = sharedPreferences.getString("phonenumber", "");
                String checkoutid = sharedPreferences.getString("checkoutid", "");
                String totalCostPrice = sharedPreferences.getString("totalCostPrice", "");





                System.out.println("phone " + phonenumber);
                System.out.println("checkout " + checkoutid);
                System.out.println("amount paid  " + totalCostPrice);

                final Intent intent = new Intent(v.getContext(), DetailsActivity.class);

                Intent detailIntent = ((Activity)v.getContext()).getIntent();

                final String usertype = detailIntent.getStringExtra("type");
                final String phoneee = detailIntent.getStringExtra("phoneee");


                final String strLastFourDi =   phoneee.length() >= 9 ? phoneee.substring(phoneee.length() - 9): "";

                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://techsavanna.net:8181/dwa/mpesaApis.php?partya=254"+strLastFourDi, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responsemsg", ">>>"+response.toString());



                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);

                        Intent detailIntent = ((Activity) v.getContext()).getIntent();

                        //String usertype = detailIntent.getStringExtra("type");
                        final String phoneee = detailIntent.getStringExtra("phoneee");
                        try {
                            Log.d("new strrr", ">>" + response);

                            myData = new ArrayList<>();

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("error").equals("false")){

                            JSONArray dataArray1 = obj.getJSONArray("data");

                            for (int i = 0; i < dataArray1.length(); i++) {
                                CheckSubscriptions checkSubscriptions = new CheckSubscriptions();
                                JSONObject dataobj1 = dataArray1.getJSONObject(i);
                                checkSubscriptions.setMpesa_receipt_number(dataobj1.getString("mpesa_receipt_number"));
                                checkSubscriptions.setAccount_reference(dataobj1.getString("account_reference"));
                                checkSubscriptions.setPartya(dataobj1.getString("partya"));
                                checkSubscriptions.setAmount(dataobj1.getString("amount"));
                                checkSubscriptions.setResult_code(dataobj1.getString("result_code"));
                                checkSubscriptions.setConfirm_date(dataobj1.getString("confirm_date"));

                                myData.add(checkSubscriptions);


                                for (int k = 0; k < myData.size(); k++) {

                                    final String mpesa_receipt_number = myData.get(k).getMpesa_receipt_number();
                                    //check type of subscription
                                    String account_reference = myData.get(k).getAccount_reference();
                                    String amount = myData.get(k).getAmount();
                                    String partya = myData.get(k).getPartya();
                                    String result_code = myData.get(k).getResult_code();

                                    //check length of subscription
                                    final String confirm_date = myData.get(k).getConfirm_date();


                                    Log.d("Message", "img " + mpesa_receipt_number + " >> " + account_reference + ">> " + amount + " >> " + partya + ">>" + result_code + ">>" + confirm_date);


                                    phoney = partya;
                                    mpesa_receipt_number1 = mpesa_receipt_number;
                                    account_reference1 = account_reference;
                                    amount1 = amount;
                                    result_code1 = result_code;
                                    confirm_date1 = confirm_date;


                                    Log.d("huhuhaaa", ">>>" + partya);
                                    System.out.println("skns " + mpesa_receipt_number);
                                    System.out.println("skns " + result_code);


                                }
                            }


                            }else if(obj.optString("error").equals("true")){
                                if (usertype.equals("0")){
                                    Toast.makeText(c, "Please Make your Payments", Toast.LENGTH_SHORT).show();
                                    Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                    c.startActivity(mpesa);
                                }
                                else if (usertype.equals("1")){
                                    Toast.makeText(c, "Please Make your Payments", Toast.LENGTH_SHORT).show();
                                    Intent mpesa = new Intent(v.getContext(), MpesaActivityEmployee.class);
                                    c.startActivity(mpesa);
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        Log.d("hihihi", ">>>" + phoney);
                        //test with 10 means employer has paid
                        //else employer has not paid


                        //Test whether the user is am employer;
                        Log.d("mydate", ">> " + confirm_date1);
                        if (usertype.equals("0")) {
                            System.out.println("mydate....>" + confirm_date1);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
                            Calendar cl = Calendar.getInstance();
                            try {
                                //Setting the date to the given date
                                cl.setTime(sdf.parse(confirm_date1));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            //Checks if user is subscribed to yearly terms
                            if (account_reference1.equals("1")) {

                                cl.add(Calendar.DAY_OF_MONTH, 365);
                                //Date after adding the days to the given date

                                //Displaying the new Date after addition of Days

                                //System.out.println("Date after Addition: "+newDate);
                                Calendar cl3 = Calendar.getInstance();
                                String newDate1 = sdf.format(cl.getTime());

                                try {
                                    cl3.setTime(sdf.parse(newDate1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                cl3.add(Calendar.DAY_OF_MONTH, -1);
                                String countdown1 = sdf.format(cl3.getTime());


                                //Displaying the new Date after addition of Days

//                                System.out.println("Date after Addition2: "+newDate1);
//                                System.out.println("Countdown: "+countdown1);


                                //means the user is active
                                if (!countdown1.equals(confirm_date1)) {

                                    //check user has paid as required
                                    if (!mpesa_receipt_number1.equals("") && phoneee.equals(phoney) && result_code1.equals("0")) {


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
                                        intent.putExtra("children", employees.get(pos).getChildren());
                                        intent.putExtra("preference_skill", employees.get(pos).getPreference_skill());


                                        intent.putExtra("image_url", employees.get(pos).getImage_url());
                                        intent.putExtra("status", employees.get(pos).getStatus());

                                        intent.putExtra("server_url", employees.get(pos).getServer_url());
                                        intent.putExtra("coordinates", employees.get(pos).getCoordinates());
                                        intent.putExtra("locname", employees.get(pos).getLocname());


                                        c.startActivity(intent);
                                    } else {


//                                        Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
//                                        c.startActivity(mpesa);


                                    }


                                    //if user is not active
                                } else {
                                    Toast.makeText(c, "Your Annual Subscription Expired on " + newDate1 + " ..PAY Again to continue Enjoying Our Services", Toast.LENGTH_SHORT).show();
                                    Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                    c.startActivity(mpesa);
                                }


                                //if user is subscribed to Monthly terms
                            } else if (account_reference1.equals("2")) {
                                cl.add(Calendar.DAY_OF_MONTH, 30);
                                //Date after adding the days to the given date
                                Calendar cl2 = Calendar.getInstance();
                                String newDate = sdf.format(cl.getTime());
                                try {
                                    cl2.setTime(sdf.parse(newDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                cl2.add(Calendar.DAY_OF_MONTH, -1);
                                String countdown = sdf.format(cl2.getTime());


                                //Displaying the new Date after addition of Days

                                System.out.println("Date after Addition2: " + newDate);
                                System.out.println("Countdown: " + countdown);

                                //checks if user is active
                                if (!countdown.equals(confirm_date1)) {

                                    //check if user has paid as required
                                    if (mpesa_receipt_number1 != null && phoneee.equals(phoney) && result_code1.equals("0")) {


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
                                        intent.putExtra("children", employees.get(pos).getChildren());
                                        intent.putExtra("preference_skill", employees.get(pos).getPreference_skill());


                                        intent.putExtra("image_url", employees.get(pos).getImage_url());
                                        intent.putExtra("status", employees.get(pos).getStatus());

                                        intent.putExtra("server_url", employees.get(pos).getServer_url());
                                        intent.putExtra("coordinates", employees.get(pos).getCoordinates());
                                        intent.putExtra("locname", employees.get(pos).getLocname());


                                        c.startActivity(intent);
                                    } else {


                                        Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                        c.startActivity(mpesa);


                                    }

                                } else {
                                    Toast.makeText(c, "Your Monthly Subscription Expired on " + newDate + " ..PAY Again to continue Enjoying Our Services", Toast.LENGTH_SHORT).show();
                                    Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                    c.startActivity(mpesa);

                                }


                            }

                        }
                        else if (usertype.equals("1")) {





                            System.out.println("mydate....>" + confirm_date1);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
                            Calendar cl = Calendar.getInstance();
                            try {
                                //Setting the date to the given date
                                cl.setTime(sdf.parse(confirm_date1));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            //Checks if user is subscribed to yearly terms
                            if (account_reference1.equals("1")) {

                                cl.add(Calendar.DAY_OF_MONTH, 365);
                                //Date after adding the days to the given date

                                //Displaying the new Date after addition of Days

                                //System.out.println("Date after Addition: "+newDate);
                                Calendar cl3 = Calendar.getInstance();
                                String newDate1 = sdf.format(cl3.getTime());

                                try {
                                    cl3.setTime(sdf.parse(newDate1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                cl3.add(Calendar.DAY_OF_MONTH, -1);

                                String countdown1 = sdf.format(cl3.getTime());


                                //Displaying the new Date after addition of Days

//                                System.out.println("Date after Addition2: "+newDate1);
//                                System.out.println("Countdown: "+countdown1);


                                //means the user is active
                                if (!countdown1.equals(confirm_date1)) {

                                    //check user has paid as required
                                    if (!mpesa_receipt_number1.equals("") && phoneee.equals(phoney) && result_code1.equals("0")) {


                                        Toast.makeText(c, "You have aleady paid for you subscription", Toast.LENGTH_SHORT).show();
                                    } else {


                                        Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                        c.startActivity(mpesa);


                                    }


                                    //if user is not active
                                } else {
                                    Toast.makeText(c, "Your Annual Subscription Expired on " + newDate1 + " ..PAY Again to continue Enjoying Our Services", Toast.LENGTH_SHORT).show();
                                    Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                    c.startActivity(mpesa);
                                }


                                //if user is subscribed to Monthly terms
                            } else if (account_reference1.equals("2")) {
                                cl.add(Calendar.DAY_OF_MONTH, 30);
                                //Date after adding the days to the given date
                                Calendar cl2 = Calendar.getInstance();
                                String newDate = sdf.format(cl.getTime());
                                try {
                                    cl2.setTime(sdf.parse(newDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                cl2.add(Calendar.DAY_OF_MONTH, -1);
                                String countdown = sdf.format(cl2.getTime());


                                //Displaying the new Date after addition of Days

                                System.out.println("Date after Addition2: " + newDate);
                                System.out.println("Countdown: " + countdown);

                                //checks if user is active
                                if (!countdown.equals(confirm_date1)) {

                                    //check if user has paid as required
                                    if (mpesa_receipt_number1 != null && phoneee.equals(phoney) && result_code1.equals("0")) {


                                        Toast.makeText(c, "You have aleady paid for you subscription", Toast.LENGTH_SHORT).show();
                                    } else {


                                        Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                        c.startActivity(mpesa);


                                    }

                                } else {
                                    Toast.makeText(c, "Your Monthly Subscription Expired on " + newDate + " ..PAY Again to continue Enjoying Our Services", Toast.LENGTH_SHORT).show();
                                    Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                    c.startActivity(mpesa);

                                }


                            }

                        }
                        }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                               System.out.println("responceErroe"+error);
//                                Toast.makeText(c, "ssssssssss "+error, Toast.LENGTH_SHORT).show();

                                Intent mpesa = new Intent(v.getContext(), MpesaActivity.class);
                                c.startActivity(mpesa);
                            }
                        });
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest1.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(c);
                requestQueue.add(stringRequest1);
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