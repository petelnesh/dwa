package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dwakenya.techsavanna.newdwa.adapters.EmployeeAdapter;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.SessionManager;
import com.dwakenya.techsavanna.newdwa.holders.Employee;
import com.dwakenya.techsavanna.newdwa.holders.OrgHolder;
import com.dwakenya.techsavanna.newdwa.holders.RegionHolder;
import com.dwakenya.techsavanna.newdwa.holders.SpecialityHolder;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity extends AppCompatActivity {



    String regId="";

    private String TAG = "employee_search";

//    SearchView sv;

    private MaterialSearchView searchView;
    private Toolbar toolbar;

    Button tvMoreSpecificSearch;


    ArrayList<Employee> employeeArrayList;

    EmployeeAdapter adapter;

    RecyclerView rv;

    Spinner spinnerSearchBy, spinnerSelect;

    ArrayList<RegionHolder> regionHolderArrayList;
    ArrayList<OrgHolder> orgHolderArrayList;
    ArrayList<SpecialityHolder> specialityHolderArrayList;


    private String selectedCriteria = "", query = "";

    // Session Manager Class
    SessionManager session;

    HashMap<String, String> user;
    
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Employee Search");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        }

        intent = getIntent();
        final String UserType = intent.getStringExtra("type");



//        sv= (SearchView) findViewById(R.id.mSearch);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);

        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        rv= (RecyclerView) findViewById(R.id.myRecycler);

        //rv.setLayoutManager(new LinearLayoutManager(this));

        // 2. set layoutManger
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

       // int spanCount = 2; // 3 columns
       // int spacing = 5; // 50px
       // boolean includeEdge = false;
        //rv.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

//        rv.setItemAnimator(new DefaultItemAnimator());

        spinnerSearchBy = (Spinner) findViewById(R.id.sp_search_by);
        spinnerSelect = (Spinner) findViewById(R.id.sp_specified_criteria);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        // get user data from session
        user = session.getUserDetails();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
        regId= pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            updateUserToken ();

        }else {

           // Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
        }


        employeeArrayList = new ArrayList<>();
        regionHolderArrayList = new ArrayList<>();
        orgHolderArrayList = new ArrayList<>();
        specialityHolderArrayList = new ArrayList<>();


        adapter = new EmployeeAdapter(this, employeeArrayList);


        fillCategories();

        spinnerSearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCriteria = String.valueOf(position);

                if (position==0) {
                    fillRegions();

                } else if (position==1) {
                    fillSpeciality();

                } else if (position==2) {
                    fillOrganizations();

                } else {

                }




//                selectedEquipmentId = equipmentHolderArrayList.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        spinnerSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                query = String.valueOf(position+1);


                employeeArrayList.clear();
                adapter.notifyDataSetChanged();


                if (selectedCriteria.equals("0")) {
                    //searching by region

                    loadEmployeesRegion(String.valueOf(position+1), "load_employees.php");


                } else if ( selectedCriteria.equals("1")) {
                    //searching by speciality
                    loadEmployeesRegion(String.valueOf(position+1), "load_employees_speciality.php");

                } else if (selectedCriteria.equals("2")) {
                    //searching by org

                    loadEmployeesRegion(String.valueOf(position+1), "load_employees_org.php");

                } else {
                   // Toast.makeText(getApplicationContext(), "Please select search criteria", Toast.LENGTH_LONG).show();
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        //SEARCH

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
//                        .show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;

            }
        });



