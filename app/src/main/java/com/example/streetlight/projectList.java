package com.example.streetlight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.streetlight.login.MD5;

/**
 * Created by Mochiduki on 2017/11/27.
 */

public class projectList extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout;
    String account,password,hash,url,status,num,PagesIndex;
    String json_obj;
    JSONArray LIST;
    String TITLE,CLSEQNO,AREA,CITYNO,CITYNM;
    String[] AREAlist;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                if (Integer.valueOf(PagesIndex)>1)
                    PagesIndex = String.valueOf(Integer.valueOf(PagesIndex)-1);
                //Log.d("PAGE",PagesIndex);
                break;
            case 2:
                PagesIndex = String.valueOf(Integer.valueOf(PagesIndex)+1);
                //Log.d("PAGE",PagesIndex);
                break;

        }
        //item.get.setTitle(PagesIndex+"");
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        //Log.d("PAGE",PagesIndex);
        if(Integer.valueOf(PagesIndex)>1){
            menu.add(Menu.NONE, 0, Menu.NONE, "◀").setShowAsAction(1);
        }
        menu.add(Menu.NONE, 1, Menu.NONE, PagesIndex).setShowAsAction(1);
        menu.add(Menu.NONE, 2, Menu.NONE, "▶").setShowAsAction(1);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("工程專案");
        setContentView(R.layout.counties);
        layout= (LinearLayout) findViewById(R.id.L);
        account = this.getIntent().getExtras().getString("userID");
        password = this.getIntent().getExtras().getString("userPWD");
        url = this.getIntent().getExtras().getString("serverIP");
        //num = this.getIntent().getExtras().getString("CITYNO");
        PagesIndex = "1";
        hash = MD5(account+password+getResources().getString(R.string.secret));

        try {
            transData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        for (int i=0;i<LIST.length();i++){
            if (v.getId()==i){
                try {
                    TITLE=LIST.getJSONObject(i).getString("TITLE");
                    CLSEQNO =LIST.getJSONObject(i).getString("CLSEQNO");
                    AREA =LIST.getJSONObject(i).getString("AREA");
                    CITYNO =LIST.getJSONObject(i).getString("CITYNO");
                    CITYNM =LIST.getJSONObject(i).getString("CITYNM");
                    AREAlist =AREA.split("\\◎");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(projectList.this,menu.class);
                Bundle bundle = new Bundle();
                bundle.putString("userID", account);
                bundle.putString("serverIP", url);
                bundle.putString("TITLE",TITLE);
                bundle.putString("CLSEQNO",CLSEQNO);
                bundle.putString("CITYNO",CITYNO);
                bundle.putStringArray("AREAlist",AREAlist);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
    protected void transData() throws JSONException {

        RequestQueue mQuene = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://"+url+ getResources().getString(R.string.url_L) +getResources().getString(R.string.key)+ "&account="+account+"&password="+password+"&hash="+hash,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json_obj = response;
                        try {
                            status = new JSONObject(json_obj).getString("status");
                            if(status.equals("success")) {
                                LIST =new JSONArray(new JSONObject(new JSONObject(json_obj).getString("data")).getString("list"));
                                Log.d("DATA", String.valueOf(LIST.getJSONObject(0).getString("TITLE")));

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
        for(int i =0;i<LIST.length();i++){
            Button TmpBtn = new Button(getApplicationContext());
            try {
                TITLE=LIST.getJSONObject(i).getString("TITLE");

                TmpBtn.setText(TITLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(TITLE!=null){
                TmpBtn.setTextSize(32);
                TmpBtn.setId(i);
                TmpBtn.setOnClickListener(this);
                LinearLayout.LayoutParams param =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,getResources().getDisplayMetrics()));
                layout.addView(TmpBtn, param);
            }
        }
    }
}

