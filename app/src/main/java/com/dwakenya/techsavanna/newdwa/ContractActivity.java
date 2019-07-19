package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContractActivity extends AppCompatActivity {
    public static  String GET_AMOUNT="https://techsavanna.net:8181/dwa/pdfApis.php";
    RecyclerView PdfRecyclerView;
    List<PDFModel> pdfModel = new ArrayList<>();
    PDFAdapter pdfAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        loadAmount();
    }
    
    private void loadAmount() {
    
    final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Content");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    
    PdfRecyclerView = findViewById(R.id.ContractRecyclerView);
        PdfRecyclerView.setHasFixedSize(true);
        PdfRecyclerView.setNestedScrollingEnabled(false);
        PdfRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    
    
    StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_AMOUNT,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++){
                            
                            JSONObject object = array.getJSONObject(i);
                            String file = object.getString("file");
                            String name = object.getString("name");
                            String category = object.getString("category");
                            
                            PDFModel pdfModel1 = new PDFModel(file, name);
                            if (category.equals("agreement")){
                                progressDialog.dismiss();
                                pdfModel.add(pdfModel1);
                            }
                        }
                        pdfAdapter = new PDFAdapter(ContractActivity.this, pdfModel);
                        PdfRecyclerView.setAdapter(pdfAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();
            final AlertDialog.Builder builder = new AlertDialog.Builder(ContractActivity.this);
            builder.setTitle("No internet connection...");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    finish();
                }
            });
            
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            
        }
        
        
    });
        
        
        
        Volley.newRequestQueue(ContractActivity.this).add(stringRequest);
}
}