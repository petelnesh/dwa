package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dwakenya.techsavanna.newdwa.adapters.SpecialityAdapter;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.SessionManager;
import com.dwakenya.techsavanna.newdwa.holders.SpecialityHolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    private String TAG = DetailsActivity.class.getSimpleName();
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private ImageView imageView, icon;
    ArrayList<SpecialityHolder> specialityHolderArrayList;
    TextView tvName, tvOrganization, tvGender, tvMaritalStatus, tvAge, tvTribe, tvRegion,
            tvCompanyPhone, tvAvailableFrom, tvCanRelocate, tvCurrentEmployer, tvCurrentPay, tvDesiredPay,
    tvLengthOfLastEmp, tvNoOfChildren, tvCoordinates, tvLocName;
    ListView listViewSpecialities;
    private String user_id, status, employee_id, employee_name, employee_phone, employee_org_id, employee_org_name, employee_email, city, state;
    // Session Manager Class
    SessionManager session;
    HashMap<String, String> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("first_name"));


        // Session Manager
        session = new SessionManager(getApplicationContext());

        user = session.getUserDetails();


        imageView = (ImageView) findViewById(R.id.profile_id);

//        icon = (ImageView) findViewById(R.id.icon);

//        tvName = (TextView) findViewById(R.id.txt_name);
        tvOrganization = (TextView) findViewById(R.id.txt_org);
        tvGender = (TextView) findViewById(R.id.txt_gender);
        tvMaritalStatus = (TextView) findViewById(R.id.txt_marital_status);
        tvAge = (TextView) findViewById(R.id.txt_age);
        tvTribe = (TextView) findViewById(R.id.txt_tribe);
        tvRegion = (TextView) findViewById(R.id.txt_region);
        tvCompanyPhone = (TextView) findViewById(R.id.txt_company_contact);
        tvAvailableFrom = (TextView) findViewById(R.id.txt_available_from);
        tvCanRelocate = (TextView) findViewById(R.id.txt_can_relocate);
        tvCurrentEmployer = (TextView) findViewById(R.id.txt_current_employer);
        tvCurrentPay = (TextView) findViewById(R.id.txt_current_pay);
        tvDesiredPay = (TextView) findViewById(R.id.txt_desired_pay);

        tvLocName = (TextView) findViewById(R.id.tv_loc);
        tvCoordinates = (TextView) findViewById(R.id.tv_coordinates);

        tvLengthOfLastEmp = (TextView) findViewById(R.id.txt_length_of_last_employment);
        tvNoOfChildren = (TextView) findViewById(R.id.txt_no_of_children);

        listViewSpecialities = (ListView) findViewById(R.id.lv_specialities);


        specialityHolderArrayList = new ArrayList<>();


        // Id of candidate in sys_users table
        // Use it to query employee Token
        user_id = getIntent().getStringExtra("id");

        employee_id =getIntent().getStringExtra("candidate_id");
        employee_name=getIntent().getStringExtra("first_name") + " " + getIntent().getStringExtra("last_name");
        employee_phone=getIntent().getStringExtra("phone_cell");
        employee_org_id=getIntent().getStringExtra("company_id");
        employee_org_name=getIntent().getStringExtra("company_name");
        employee_email=getIntent().getStringExtra("email1");

        city=getIntent().getStringExtra("company_city");
        state=getIntent().getStringExtra("company_state");




//        tvName.setText(getIntent().getStringExtra("name"));
        tvOrganization.setText(getIntent().getStringExtra("company_name"));
        tvGender.setText(getIntent().getStringExtra("gender"));
        tvMaritalStatus.setText(getIntent().getStringExtra("marital_status"));
        tvAge.setText(getIntent().getStringExtra("age"));
        tvTribe.setText(getIntent().getStringExtra("tribe"));
        tvRegion.setText(getIntent().getStringExtra("region_name"));

        tvCompanyPhone.setText(getIntent().getStringExtra("company_phone"));
        tvAvailableFrom.setText(getIntent().getStringExtra("date_available"));
        String canRelocate = getIntent().getStringExtra("can_relocate");
        tvCanRelocate.setText(canRelocate.equals("0") ? "No" : "Yes");
        tvCurrentEmployer.setText(getIntent().getStringExtra("current_employer"));
        tvCurrentPay.setText(getIntent().getStringExtra("current_pay"));
        tvDesiredPay.setText(getIntent().getStringExtra("desired_pay"));

        tvLengthOfLastEmp.setText(getIntent().getStringExtra("length"));
        tvNoOfChildren.setText(getIntent().getStringExtra("children"));


        tvCoordinates.setText(getIntent().getStringExtra("coordinates"));
        tvLocName.setText(getIntent().getStringExtra("locname"));


        status = getIntent().getStringExtra("status");
