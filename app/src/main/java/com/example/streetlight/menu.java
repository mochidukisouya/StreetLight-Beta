package com.example.streetlight;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by Mochiduki on 2017/11/17.
 */

public class menu extends AppCompatActivity {
    TextView nowproject;
    EditText lampno;
    String account,serverIP,TITLE,SEQNO,CLSEQNO,CITYNO,SLNO,hash,LNG,LAT,QTY,O_TYPE,N_TYPE,O_WATTS,N_WATTS;
    boolean isLAST=false;
    int area_index;
    String[] AREAlist,lampdetail={"0","1","2","3","4","5","6","7","8","9"};
    ArrayList<String> lampType_typelist =new ArrayList();
    //ArrayList<String> lampno_list =new ArrayList();
    ArrayList<String> lampType_alllist =new ArrayList();
    String[] LampTypeArray = {""};
    String[] LampAllArray = {""};
    //String[] testArray = {""};

    Button BTN_NEW,BTN_LAST;
    ImageButton BTN_SEARCH;
    String PagesIndex = "1";
    private ListView listView;
    private ArrayList<String> lightlist = new ArrayList<>();
    private ArrayAdapter<String> listAdapter,listAdapter2;

    String json_obj,status,refresh="No";
    JSONArray LIST,LampType;
    SharedPreferences sharedPreferences ;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                if (Integer.valueOf(PagesIndex)>1)
                PagesIndex = String.valueOf(Integer.valueOf(PagesIndex)-1);
                //Log.d("PAGE",PagesIndex);
                try {
                    GetLightList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                PagesIndex = String.valueOf(Integer.valueOf(PagesIndex)+1);
                //Log.d("PAGE",PagesIndex);
                try {
                    GetLightList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        setContentView(R.layout.menu);
        lampno =(EditText)findViewById(R.id.lampno);
        sharedPreferences = getSharedPreferences("LastData" , MODE_PRIVATE);
        account = this.getIntent().getExtras().getString("userID");
        serverIP = this.getIntent().getExtras().getString("serverIP");
        TITLE = this.getIntent().getExtras().getString("TITLE");
        CLSEQNO = this.getIntent().getExtras().getString("CLSEQNO");
        CITYNO = this.getIntent().getExtras().getString("CITYNO");
        AREAlist = this.getIntent().getExtras().getStringArray("AREAlist");
        getSupportActionBar().setTitle(TITLE);
        BTN_NEW = (Button)findViewById(R.id.BTN_NEW);
        BTN_LAST = (Button)findViewById(R.id.BTN_LAST);
        BTN_SEARCH = (ImageButton) findViewById(R.id.BTN_SEARCH);
        listView = (ListView)findViewById(R.id.list_view);
        setClickListener();
        lampType_typelist.add("Select");
        //lampno_list.add("LAMPNO");
        hash = MD5(CLSEQNO+PagesIndex+getResources().getString(R.string.secret));

        try {
//            transData();
            GetLampType();
            GetLightList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void setClickListener() {
        BTN_NEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(menu.this,dataEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("userID", account);
                bundle.putString("serverIP",serverIP);
                lampdetail[2]= String.valueOf(lampno.getText());
                bundle.putStringArray("lampdetail",lampdetail);
                bundle.putString("CLSEQNO",CLSEQNO);
                bundle.putString("CITYNO",CITYNO);
                SEQNO = "-1";
                bundle.putString("SEQNO",SEQNO);
                //bundle.putStringArray("testArray",testArray);
                bundle.putStringArray("LampTypeArray",LampTypeArray);
                bundle.putStringArray("LampAllArray",LampAllArray);
                bundle.putStringArray("AREAlist",AREAlist);
                //bundle.putString("test",LampType.toString());
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
                //startActivity(intent);
                //finish();

            }
        });
        BTN_SEARCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SLNO = String.valueOf(lampno.getText());
                if(lampno.getText().length()!=0){
                //if(SLNO !="輸入路燈編號"){
                    SLNO = String.valueOf(lampno.getText());
                    Log.d("OOO","11");
                    try {
                        SearchLight();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    SLNO = "-1";
                    //Toast.makeText(getApplicationContext(), "請輸入查詢編號", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), SLNO, Toast.LENGTH_SHORT).show();

                        addlist();

                }

            }
        });
        BTN_LAST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CLSEQNO.equals(sharedPreferences.getString("CLSEQNO" , "0"))){
                    //Log.d("XXX","XXX");
                    CLSEQNO =sharedPreferences.getString("CLSEQNO" ,"0");
                    SEQNO =sharedPreferences.getString("SEQNO" ,"0");
                    lampdetail[2]=SEQNO;
                    area_index =sharedPreferences.getInt("area_index" , 0);
                    LNG = sharedPreferences.getString("LNG" , "0");
                    LAT = sharedPreferences.getString("LAT" , "0");
                    QTY = sharedPreferences.getString("QTY" , "0");
                    O_TYPE = sharedPreferences.getString("O_TYPE" , "0");
                    O_WATTS = sharedPreferences.getString("O_WATTS" , "0");
                    N_TYPE = sharedPreferences.getString("N_TYPE" , "0");
                    N_WATTS = sharedPreferences.getString("N_WATTS" , "0");
                    isLAST = true;
                    Log.d("+++++++++++","+++++++++++++");
                    Log.d("CLSEQNO",CLSEQNO);
                    Log.d("SP_CLSEQNO",sharedPreferences.getString("CLSEQNO" ,""));
                    Log.d("SP_SEQNO",sharedPreferences.getString("SEQNO" ,""));
                    Log.d("SP_area_index", String.valueOf(sharedPreferences.getInt("area_index" ,0)));
                    Log.d("area_index", String.valueOf(area_index));
                    Log.d("SP_LNG",sharedPreferences.getString("LNG" ,""));
                    Log.d("SP_LAT",sharedPreferences.getString("LAT" ,""));
                    Log.d("SP_QTY",sharedPreferences.getString("QTY" ,""));
                    Log.d("SP_O_TYPE",sharedPreferences.getString("O_TYPE" ,""));
                    Log.d("O_TYPE",O_TYPE);
                    Log.d("SP_O_WATTS",sharedPreferences.getString("O_WATTS" ,""));
                    Log.d("SP_N_TYPE",sharedPreferences.getString("N_TYPE" ,""));
                    Log.d("SP_N_WATTS",sharedPreferences.getString("N_WATTS" ,""));
                    Log.d("lampdaitl",lampdetail[2]);
                    Log.d("----------","---------------");
                    Intent intent = new Intent(menu.this,dataEdit.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userID", account);
                    bundle.putString("serverIP",serverIP);
                    bundle.putString("CLSEQNO",CLSEQNO);
                    bundle.putString("CITYNO",CITYNO);
                    bundle.putString("SEQNO",SEQNO);
                    bundle.putInt("area_index",area_index);
                    bundle.putInt("otype_index",sharedPreferences.getInt("otype_index" , 0));
                    bundle.putInt("ntype_index",sharedPreferences.getInt("ntype_index" , 0));
                    bundle.putString("LNG",LNG);
                    bundle.putString("LAT",LAT);
                    bundle.putString("QTY",QTY);
                    bundle.putString("O_TYPE",O_TYPE);
                    bundle.putString("N_TYPE",N_TYPE);
                    bundle.putString("O_WATTS",O_WATTS);
                    bundle.putString("N_WATTS",N_WATTS);
                    bundle.putString("LAMPNO_PICS_FILE",sharedPreferences.getString("LAMPNO_PICS_FILE" ,""));
                    bundle.putString("B_PICS_FILE",sharedPreferences.getString("B_PICS_FILE" ,""));
                    bundle.putString("I_PICS_FILE",sharedPreferences.getString("I_PICS_FILE" ,""));
                    bundle.putString("A_PICS_FILE",sharedPreferences.getString("A_PICS_FILE" ,""));
                    bundle.putString("P_PICS_FILE",sharedPreferences.getString("P_PICS_FILE" ,""));
                    bundle.putStringArray("LampTypeArray",LampTypeArray);
                    bundle.putStringArray("LampAllArray",LampAllArray);
                    bundle.putStringArray("lampdetail",lampdetail);
                    bundle.putStringArray("AREAlist",AREAlist);
                    bundle.putBoolean("isLast",isLAST);
                    if(isLAST)
                    intent.putExtras(bundle);
                    startActivity(intent);

                }




            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            SEQNO = "0";
            try {
                GetLightList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    protected void GetLampType() throws JSONException {
        RequestQueue mQuene = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://"+serverIP+ getResources().getString(R.string.light_Type) +getResources().getString(R.string.key),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json_obj = response;
                        try {
                            status = new JSONObject(json_obj).getString("status");
                            if(status.equals("success")) {
                                LampType =new JSONArray(new JSONObject(json_obj).getString("data"));

                                for (int i =1;i<=LampType.length();i++){
                                    try {
                                        //oldTypeArray[i]=LampType.getJSONObject(i-1).getString("NAME");
                                        lampType_typelist.add(LampType.getJSONObject(i-1).getString("NAME"));
                                        //lampno_list.add(LampType.getJSONObject(i-1).getString("LAMPNO"));
                                        lampType_alllist.add(LampType.get(i-1).toString());
                                        //oldTypeSelect = oldTypeArray[i+1];
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                //Log.d("XXX", String.valueOf(LampType));
                                Log.d("XXX-1", String.valueOf(lampType_alllist));
                                //Log.d("000", String.valueOf(lampType_typelist));
                                //Log.d("111", String.valueOf(lampno_list));
                                LampTypeArray= lampType_typelist.toArray(new String[0]);
                                LampAllArray= lampType_alllist.toArray(new String[0]);
                                //Log.d("000",lampType_alllist.);
//                                Log.d("DATA", String.valueOf(LampType));
//                                Log.d("DATA", String.valueOf(testlist));
//                                Log.d("DATA", String.valueOf(oldTypeArray[2]));
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


    protected void GetLightList() throws JSONException {

        RequestQueue mQuene = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://"+serverIP+ getResources().getString(R.string.light_List) +getResources().getString(R.string.key)+"&CLSEQNO="+CLSEQNO+"&PagesIndex="+PagesIndex+"&hash="+hash,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json_obj = response;
                        try {
                            status = new JSONObject(json_obj).getString("status");

                            if(status.equals("success")) {
                                LIST =new JSONArray(new JSONObject(new JSONObject(json_obj).getString("data")).getString("list"));

                                Log.d("GetSTREETLIGHTList", String.valueOf(LIST));
                                //.getJSONObject(0).getString("TITLE")
                                //SLNO = "0";
                                addlist();
                            }else{
                                Toast.makeText(getApplicationContext(),"讀取失敗", Toast.LENGTH_SHORT).show();
                                listView.setAdapter(null);
                                lightlist.clear();
                                //listView.setAdapter(listAdapter);
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
    protected void SearchLight() throws JSONException {

        RequestQueue mQuene = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://"+serverIP+ getResources().getString(R.string.light_List) +getResources().getString(R.string.key)+"&CLSEQNO="+CLSEQNO+"&SLNO="+SLNO+"&PagesIndex="+PagesIndex+"&hash="+hash,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json_obj = response;
                        try {
                            status = new JSONObject(json_obj).getString("status");
                            Log.d("000",response);
                            Log.d("000",SLNO);
                            if(status.equals("success")) {
                                LIST =new JSONArray(new JSONObject(new JSONObject(json_obj).getString("data")).getString("list"));

                                Log.d("SearchSTREETLIGHT", String.valueOf(LIST));
                                //.getJSONObject(0).getString("TITLE")
                                //listView.setAdapter(null);
                                addlist();
                            }else{
                                Toast.makeText(getApplicationContext(),"讀取失敗", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            SEQNO = "-1";
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


    public void addlist(){
        try {
            if(SLNO == "-1" || SEQNO =="-1") {
                listView.setAdapter(null);
                lightlist.clear();
                lightlist.add("請輸入查詢編號\n點此重整");
                listAdapter2 = new ArrayAdapter(this, R.layout.font3, lightlist) {
                    @NonNull
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        view.setLayoutParams(params);
                        return view;
                    }
                };
                listView.setAdapter(listAdapter2);
                //Log.d("000","000");
            }else if(LIST.getJSONObject(0).getString("SEQNO")!="-1" &&SLNO != "-1"){
                listView.setAdapter(null);
                lightlist.clear();
                for (int i = 0; i < LIST.length(); i++) {
                    try {
                        lampdetail[2] = String.valueOf(LIST.getJSONObject(i).getString("SLNO"));
                        lampdetail[7] = String.valueOf(LIST.getJSONObject(i).getString("AREA"));
                        lampdetail[8] = String.valueOf(LIST.getJSONObject(i).getString("LAMPSTYLE"));
                        lampdetail[9] = String.valueOf(LIST.getJSONObject(i).getString("WATTS"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lightlist.add("路燈編號:" + lampdetail[2] + "　地區:" + lampdetail[7] + "\n燈種:" + lampdetail[8] + "　瓦特數:" + lampdetail[9] + "W");
                }
                listAdapter = new ArrayAdapter(this, R.layout.font, lightlist) {
                    @NonNull
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        view.setLayoutParams(params);
                        return view;
                    }
                };
                listView.setAdapter(listAdapter);
            }
            else {
                Toast.makeText(getApplicationContext(),SLNO, Toast.LENGTH_SHORT).show();
                //GetLightList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "你選擇的是" + lightlist.get(position), Toast.LENGTH_SHORT).show();
                if (SLNO == "-1") {
                    try {
                        SLNO ="0";
                        GetLightList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        lampdetail[0] = String.valueOf(LIST.getJSONObject(position).getString("SEQNO"));
                        lampdetail[1] = String.valueOf(LIST.getJSONObject(position).getString("CLSEQNO"));
                        lampdetail[2] = String.valueOf(LIST.getJSONObject(position).getString("SLNO"));
                        lampdetail[3] = String.valueOf(LIST.getJSONObject(position).getString("CITYNM"));
                        lampdetail[4] = String.valueOf(LIST.getJSONObject(position).getString("NAME_1"));
                        lampdetail[5] = String.valueOf(LIST.getJSONObject(position).getString("NAME_2"));
                        lampdetail[6] = String.valueOf(LIST.getJSONObject(position).getString("NAME_3"));
                        lampdetail[7] = String.valueOf(LIST.getJSONObject(position).getString("AREA"));
                        lampdetail[8] = String.valueOf(LIST.getJSONObject(position).getString("LAMPSTYLE"));
                        lampdetail[9] = String.valueOf(LIST.getJSONObject(position).getString("WATTS"));
                        SEQNO = lampdetail[0];
                        CLSEQNO = lampdetail[1];
//                    for (int i=0;i<10;i++){
//                        Log.d("detail",lampdetail[i]);
//                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(menu.this, dataEdit.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userID", account);
                    bundle.putString("serverIP", serverIP);
                    bundle.putStringArray("lampdetail", lampdetail);
                    bundle.putString("SEQNO", SEQNO);
                    bundle.putString("CLSEQNO", CLSEQNO);
                    bundle.putString("CITYNO", CITYNO);
                    bundle.putStringArray("LampTypeArray", LampTypeArray);
                    bundle.putStringArray("LampAllArray", LampAllArray);
                    bundle.putStringArray("AREAlist", AREAlist);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,0);
//                    startActivity(intent);
                    //finish();
                }
            }
        });


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
