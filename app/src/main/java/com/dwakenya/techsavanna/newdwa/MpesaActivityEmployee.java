package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.SSLCertificateSocketFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.SQLiteHandler;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MpesaActivityEmployee extends AppCompatActivity {
    private static final String TAG = MpesaActivityEmployee.class.getSimpleName();
    private final String MPESA_SPREF = "MPESA_SPREF";
    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.editTextAmount)
    EditText editTextAmount;

    @BindView(R.id.sendButton)
    Button sendButton;
    String checkout,checkoutd;
    @BindView(R.id.totalamount)
    TextView totalamount;
    //Declare Daraja :: Global Variable

    //private double totalCostPrice;

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private  static final String urlAdress="https://techsavanna.net:3000/DomesticMpesa/webresources/domesticmpesa/mpesa/push";

    private static final String SERVER_PATH = "Path_to_Server_To_Store_Token";

    String phoneNumber,totalCostPrice;
    SQLiteHandler db;
    boolean error;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Make Payement");
        myToolbar.setTitleTextColor(Color.WHITE);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        //totalCostPrice = intent.getDoubleExtra("money",0);

        //totalamount.setText("Total Amount: "+totalCostPrice);
        db=new SQLiteHandler(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Phone Number from User Input
                phoneNumber = editTextPhoneNumber.getText().toString().trim();
                totalCostPrice = editTextAmount.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    editTextPhoneNumber.setError("Please Provide a Phone Number");
                    return;
                }
                if(TextUtils.isEmpty(totalCostPrice)){
                    editTextAmount.setError("Please Enter Amount");
                }
                sendPost();


            }
        });
//        continuebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              //  phoneNumber = editTextPhoneNumber.getText().toString().trim();
//                List<CheckPay> orderAList=db.getCurrent();
//                for(CheckPay item:orderAList) {
//                    checkoutd=item.getCheckout();
//                    phoneNumbers=item.getPhonenumber();
//                }
//                System.out.println("phoneNumbers"+phoneNumber);
//                getResponse(phoneNumbers,checkoutd);
//            }
//        });
    }

    public void getResponse(String phoneNumber,  String checkout,String totalCostPrice) {

        System.out.println("Phonenumber"+phoneNumber);
        System.out.println("Phonenumber"+checkout);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNumber);
        params.put("checkout", checkout);
        params.put("totalCostPrice", totalCostPrice);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.GET_URL,  new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(final JSONObject response) {
                        // display response
                        Log.d("ooo Response", response.toString());
//
                        try {
                            // Parse
                            JSONArray jArray = response.getJSONArray("post");
                            JSONObject er = jArray.getJSONObject(0);
                            error = er.getBoolean("error");

                            if (!error) {
                                final JSONObject user = jArray.getJSONObject(1);
                                final JSONObject user1 = user.getJSONObject("user");
                                String uid = user1.getString("id");
                                String name = user1.getString("mpesa_receipt_number");
                                int amount=user1.getInt("amount");
                                System.out.println("Amountt"+amount);
                                if(name != null  && amount==2 ){
                                    Intent net=new Intent(MpesaActivityEmployee.this,SearchActivity.class);
                                    startActivity(net);
                                    finish();
                                }else if(name != null  && amount==1 ){
                                    Intent net=new Intent(MpesaActivityEmployee.this,EmployeeHomeActivity.class);
                                    startActivity(net);
                                    finish();
                                }
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

// add it to the RequestQueue
        AppController.getInstance().addToRequestQueue(getRequest);

    }

    public void sendPost() {
        //totalamount.setText("Total Amount: "+totalCostPrice);
        progressDialog = new ProgressDialog(MpesaActivityEmployee.this);
        progressDialog.setTitle("Requesting Mpesa");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    if (conn instanceof HttpsURLConnection) {
                        HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                        httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                        httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
                    }
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    String strLastFourDi = phoneNumber.length() >= 9 ? phoneNumber.substring(phoneNumber.length() - 9): "";
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("phone", "254"+strLastFourDi);
                    jsonParam.put("amount", totalCostPrice);
                    jsonParam.put("accountReference", "Centre for Domestic Training");
                    jsonParam.put("transactionDesc", "Subscription");

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Output from Server .... \n");
                    //   System.out.println("jsonnn"+br.readLine());

                    // String json = null;

                    //System.out.println("CheckoutRequestID :"+ br.readLine());
                    try {
                        JSONObject ns = new JSONObject(br.readLine());
                        checkout=ns.getString("CheckoutRequestID");
                        System.out.println("CheckoutRequestID ns"+ checkout);
                        System.out.println("Phonenumber ns"+ phoneNumber);
                        System.out.println("totalCostPrice ns"+ totalCostPrice);
                        //int score = (int)totalCostPrice;
                        SharedPreferences.Editor sPEditor = getSharedPreferences(MPESA_SPREF, MODE_PRIVATE).edit();
                        //SharedPreferences.Editor sPEditor = getSharedPreferences()
                        sPEditor.putString("phonenumber", phoneNumber);
                        sPEditor.putString("checkoutid", checkout);
                        sPEditor.putString("totalCostPrize", totalCostPrice);
                        //sPEditor.putString("totalCostPrice", String.valueOf(score));
                        sPEditor.apply();

                        //Do something after 100ms
                        //
//                        if(checkout!=null){
//
//                                db.addPhone(phoneNumber,checkout);
//                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  JsonObject json = (JsonObject) ps.parse(brr);
                    //  JsonElement jtree = ps.parse(brr);
                    //   JsonObject jel = jtree.getAsJsonObject();
                    // JsonElement checkout =jel.get("CheckoutRequestID");

                    //  JSONArray ticketArray = response.getJSONArray("CheckoutRequestID");
                    // String CheckoutRequestID=response.getjsonS



//
//                    while ((output = br.readLine()) != null) {
//                        //System.out.println(output);
//                        //outputs = output;
//  }
//System.out.println("jsonnn"+output);

                    //Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG new" , conn.getInputStream().toString());


                    conn.disconnect();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void send2Data(){
        System.out.println("phone"+ phoneNumber);

        HashMap<String, String> jsonParam = new HashMap<String, String>();
        jsonParam.put("phone", phoneNumber);
        jsonParam.put("amount", totalCostPrice);
        jsonParam.put("accountReference", "mnmn");
        jsonParam.put("transactionDesc", "ndmh");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                urlAdress, new JSONObject(jsonParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.d(TAG, " Mpesa respo: " + response.toString());
                    }}, new Response.ErrorListener() {
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
            } }

        ){

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mpesa_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                // List<CheckPay> orderAList=db.getCurrent();
//                for(CheckPay items:orderAList) {
//                    checkoutd=items.getCheckout();
//                    phoneNumbers=items.getPhonenumber();
//                }
//                System.out.println("phoneNumbers"+phoneNumber);
                SharedPreferences sharedPreferences = getSharedPreferences(MPESA_SPREF, MODE_PRIVATE);
                String phonenumber = sharedPreferences.getString("phonenumber", "");
                String checkoutid = sharedPreferences.getString("checkoutid", "");
                String totalCostPrice = sharedPreferences.getString("totalCostPrice", "");
////////     or         /////
//                String email = getSharedPreferences(MPESA_SPREF, MODE_PRIVATE).getString("checkoutid","EMPTY");
//                if (!email.equals("EMPTY")){
//                    etEmail.setText(email);
//                }

                //session.createLoginSession(name, email, uid, phone, status, user_type,preference_skill, image_path);

                getResponse(phonenumber,checkoutid,totalCostPrice);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
