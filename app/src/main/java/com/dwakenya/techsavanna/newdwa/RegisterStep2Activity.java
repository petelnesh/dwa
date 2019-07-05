package com.dwakenya.techsavanna.newdwa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RadioButton;
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
import com.dwakenya.techsavanna.newdwa.holders.OrgHolder;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterStep2Activity extends AppCompatActivity implements View.OnClickListener{

    Button proceedToImage;
    SearchableSpinner spOrg;
    RadioButton rbMale, rbFemale;
    EditText _ageText, _tribeText, _religionText, _childrenText;
    RadioButton rbMarried, rbSingle;
    private String gender ="", maritalStatus = "";

    ArrayList<OrgHolder> orgHolderArrayList;

    private String selected_org = "",  selected_org_id = "";

    private String TAG = "Step 2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration Step 2/4");

        spOrg = findViewById(R.id.sp_org);
        rbMale = (RadioButton) findViewById(R.id.rb_male);
        rbMale.setOnClickListener(this);
        rbFemale = (RadioButton) findViewById(R.id.rb_female);
        rbFemale.setOnClickListener(this);
        _ageText = (EditText) findViewById(R.id.et_age);
        rbMarried = (RadioButton) findViewById(R.id.rb_married);
        rbMarried.setOnClickListener(this);
        rbSingle = (RadioButton) findViewById(R.id.rb_single);
        rbSingle.setOnClickListener(this);
        _tribeText = (EditText) findViewById(R.id.et_tribe);

        _religionText = (EditText) findViewById(R.id.et_religion);
        _childrenText = (EditText) findViewById(R.id.et_children);



        orgHolderArrayList = new ArrayList<>();

        fillOrgs();

        spOrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_org_id = orgHolderArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        proceedToImage = (Button) findViewById(R.id.btn_proceedToPhoto);
        proceedToImage.setOnClickListener(this);
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

                Intent i= new Intent(this, RegisterActivity.class);
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

    @Override
    public void onClick(View v) {
        if (v.equals(proceedToImage)) {


            if (gender.equals("") || _ageText.getText().toString().equals("") || maritalStatus.equals("")) {


                Toast.makeText(getApplicationContext(), "Please Fill out all Fields", Toast.LENGTH_LONG).show();

            } else {

                AppConfig.preferences = getSharedPreferences(AppConfig.prefString, MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConfig.preferences.edit();
                editor.putString(AppConfig.ORGANIZATION, selected_org_id);
                editor.putString(AppConfig.GENDER, gender);
                editor.putString(AppConfig.AGE, _ageText.getText().toString());
                editor.putString(AppConfig.MARITAL_STATUS, maritalStatus);
                editor.putString(AppConfig.TRIBE, _tribeText.getText().toString());

                //editor.putString(AppConfig.RELIGION, _religionText.getText().toString());
                editor.putString(AppConfig.CHILDREN, _childrenText.getText().toString());
                editor.apply();

                Intent i = new Intent(RegisterStep2Activity.this, CandidateActivity.class);
                startActivity(i);


            }

        }

        if(v == rbMale) {
            gender = "Male";

        } else if(v == rbFemale) {
            gender = "Female";

        }

        if(v == rbMarried) {
            maritalStatus = "Married";

        } else if(v == rbSingle) {
            maritalStatus = "Single";

        }

    }


    private void fillOrgs() {
        Thread thread = new Thread() {
            Handler handler = new Handler();
            boolean error;
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fill_orgs.php", new JSONObject(params),
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
                                        JSONArray jArray = object.getJSONArray("orgs");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        List<String> list = new ArrayList<String>();
                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            OrgHolder orgHolder  = new OrgHolder(
                                                    singleRow.getString("id"), singleRow.getString("org_name"));
                                            orgHolderArrayList.add(orgHolder);
                                        }
                                        for (int i = 0; i < orgHolderArrayList.size(); i++) {
                                            list.add(orgHolderArrayList.get(i).getOrg_name());
                                            // list.add(locationList.get(i).getId());
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.select_dialog_item,list);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spOrg.setAdapter(adapter);
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



}