//
//        mOnlineIcon = BitmapFactory.decodeResource(getResources(), R.drawable.greenstar);
//        mOfflineIcon = BitmapFactory.decodeResource(getResources(), R.drawable.redstar);
//
//        icon.setImageBitmap(status.equals("0") ? mOnlineIcon : mOfflineIcon);
//
//        if (status.equals("1")) {
//
//            tvStatus.setText("Occupied");
//        } else if (status.equals("0")) {
//            tvStatus.setText("Available");
//
//        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_request);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

               if (status.equals("1")) {
                    Snackbar.make(view, "Specified Employee already Occupied", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    builder.setTitle("Employee Request");
                    builder.setMessage("Request for "+ getIntent().getStringExtra("first_name"));

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            sendRequest();


//                            Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_LONG).show();

                            String message = user.get(SessionManager.KEY_NAME) + " made a request for you." + "\n" +
                                    "Kindly follow up with your Organization";
                            sendSinglePush(user_id, "DWA Request", message);

                        }
                    });

                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
//
                }

            }
        });


        //get the bitmap of the drawable image we are using as background
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.techsavanna);

        Glide.with(this).load(getIntent().getStringExtra("server_url")+getIntent().getStringExtra("image_url"))
//                .crossFade()
//                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(imageView);


        getEmployeeSpecialities(getIntent().getStringExtra("id"));


        //using palette, change the color of collapsing toolbar layout
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                int mutedDarkColor = palette.getDarkMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                int vibrantColor = palette.getVibrantColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                collapsingToolbarLayout.setContentScrimColor(mutedColor);
                collapsingToolbarLayout.setStatusBarScrimColor(mutedDarkColor);
