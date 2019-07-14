package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.SSLCertificateSocketFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.SQLiteHandler;
import com.dwakenya.techsavanna.newdwa.holders.CheckSubscriptions;
import com.dwakenya.techsavanna.newdwa.holders.Subscriptions;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MpesaActivity extends AppCompatActivity {

    private static final String TAG = MpesaActivity.class.getSimpleName();
    private final String MPESA_SPREF = "MPESA_SPREF";
    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.editTextAmount)
    TextView editTextAmount;

    @BindView(R.id.sendButton)
    Button sendButton;

    @BindView(R.id.sendContinue)
    Button sendContinue;
    String checkout,checkoutd;
    @BindView(R.id.totalamount)
    TextView totalamount;


   public String selected, myAmount1, myAmount2,mysubs7, mysub9, strLastFourDi;

    //Declare Daraja :: Global Variable

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private  static final String urlAdress="https://techsavanna.net:3000/DomesticMpesa/webresources/domesticmpesa/mpesa/push";

    private static final String SERVER_PATH = "Path_to_Server_To_Store_Token";

    String phoneNumber,totalCostPrice;
    SQLiteHandler db;
    boolean error;

    int finalid;


    ProgressDialog progressDialog;

    //url to get the Subscription data from the database
    private String URL = "https://techsavanna.net:8181/dwa/getEmployerSubs.php";
    private ArrayList<Subscriptions> mySubscriptionList;

    private ArrayList<CheckSubscriptions> myData;


    private ArrayList<String> myDataset;
    private Spinner spinner;


    //variables
    String mpesa_receipt_number, account_reference, amount, partya, result_code, confirm_date;
    private ArrayList<String> mySubscription;
    //String URL2 = "https://techsavanna.net:8181/dwa/mpesaApis.php?partya=254"+strLastFourDi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);

//        sendContinue.findViewById(R.id.sendContinue);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Make Payement");
        myToolbar.setTitleTextColor(Color.WHITE);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        //totalCostPrice = intent.getDoubleExtra("money",0);

        //totalamount.setText("Total Amount: "+totalCostPrice);
        db=new SQLiteHandler(this);

        sendContinue.setEnabled(false);

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
//                if(TextUtils.isEmpty(totalCostPrice)){
//                    editTextAmount.setError("Please Enter Amount");
//                }
                sendPost();
                editTextPhoneNumber.setEnabled(false);
                spinner.setEnabled(false);
                sendButton.setEnabled(false);

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




        mySubscription=new ArrayList<>();
        spinner= findViewById(R.id.subscription);
        loadSpinnerData(URL);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                finalid = Integer.parseInt(selected.substring(0, selected.indexOf(" ")));

               Log.d("hahah", ">>" + selected);
                Log.d("maneno", ">>" + finalid);

                if (selected.equals("1 Yearly")){
                    editTextAmount.setText(myAmount1);

                }else if(selected.equals("2 Monthly")){
                    editTextAmount.setText(myAmount2);
                }



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sendContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MpesaActivity.this);
                progressDialog.setTitle("Verifying your Subscription");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                CheckPayment();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                    }
                },50000);



            }
        });
    }

    private void CheckPayment() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://techsavanna.net:8181/dwa/mpesaApis.php?partya=254"+strLastFourDi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("new strrr", ">>" + response);

                    myData = new ArrayList<>();

                    JSONObject obj = new JSONObject(response);

                        JSONArray dataArray1  = obj.getJSONArray("data");

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



                            for (int k = 0; k<myData.size();k++){

                                mpesa_receipt_number = myData.get(k).getMpesa_receipt_number();
                                account_reference = myData.get(k).getAccount_reference();
                                amount=myData.get(k).getAmount();
                                partya=myData.get(k).getPartya();
                                result_code=myData.get(k).getResult_code();
                                confirm_date=myData.get(k).getConfirm_date();




                               // Log.d("Message", "img " +mpesa_receipt_number+" >> "+ account_reference+ ">> "+amount+ " >> "+partya+">>"+result_code+">>"+confirm_date);


                                Intent detailIntent = new Intent(MpesaActivity.this, SearchActivity.class);
                                //Bundle bundle = new Bundle();

                                detailIntent.putExtra("mpesa_receipt_number", mpesa_receipt_number);
                                detailIntent.putExtra("account_reference", account_reference);
                                detailIntent.putExtra("partya", partya);
                                detailIntent.putExtra("amount", amount);
                                detailIntent.putExtra("result_code", result_code);



                                Log.d("huhuhaaa", ">>>" +partya);



                                startActivity(detailIntent);
                                finish();


                            }






                        }

                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest1.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(MpesaActivity.this);
        requestQueue.add(stringRequest1);
    }


    private void loadSpinnerData(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("strrrrr", ">>" + response);

                    JSONObject obj = new JSONObject(response);
                    if(obj.optString("status").equals("true")){
                        mySubscriptionList = new ArrayList<>();
                        JSONArray dataArray  = obj.getJSONArray("subs");

                        for (int i = 0; i < dataArray.length(); i++) {
                            Subscriptions subscriptions = new Subscriptions();
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            subscriptions.setSubscriptionid(dataobj.getString("subscriptionid"));
                            subscriptions.setSubscriptionType(dataobj.getString("subscriptionType"));
                            subscriptions.setSubscriptionAmount_KSH(dataobj.getString("subscriptionAmount"));
                            subscriptions.setSubscriptionDuration_Days(dataobj.getString("subscriptionDuration"));

                            mySubscriptionList.add(subscriptions);



                        }
                        for (int i = 0; i<mySubscriptionList.size();i++){

                            mysubs7 = mySubscriptionList.get(i).getSubscriptionid();
                            mysub9 = mySubscriptionList.get(i).getSubscriptionType();


                            mySubscription.add(mysubs7+" "+mysub9);


                            //get the Amounts
                            myAmount1 = mySubscriptionList.get(0).getSubscriptionAmount_KSH();
                            myAmount2 = mySubscriptionList.get(1).getSubscriptionAmount_KSH();


                        }


                    }

                    spinner.setAdapter(new ArrayAdapter<>(MpesaActivity.this, R.layout.myspinner, mySubscription));
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(MpesaActivity.this);
        requestQueue.add(stringRequest);
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
                                    Intent net=new Intent(MpesaActivity.this,SearchActivity.class);
                                    startActivity(net);
                                    finish();
                                }else if(name != null  && amount==1 ){
                                    Intent net=new Intent(MpesaActivity.this,EmployeeHomeActivity.class);
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
        progressDialog = new ProgressDialog(MpesaActivity.this);
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
                    strLastFourDi = phoneNumber.length() >= 9 ? phoneNumber.substring(phoneNumber.length() - 9): "";
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("phone", "254"+strLastFourDi);
                    jsonParam.put("amount", totalCostPrice);
                    jsonParam.put("accountReference", finalid);
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
        sendContinue.setEnabled(true);
        sendContinue.setVisibility(View.VISIBLE);
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
