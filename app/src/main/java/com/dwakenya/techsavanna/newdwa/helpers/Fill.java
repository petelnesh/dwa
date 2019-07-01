package com.dwakenya.techsavanna.newdwa.helpers;

import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.holders.RegionHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Padie on 8/14/2017.
 */

public class Fill {

    private static String TAG = "";
    private static String selected_region_id;

    public static ArrayList<String> fillRegion() {

        final ArrayList<RegionHolder> regionHolderArrayList = new ArrayList<>();
        final ArrayList<String> list = new ArrayList<String>();
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
                                        JSONArray jArray = object.getJSONArray("hosp");
                                        Log.d(TAG, "Array: " + jArray.toString());


                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            RegionHolder hospHolder = new RegionHolder(
                                                    singleRow.getString("id"),
                                                    singleRow.getString("region_name"));
                                            regionHolderArrayList.add(hospHolder);
                                        }

                                        for (int i = 0; i < regionHolderArrayList.size(); i++) {
                                            list.add(regionHolderArrayList.get(i).getRegion_name());
                                        }

                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
//                                                Toast.makeText(getApplicationContext(), "No programs available", Toast.LENGTH_LONG).show();
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
                AppController.getInstance().addToRequestQueue(req);
            }
        };
        thread.start();

        return list;
    }

    public static String getSelectedRegionId(final String reg) {



        Thread thread = new Thread() {
            Handler handler = new Handler();
            boolean error;

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("region_name", reg);


                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "get_region_id.php", new JSONObject(params),
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

                                        JSONArray jArray = object.getJSONArray("regions");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        List<String> list = new ArrayList<String>();
                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            selected_region_id = singleRow.getString("id");
                                        }
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
//                                                Toast.makeText(getApplicationContext(), "No programs available", Toast.LENGTH_LONG).show();
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

        return selected_region_id;
    }

}
