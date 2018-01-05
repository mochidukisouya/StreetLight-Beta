package com.example.streetlight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.example.streetlight.login.MD5;

/**
 * Created by Mochiduki on 2017/11/27.
 */

public class counties extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layout2  ;
    LinearLayout layout;
    String account,timenow,hash,url,status,num;
    String json_obj;
    JSONArray CITYNO;
    String CITYNM;
    //Button TmpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("請選擇縣市");

        setContentView(R.layout.counties);
        layout= (LinearLayout) findViewById(R.id.L);
        account = this.getIntent().getExtras().getString("userID");
        url = this.getIntent().getExtras().getString("serverIP");
        timenow ="15:20:12";
        hash = MD5(timenow+getResources().getString(R.string.secret));

        try {
            transData();
        } catch (JSONException e) {
            e.printStackTrace();
        }






    }

    @Override
    public void onClick(View v) {
        for (int i=0;i<CITYNO.length();i++){
            if (v.getId()==i){
                try {
                    num=CITYNO.getJSONObject(i).getString("CITYNO");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(counties.this,projectList.class);
                Bundle bundle = new Bundle();
                bundle.putString("userID", account);
                bundle.putString("serverIP", url);
                bundle.putString("CITYNO",num);
                Log.d("CITYNO",num);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
    protected void transData() throws JSONException {

        RequestQueue mQuene = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://"+url+ getResources().getString(R.string.url_C) +getResources().getString(R.string.key)+"&timenow="+timenow+"&hash="+hash,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json_obj = response;
                        try {
                            status = new JSONObject(String.valueOf(json_obj)).getString("status");
                            if(status.equals("success")) {
                                CITYNO = new JSONArray(new JSONObject(String.valueOf(json_obj)).getString("data"));
                                addButton();
                            }else{
                                Toast.makeText(getApplicationContext(),"讀取失敗", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        mQuene.add(stringRequest);
    }
    public  void addButton(){
        for(int i =0;i<CITYNO.length();i++){
            if(i%3==0){
                layout2 = new LinearLayout(this);
                layout.addView(layout2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.HORIZONTAL);
            }
                Button TmpBtn = new Button(getApplicationContext());
                try {
                    CITYNM=CITYNO.getJSONObject(i).getString("CITYNM");
                    TmpBtn.setText(CITYNM);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(CITYNM!=null){
                    TmpBtn.setTextSize(28);
                    TmpBtn.setId(i);
                    //Log.d("ID", String.valueOf(TmpBtn.getId()));
                    TmpBtn.setOnClickListener(this);
                    LinearLayout.LayoutParams param =new LinearLayout.LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,120,getResources().getDisplayMetrics()),(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,90,getResources().getDisplayMetrics()));
                    layout2.addView(TmpBtn, param);
                }
        }
    }
}