//                fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
            }
        });
    }
    public void refresh (){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

            // disable going back to the MainActivity
//            moveTaskToBack(true);
            finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {


            case android.R.id.home:

                Intent i= new Intent(this, SearchActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

                break;

            case R.id.action_logout:

                session.logoutUser();

                break;


            case R.id.action_feedback:

                Intent i2= new Intent(this, FeedBackActivity.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i2);

                break;

            case R.id.action_refresh:

                refresh();

                break;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        return true;




    }
    private void getEmployeeSpecialities (final String query){
        Thread thread = new Thread() {
            Handler handler = new Handler();
            ProgressDialog progressDialog;
            boolean error;
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(DetailsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }
                });



                HashMap<String, String> params = new HashMap<String, String>();
                params.put("query", query);
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "get_employee_specialities.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                progressDialog.dismiss();
                                Log.d(TAG, " On Response: " + response.toString());
                                try {
                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());
                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {
                                        // iterate over array users as declared in php script
                                        JSONArray jArray = object.getJSONArray("employees");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        List<String> list = new ArrayList<String>();
                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);

                                            specialityHolderArrayList.add(new SpecialityHolder(
                                                    singleRow.getString("id"),
                                                    singleRow.getString("speciality_name"),
                                                    singleRow.getString("where_learnt")
                                            ));

                                        }

                                        SpecialityAdapter adapter = new SpecialityAdapter(getApplicationContext(),R.layout.speciality_list_item,
                                                specialityHolderArrayList);
                                        listViewSpecialities.setAdapter(adapter);
                                        setListViewHeightBasedOnChildren(listViewSpecialities);

                                    } else {

//                                        rv.setAdapter(adapter);

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Employee has no Specialities added", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }

                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if (errorRes != null && errorRes.data != null) {
                            try {
                                stringData = new String(errorRes.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error", stringData);

                        Log.e(TAG, "On ErrorResponse: " + error.getMessage());
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=utf-8");

                        return headers;
                    }

                };

                // add the request object to the queue to be executed
                AppController.getInstance().addToRequestQueue(req);
            }

        };
        thread.start();
    }
    public void sendRequest() {
        Log.d(TAG, "request");


        Thread thread = new Thread() {
            ProgressDialog progressDialog;
            Handler handler = new Handler();
            boolean error;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(DetailsActivity.this,
                                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Sending Request...");
                        progressDialog.show();
                    }
                });


                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String date_time_in = date_formatter.format(today);


                HashMap<String, String> params = new HashMap<String, String>();


                params.put("title", "Employer Contact : " + user.get(SessionManager.KEY_PHONE));
                params.put("city", city);
                params.put("state", state);

                params.put("employer_id", user.get(SessionManager.KEY_USER_ID));
                params.put("employer_phone", user.get(SessionManager.KEY_PHONE));
                params.put("employer_name", user.get(SessionManager.KEY_NAME));
                params.put("employee_org_id", employee_org_id);
                params.put("employee_org_name", employee_org_name);
                params.put("employee_id", employee_id);
                params.put("employee_name", employee_name);
                params.put("employee_phone", employee_phone);
                params.put("employee_email", employee_email);
                params.put("request_date", date_time_in);
                params.put("request_status", "0");



                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "request.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                progressDialog.dismiss();
                                Log.d(TAG, " On Response: " + response.toString());
                                try {

                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());

                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {
                                        String message = user.get(SessionManager.KEY_NAME) + " made a request for you." + "\n" +
                                                "Kindly follow up with your Organization";
                                        sendSinglePush(user_id, "Request for You", message);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                                        builder.setTitle("Success");
                                        builder.setMessage("Please Follow up with the Company");

                                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(DetailsActivity.this,SearchActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                        AlertDialog alert = builder.create();
                                        alert.show();




                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                                        builder.setTitle("Request Error");
                                        builder.setMessage(object.getString("error_msg"));

                                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        AlertDialog alert = builder.create();
                                        alert.show();


                                      }
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        NetworkResponse errorRes = error.networkResponse;

                        Toast.makeText(getApplicationContext(), "Network Error, Please Retry.", Toast.LENGTH_LONG).show();

                        String stringData = "";
                        if (errorRes != null && errorRes.data != null) {
                            try {
                                stringData = new String(errorRes.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error", stringData);

                        Log.e(TAG, "On ErrorResponse: " + error.getMessage());
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=utf-8");

                        return headers;
                    }

                };

                // add the request object to the queue to be executed
                AppController.getInstance().addToRequestQueue(req);
            }

        };
        thread.start();
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;

        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        desiredWidth, CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    public void sendSinglePush(final String user_id, final String title, final String msg) {



//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.SERVER_URL + "single_push.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("Single Push: ", response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Log.d("Single Push: ", error.toString());
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("user_id", title);
//                params.put("title", title);
//                params.put("message", msg);
//
//                return params;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(stringRequest);






        Thread thread = new Thread() {

            boolean error;

            @Override
            public void run() {


                HashMap<String, String> params = new HashMap<String, String>();


                params.put("user_id", user_id);
                params.put("title", title);
                params.put("message", msg);


                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "single_push.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, " On Response: " + response.toString());
                                try {

                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());

                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {

                                    } else {

                                    }
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;

                        Toast.makeText(getApplicationContext(), "Single Push : Network Error, Please Retry.", Toast.LENGTH_LONG).show();

                        String stringData = "";
                        if (errorRes != null && errorRes.data != null) {
                            try {
                                stringData = new String(errorRes.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error", stringData);

                        Log.e(TAG, "On ErrorResponse: " + error.getMessage());
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=utf-8");

                        return headers;
                    }

                };

                // add the request object to the queue to be executed
                AppController.getInstance().addToRequestQueue(req);
            }

        };
        thread.start();
    }
}