//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String query) {
//                //FILTER AS YOU TYPE
//                adapter.getFilter().filter(query);
//                return false;
//            }
//        });


    }
    
    
    
    public void refresh (){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);



        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
    
    
            case R.id.action_logout:
        
                session.logoutUser();
        
                break;
    
    
            case R.id.action_refresh:
        
                refresh();
        
                break;
            case R.id.action_profile:
                openProfile();
                break;
    
    
    
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void openProfile() {
        if (user.get(SessionManager.KEY_USER_TYPE).equals("1")) {
            //Employee
            Intent intent = new Intent(this, EmployeeHomeActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // finish();
        
        } else if (user.get(SessionManager.KEY_USER_TYPE).equals("0")) {
        
            Intent intent = new Intent(this, EmployeeHomeActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // finish();
        
        } else {
        
            Intent intent = new Intent(this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finish();
        
        }
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {

            // disable going back to the MainActivity
//            moveTaskToBack(true);
            finish();
        }
    }



    private void loadEmployeesRegion (final String query, final String sql){
        Thread thread = new Thread() {
            Handler handler = new Handler();
            ProgressDialog progressDialog;
            boolean error;
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(SearchActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setTitle("Loading");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                    }
                });



                HashMap<String, String> params = new HashMap<String, String>();
                params.put("query", query);
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + sql, new JSONObject(params),
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

                                            employeeArrayList.add(new Employee(
                                                    singleRow.getString("id"),
                                                    singleRow.getString("candidate_id"),
                                                    singleRow.getString("key_skills"),
                                                    singleRow.getString("first_name"),
                                                    singleRow.getString("last_name"),
                                                    singleRow.getString("id_no"),
                                                    singleRow.getString("phone_cell"),
                                                    singleRow.getString("email1"),
                                                    singleRow.getString("region_id"),
                                                    singleRow.getString("region_name"),
                                                    singleRow.getString("age"),
                                                    singleRow.getString("gender"),
                                                    singleRow.getString("company_id"),
                                                    singleRow.getString("company_name"),
                                                    singleRow.getString("company_phone"),
                                                    singleRow.getString("company_city"),
                                                    singleRow.getString("company_state"),
                                                    singleRow.getString("marital_status"),
                                                    singleRow.getString("tribe"),
                                                    singleRow.getString("candidate_city"),
                                                    singleRow.getString("candidate_state"),
                                                    singleRow.getString("date_available"),
                                                    singleRow.getString("can_relocate"),
                                                    singleRow.getString("current_employer"),
                                                    singleRow.getString("desired_pay"),
                                                    singleRow.getString("current_pay"),
                                                    singleRow.getString("best_time_to_call"),
                                                    singleRow.getString("length"),
                                                    singleRow.getString("reason"),
                                                    singleRow.getString("referee"),
                                                    singleRow.getString("religion"),
                                                    singleRow.getString("children"),
                                                    singleRow.getString("preference_skill"),
                                                    singleRow.getString("image_url"),
                                                    singleRow.getString("status"),
                                                    singleRow.getString("server_url"),
                                                    singleRow.getString("coordinates"),
                                                    singleRow.getString("locname")
                                            ));


                                        }

                                        adapter = new EmployeeAdapter(getApplicationContext(),
                                                employeeArrayList);
                                        rv.setAdapter(adapter);

                                    } else {

                                        employeeArrayList.clear();
                                        adapter.notifyDataSetChanged();
//                                        rv.setAdapter(adapter);

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "No data within specified criteria", Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getApplicationContext(), "Network Error, Please Retry.", Toast.LENGTH_LONG).show();

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

    private void fillCategories() {
        Thread thread = new Thread() {
            Handler handler = new Handler();
            boolean error;
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fill_category.php", new JSONObject(params),
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
                                        // iterate over array users as declared in php script
                                        JSONArray jArray = object.getJSONArray("categories");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        List<String> list = new ArrayList<String>();
                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);

                                            list.add(singleRow.getString("category"));

                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerSearchBy.setAdapter(adapter);
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

    private void fillRegions() {
        Thread thread = new Thread() {
            Handler handler = new Handler();
//            ProgressDialog progressDialog;
            boolean error;



            @Override
            public void run() {

//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog = new ProgressDialog(SearchActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//                        progressDialog.setMessage("Loading...");
//                        progressDialog.show();
//                    }
//                });

                final List<String> list = new ArrayList<String>();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                regionHolderArrayList.clear();
                adapter.notifyDataSetChanged();


                HashMap<String, String> params = new HashMap<String, String>();
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fill_region.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
//                                progressDialog.dismiss();
                                Log.d(TAG, " On Response: " + response.toString());
                                try {
                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());
                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {
                                        // iterate over array users as declared in php script
                                        JSONArray jArray = object.getJSONArray("regions");
                                        Log.d(TAG, "Array: " + jArray.toString());


                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            RegionHolder regionHolder = new RegionHolder(
                                                    singleRow.getString("id"), singleRow.getString("region_name"));
                                            regionHolderArrayList.add(regionHolder);
                                        }
                                        for (int i = 0; i < regionHolderArrayList.size(); i++) {
                                            list.add(regionHolderArrayList.get(i).getRegion_name());
                                            // list.add(locationList.get(i).getId());
                                        }

                                        spinnerSelect.setAdapter(adapter);
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
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
//                        progressDialog.dismiss();
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

    private void fillSpeciality() {
        Thread thread = new Thread() {
            Handler handler = new Handler();
//            ProgressDialog progressDialog;
            boolean error;
            @Override
            public void run() {

//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog = new ProgressDialog(SearchActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//                        progressDialog.setMessage("Loading...");
//                        progressDialog.show();
//                    }
//                });



                final List<String> list = new ArrayList<String>();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specialityHolderArrayList.clear();
                adapter.notifyDataSetChanged();

                HashMap<String, String> params = new HashMap<String, String>();
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fill_speciality.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
//                                progressDialog.dismiss();
                                Log.d(TAG, " On Response: " + response.toString());
                                try {
                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());
                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {
                                        // iterate over array users as declared in php script
                                        JSONArray jArray = object.getJSONArray("specialities");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            SpecialityHolder specialityHolder = new SpecialityHolder(
                                                    singleRow.getString("id"), singleRow.getString("speciality_name"));
                                            specialityHolderArrayList.add(specialityHolder);
                                        }
                                        for (int i = 0; i < specialityHolderArrayList.size(); i++) {
                                            list.add(specialityHolderArrayList.get(i).getSpeciality_name());
                                            // list.add(locationList.get(i).getId());
                                        }

                                        spinnerSelect.setAdapter(adapter);
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Equipment available", Toast.LENGTH_LONG).show();
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
//                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Network Error, Please Retry.", Toast.LENGTH_LONG).show();
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

    private void fillOrganizations (){
        Thread thread = new Thread() {
            Handler handler = new Handler();
//            ProgressDialog progressDialog;
            boolean error;
            @Override
            public void run() {

//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog = new ProgressDialog(SearchActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//                        progressDialog.setMessage("Loading...");
//                        progressDialog.show();
//                    }
//                });


                final List<String> list = new ArrayList<String>();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                orgHolderArrayList.clear();
                adapter.notifyDataSetChanged();

                HashMap<String, String> params = new HashMap<String, String>();
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fill_orgs.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
//                                progressDialog.dismiss();
                                Log.d(TAG, " On Response: " + response.toString());
                                try {
                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());
                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {
                                        // iterate over array users as declared in php script
                                        JSONArray jArray = object.getJSONArray("orgs");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        List<String> list = new ArrayList<String>();
                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            OrgHolder orgHolder = new OrgHolder(
                                                    singleRow.getString("id"), singleRow.getString("org_name"));
                                            orgHolderArrayList.add(orgHolder);
                                        }
                                        for (int i = 0; i < orgHolderArrayList.size(); i++) {
                                            list.add(orgHolderArrayList.get(i).getOrg_name());
                                            // list.add(locationList.get(i).getId());
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerSelect.setAdapter(adapter);
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Equipment available", Toast.LENGTH_LONG).show();
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
//                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Network Error, Please Retry.", Toast.LENGTH_LONG).show();

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

//    private void updateUserTokenOnServer(String user_id, String fcm_token) {
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("id", user_id)
//                .add("fcm_token", fcm_token)
//                .build();
//
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(AppConfig.SERVER_URL+"fcm_registration.php")
//                .post(body)
//                .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    private void updateUserToken () {



        Thread thread = new Thread() {

            boolean error;

            @Override
            public void run() {


                HashMap<String, String> params = new HashMap<String, String>();


                params.put("user_id", user.get(SessionManager.KEY_USER_ID));
                params.put("fcm_token", regId);


                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fcm_registration.php", new JSONObject(params),
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