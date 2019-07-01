package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.GPSService;
import com.dwakenya.techsavanna.newdwa.holders.RegionHolder;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _lnameText;
    EditText _idText;
    EditText _phoneText;
    EditText _emailText;
    EditText _passwordText;
    EditText _vpasswordText;
    Button _signupEmployer;
    Button _signupEmployee;
    TextView _loginLink;
    SearchableSpinner spCounty;

    ArrayList<RegionHolder> regionHolderArrayList;

    private String selected_reg = "",  selected_region_id = "", lat = "", lon="", loc="";
    GPSService mGPSService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _nameText = findViewById(R.id.input_name);
        _lnameText = findViewById(R.id.input_lname);
        _idText = findViewById(R.id.input_id);
        _phoneText = findViewById(R.id.input_phone);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _vpasswordText = findViewById(R.id.input_vpassword);
        _signupEmployer = findViewById(R.id.btn_signupEmployer);
        _signupEmployee = findViewById(R.id.btn_signupEmployee);
        _loginLink = findViewById(R.id.link_login);

        spCounty = findViewById(R.id.sp_county_town);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Registration");

        _signupEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        // Employee signup, hold data in shared preferences to next steps
        _signupEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    AppConfig.preferences = getSharedPreferences(AppConfig.prefString, MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConfig.preferences.edit();
                    editor.putString(AppConfig.FIRST_NAME, _nameText.getText().toString());
                    editor.putString(AppConfig.LAST_NAME, _lnameText.getText().toString());
                    editor.putString(AppConfig.IDNO, _idText.getText().toString());
                    editor.putString(AppConfig.PHONE, _phoneText.getText().toString());
                    editor.putString(AppConfig.EMAIL, _emailText.getText().toString());
                    editor.putString(AppConfig.COUNTY_TOWN, selected_region_id);
                    editor.putString(AppConfig.PASSWORD, _passwordText.getText().toString());
                    editor.putString(AppConfig.COORDINATES, lat+"," + lon);
                    editor.putString(AppConfig.LOCNAME, loc);
                    editor.apply();

                    Intent i = new Intent(RegisterActivity.this, RegisterStep2Activity.class);
                    startActivity(i);
                }

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        regionHolderArrayList = new ArrayList<>();

        fillRegions();

        spCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_region_id = regionHolderArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        getGPS(RegisterActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.registration_menu, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:

                Intent i= new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

                break;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        return true;


    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Please check that required fields are not blank.", Toast.LENGTH_LONG).show();
            return;
        }



        Thread thread = new Thread() {
            ProgressDialog progressDialog;
            Handler handler = new Handler();
            boolean error;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(RegisterActivity.this,
                                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Creating Account...");
                        progressDialog.show();
                    }
                });


                HashMap<String, String> params = new HashMap<String, String>();

                // Type - 0 representing employer
                params.put("user_type", "0");
                params.put("first_name",  _nameText.getText().toString());
                params.put("last_name", _lnameText.getText().toString());
                params.put("id_no", _idText.getText().toString());
                params.put("phone_cell", _phoneText.getText().toString());
                params.put("email1", _emailText.getText().toString());
                params.put("region", selected_region_id);
                params.put("coordinates", lat+"," + lon);
                params.put("locname", loc);


                params.put("age", "");
                params.put("gender", "");
                params.put("organization", "");
                params.put("marital_status", "");
                params.put("tribe", "");
                params.put("city", "");
                params.put("state", "");
                params.put("date_available", "");
                params.put("can_relocate", "");
                params.put("current_employer", "");
                params.put("desired_pay", "");
                params.put("current_pay", "");
                params.put("best_time_to_call", "");

                params.put("password", _passwordText.getText().toString());





                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "registration.php", new JSONObject(params),
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
                                        Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();


                                    } else {
                                        Toast.makeText(getBaseContext(), "Registration failed : " + object.getString("error_msg"), Toast.LENGTH_LONG).show();
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




    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String id = _idText.getText().toString();
        String phone = _phoneText.getText().toString();
        String email = _emailText.getText().toString();
//        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String vpassword = _vpasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (lname.isEmpty() || lname.length() < 3) {
            _lnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lnameText.setError(null);
        }

        if (id.isEmpty()) {
            _idText.setError("ID No is required");
            valid = false;
        } else {
            _idText.setError(null);
        }

        if (phone.isEmpty()) {
            _phoneText.setError("Phone No is required");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 2 || password.length() > 10) {
            _passwordText.setError("between 2 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!password.equals(vpassword)) {
            _vpasswordText.setError("Passwords dint match");
            valid = false;
        } else {
            _vpasswordText.setError(null);
        }

        return valid;
    }

    private void fillRegions() {
        Thread thread = new Thread() {
            Handler handler = new Handler();
            boolean error;
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fill_region.php", new JSONObject(params),
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
                                        JSONArray jArray = object.getJSONArray("regions");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        List<String> list = new ArrayList<String>();
                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            RegionHolder regionHolder  = new RegionHolder(
                                                    singleRow.getString("id"), singleRow.getString("region_name"));
                                            regionHolderArrayList.add(regionHolder);
                                        }
                                        for (int i = 0; i < regionHolderArrayList.size(); i++) {
                                            list.add(regionHolderArrayList.get(i).getRegion_name());
                                            // list.add(locationList.get(i).getId());
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,list);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCounty.setAdapter(adapter);
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "No Regions available", Toast.LENGTH_LONG).show();
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

    private void getGPS(Context context) {

        mGPSService = new GPSService(context);
        mGPSService.getLocation();

//            availableLatLngArrayList = new ArrayList<>();

        if (!mGPSService.isLocationAvailable) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(context, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
//            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            String address = mGPSService.getLocationAddress();

            lat = String.valueOf(latitude);
            lon = String.valueOf(longitude);

            if (address.contains("IO Exception") || address.contains("Error") || address.contains("not found")) {
                loc = "";
            } else {
                loc = address;
            }

        }

    }



}
