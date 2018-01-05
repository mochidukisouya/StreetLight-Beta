package com.example.streetlight;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mochiduki on 2017/11/15.
 */

public class login extends AppCompatActivity {
    String account,password,hash,url,json_obj,status,data;
    EditText userID,userPWD,serverIP ;
    Button BTN_LOG,BTN2;
    JSONArray LIST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userID = (EditText)findViewById(R.id.userID);
        userPWD = (EditText)findViewById(R.id.userPWD);
        serverIP = (EditText)findViewById(R.id.serverIP);
        BTN_LOG =(Button)findViewById(R.id.BTN_LOG);
        //BTN2 =(Button)findViewById(R.id.BTN2);
        setClickListener();
    }

    private void setClickListener() {
        BTN_LOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = String.valueOf(userID.getText());
                password = String.valueOf(userPWD.getText());
                url =  String.valueOf(serverIP.getText());
                hash = MD5(account+password+getResources().getString(R.string.secret));

                //Log.d("MD5acc",hash);
                try {
                    transData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
//        BTN2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(login.this,MapsActivity.class);
//                startActivity(intent);
//            }
//        });

    }
    protected void transData() throws JSONException {

        RequestQueue mQuene = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://"+url+ getResources().getString(R.string.url_L) +getResources().getString(R.string.key)+ "&account="+account+"&password="+password+"&hash="+hash,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json_obj = response;


                        try {
                            status = new JSONObject(String.valueOf(json_obj)).getString("status");
                            data =new JSONObject(String.valueOf(json_obj)).getString("data");

                            if(status.equals("success")) {
                                //message = new JSONObject(new JSONObject(String.valueOf(json_obj)).getString("data")).getString("message");
                                LIST = new JSONArray(new JSONObject(data).getString("list"));
                                //pd2 = ProgressDialog.show(Login.this, "", "登入中,請稍後......");
                                Toast.makeText(getApplicationContext(),"登入成功", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(login.this,projectList.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userID", account);
                                bundle.putString("userPWD", password);
                                bundle.putString("serverIP", url);
                                bundle.putString("list", String.valueOf(LIST));
                                Log.d("list", String.valueOf(LIST));
                                intent.putExtras(bundle);
                                startActivity(intent);

                                //pd2.dismiss();
                            }else{
                                //message = new JSONObject(new JSONObject(String.valueOf(json_obj)).getString("error")).getString("message");
                                Toast.makeText(getApplicationContext(),"登入失敗", Toast.LENGTH_SHORT).show();
                            }

                            //Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                            //code = new JSONObject(new JSONObject(String.valueOf(json_obj)).getString("error")).getString("code");
                            //error = new JSONObject(new JSONObject(String.valueOf(json_obj)).getString("error")).getString("message");

                            //Log.d("test","XXXX");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        if (response.equals("OK")) {
//                            Toast.makeText(getApplicationContext(), "上傳成功", Toast.LENGTH_SHORT).show();
//                          }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<>();
//
//                return param;
//            }
//        };
        mQuene.add(stringRequest);
    }
    public static String MD5(String str)
    {
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for(int i = 0; i < charArray.length; i++){
            byteArray[i] = (byte)charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for( int i = 0; i < md5Bytes.length; i++){
            int val = ((int)md5Bytes[i])&0xff;
            if(val < 16){
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


}
