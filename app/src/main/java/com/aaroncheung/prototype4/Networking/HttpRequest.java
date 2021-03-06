package com.aaroncheung.prototype4.Networking;

import android.content.Context;
import android.util.Log;

import com.aaroncheung.prototype4.Helper.UserInformationSingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpRequest {

    private String TAG = "debug_123";

    private RequestQueue requestQueue;
    private String finalResponse = "0";
    private JSONObject myJSONObject;
    private String SERVER_URL;

    public HttpRequest(Context context){
        Log.d(TAG, "Connection");
        SERVER_URL = UserInformationSingleton.getInstance().getSERVER_URL();
        requestQueue = Volley.newRequestQueue(context);
    }

    public void sendLoginGetRequest(String email){
        String url = SERVER_URL + "api/authentication/" + email;
        Log.d(TAG, url);
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d(TAG, "GET Success");
                            JSONObject jsonObject = response.getJSONObject(0);
                            Log.d(TAG, jsonObject.toString());
                            myJSONObject = jsonObject;
                            setResponse("1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        setResponse("0");
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void sendPostRequest(JSONObject jsonBodyPost){
        Log.d(TAG, "postRequest");
        String url = SERVER_URL + "api/authentication";
        JsonObjectRequest postReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonBodyPost,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.d(TAG, "POST Success");
                        try {
                            Log.d(TAG, response.get("status").toString());
                            setResponse(response.get("status").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Success Callback

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Log.d(TAG, "POST Error");
                        setResponse("0");
                        //Failure Callback

                    }
                });

        requestQueue.add(postReq);
    }

    public void setResponse(String response){
        if(response.matches("0")){
            finalResponse = "0";
        }
        else if(response.matches("1")){
            finalResponse = "1";
        }
    }

    public JSONObject getMyJSONObject() {
        return myJSONObject;
    }

    public String getResponse(){
        return finalResponse;
    }


}