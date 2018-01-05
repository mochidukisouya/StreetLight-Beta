package com.example.streetlight;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class dataEdit extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    EditText number, address, amount, oldPower, newPower;
    TextView userID, time, longitude, latitude;
    Spinner oldType, newType, areaSpinner;


    String[] oldTypeArray,LampTypeArray={""},LampAllArray={""} ;
    ArrayList<String> testlist = new ArrayList();
    ArrayList<String> lampno_list = new ArrayList();
    //List<String> testlist;
    ArrayAdapter<String> adapter;
    boolean isLAST=false;


    String[] newTypeArray = {"select", "newType1", "newType2", "newType3"};
    String[] AREAlist,lampdetail;
    ImageView photo1, photo2, photo3, photo4,photo5;
    Button getlocation, map,submit;
    String user,lng="",lat="", nowtime, areaSelect, oldTypeSelect, newTypeSelect;
    Bitmap bitmap;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    String serverIP,MESSAGE,SEQNO,CLSEQNO,SLNO,CITYNO,CITYNM, NAME_1, NAME_2, NAME_3,AREA,O_KINDSEQNO="",N_KINDSEQNO="", LAMPSTYLE,WATTS, OLDLAMPSTYLE,OLDWATTS, QTY;
    String LAMPNO_PICS, LAMPNO_PICS_FILE,B_PICS,B_PICS_FILE,I_PICS,I_PICS_FILE,A_PICS,A_PICS_FILE,P_PICS,P_PICS_FILE;
    String hash,hash2,json_obj,status,url_photo;
    int O_LampIndex,N_LampIndex,area_index;
    JSONArray LampType;
    JSONObject detail;
    Map<String, Integer> param2 = new HashMap<>();
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, Menu.NONE, "返回").setShowAsAction(1);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //item.get.setTitle(PagesIndex+"");
        switch (item.getItemId()){
            case 0:
                new AlertDialog.Builder(this)
                        .setTitle("確定返回?")
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                    }
                                }).show();
                //finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataedit);
        getSupportActionBar().setTitle("新設路燈");
        getPermission();
        init();
        setClickListener();
        //context = this;
        testlist.add("select");
        mGoogleApiClient.connect();
        if (isLAST){
//            oldTypeSelect = this.getIntent().getExtras().getString("O_TYPE");
//            newTypeSelect = this.getIntent().getExtras().getString("N_TYPE");
            oldPower.setText(this.getIntent().getExtras().getString("O_WATTS"));
            newPower.setText(this.getIntent().getExtras().getString("N_WATTS"));
            amount.setText(this.getIntent().getExtras().getString("QTY"));
            areaSpinner.setSelection(sharedPreferences.getInt("area_index" , 0));
            oldType.setSelection(sharedPreferences.getInt("otype_index" , 0));
            newType.setSelection(sharedPreferences.getInt("ntype_index" , 0));
            //areaSelect = this.getIntent().getExtras().getString("AREA");
            Log.d("test", this.getIntent().getExtras().getString("QTY"));
            //Log.d("000", String.valueOf(amount.getText()));
            if (this.getIntent().getExtras().getString("LAMPNO_PICS_FILE").length()!=0){
                bitmap =imgconvert.convert(this.getIntent().getExtras().getString("LAMPNO_PICS_FILE"));
                photo1.setImageBitmap(bitmap);
            }
            if (this.getIntent().getExtras().getString("B_PICS_FILE").length()!=0) {
                bitmap = imgconvert.convert(this.getIntent().getExtras().getString("B_PICS_FILE"));
                photo2.setImageBitmap(bitmap);
            }
            if (this.getIntent().getExtras().getString("I_PICS_FILE").length()!=0) {
                bitmap = imgconvert.convert(this.getIntent().getExtras().getString("I_PICS_FILE"));
                photo3.setImageBitmap(bitmap);
            }
            if (this.getIntent().getExtras().getString("A_PICS_FILE").length()!=0) {
                bitmap = imgconvert.convert(this.getIntent().getExtras().getString("A_PICS_FILE"));
                photo4.setImageBitmap(bitmap);
            }
            if (this.getIntent().getExtras().getString("P_PICS_FILE").length()!=0) {
                bitmap = imgconvert.convert(this.getIntent().getExtras().getString("P_PICS_FILE"));
                photo5.setImageBitmap(bitmap);
            }

        }else {
            //sharedPreferences.edit().clear();

        }
        //hash = MD5(user+"Phototest1117"+getResources().getString(R.string.secret));
    }
    private void start_DL(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap mBitmap1 = downloadJPG(LAMPNO_PICS);
                final Bitmap mBitmap2 = downloadJPG(B_PICS);
                final Bitmap mBitmap3 = downloadJPG(I_PICS);
                final Bitmap mBitmap4 = downloadJPG(A_PICS);
                final Bitmap mBitmap5 = downloadJPG(P_PICS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBitmap1!=null)
                        photo1.setImageBitmap(mBitmap1);
                        if (mBitmap2!=null)
                        photo2.setImageBitmap(mBitmap2);
                        if (mBitmap3!=null)
                        photo3.setImageBitmap(mBitmap3);
                        if (mBitmap4!=null)
                        photo4.setImageBitmap(mBitmap4);
                        if (mBitmap5!=null)
                        photo5.setImageBitmap(mBitmap5);
                    }
                });
            }
        }).start();
    }

    public static Bitmap downloadJPG(String src){
        try {
            URL DL_JPG = new URL(src);
            HttpURLConnection conn = (HttpURLConnection)DL_JPG.openConnection();
            conn.connect();
            InputStream input = conn.getInputStream();
            Bitmap mbitmap = BitmapFactory.decodeStream(input);
            return mbitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA}, 0);
        }
    }



    private TextWatcher onchange=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            sharedPreferences.edit().putString("CLSEQNO" , CLSEQNO).apply();
            sharedPreferences.edit().putString("SEQNO" , String.valueOf(number.getText())).apply();
            //sharedPreferences.edit().putString("AREA" , String.valueOf(areaSpinner)).apply();
            sharedPreferences.edit().putString("LNG" , String.valueOf(longitude.getText())).apply();
            sharedPreferences.edit().putString("LAT" , String.valueOf(latitude.getText())).apply();
            sharedPreferences.edit().putString("QTY" , String.valueOf(amount.getText())).apply();
            //sharedPreferences.edit().putString("O_KINDSEQNO" , String.valueOf(O_KINDSEQNO.getText())).apply();
            //sharedPreferences.edit().putString("N_KINDSEQNO" , String.valueOf(N_KINDSEQNO.getText())).apply();
            //Log.d("SP_CLSEQNO",sharedPreferences.getString("CLSEQNO" ,""));
//            Log.d("SP_SEQNO",sharedPreferences.getString("SEQNO" ,""));
            //Log.d("SP_area_index", String.valueOf(sharedPreferences.getInt("area_index" ,0)));
//            Log.d("SP_LNG",sharedPreferences.getString("LNG" ,""));
//            Log.d("SP_LAT",sharedPreferences.getString("LAT" ,""));
//            Log.d("SP_QTY",sharedPreferences.getString("QTY" ,""));
//            Log.d("SP_O_TYPE",sharedPreferences.getString("O_TYPE" ,""));
//            Log.d("SP_O_WATTS",sharedPreferences.getString("O_WATTS" ,""));
//            Log.d("SP_N_TYPE",sharedPreferences.getString("N_TYPE" ,""));
//            Log.d("SP_N_WATTS",sharedPreferences.getString("N_WATTS" ,""));
        }
    };

    private void init() {
        sharedPreferences = getSharedPreferences("LastData" , MODE_PRIVATE);
        lampdetail=this.getIntent().getExtras().getStringArray("lampdetail");
        //Log.d("",lampdetail[0]);
        number = (EditText) findViewById(R.id.number);

        if(lampdetail[2]!=""){
            number.setText(lampdetail[2]);
        }
        areaSpinner = (Spinner) findViewById(R.id.area);
        amount = (EditText) findViewById(R.id.amount);
        oldPower = (EditText) findViewById(R.id.oldPower);
        newPower = (EditText) findViewById(R.id.newPower);
        userID = (TextView) findViewById(R.id.userID);
        time = (TextView) findViewById(R.id.time);
        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        oldType = (Spinner) findViewById(R.id.oldType);
        newType = (Spinner) findViewById(R.id.newType);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        photo3 = (ImageView) findViewById(R.id.photo3);
        photo4 = (ImageView) findViewById(R.id.photo4);
        photo5 = (ImageView) findViewById(R.id.photo5);
        getlocation = (Button) findViewById(R.id.getLocation);
        map = (Button) findViewById(R.id.map);
        submit = (Button) findViewById(R.id.submit);
        user = this.getIntent().getExtras().getString("userID");
        userID.setText(user);
        serverIP = this.getIntent().getExtras().getString("serverIP");
        SEQNO = this.getIntent().getExtras().getString("SEQNO");
        CLSEQNO = this.getIntent().getExtras().getString("CLSEQNO");
        CITYNO = this.getIntent().getExtras().getString("CITYNO");
        LampTypeArray = this.getIntent().getExtras().getStringArray("LampTypeArray");
        LampAllArray = this.getIntent().getExtras().getStringArray("LampAllArray");
        AREAlist = this.getIntent().getExtras().getStringArray("AREAlist");
        isLAST = this.getIntent().getExtras().getBoolean("isLast");

//        try {
//            Log.d("LampAllArray", new JSONObject(LampAllArray[0]).getString("LAMPNO"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        longitude.addTextChangedListener(onchange);
        number.addTextChangedListener(onchange);
        amount.addTextChangedListener(onchange);
        latitude.addTextChangedListener(onchange);
//        Log.d("LampAllArray", LampAllArray[3]);


        lampdetail = this.getIntent().getExtras().getStringArray("lampdetail");
        //test =this.getIntent().getExtras().getString("test");
//        try {
//            String aaa=new JSONObject(LampAllArray[0]).getString("LAMPNO");
//            Log.d("000",aaa);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        hash = MD5(String.valueOf(SEQNO) + String.valueOf(CLSEQNO) +CITYNO+getResources().getString(R.string.secret));
        hash2 = MD5(String.valueOf(SEQNO) + String.valueOf(CLSEQNO) +getResources().getString(R.string.secret));
//        Log.d("SEQNO",SEQNO);
//        Log.d("CLSEQNO",CLSEQNO);
//        Log.d("CITYNO",CITYNO);
        //Log.d("ALL", LampAllArray[0].split(","));
        try {
            if(SEQNO != "-1" && !isLAST)
            GetLightDetail();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("serverIP2", String.valueOf(serverIP));
        //Toast.makeText(getApplicationContext(),serverIP, Toast.LENGTH_SHORT).show();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        nowtime = sf.format(cal.getTime());
        time.setText(nowtime);
        areaSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.font, AREAlist));
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    areaSelect = AREAlist[position];
                    area_index = position;
                    Log.d("XXX","XXX");
                    sharedPreferences.edit().putInt("area_index" , position).apply();
//                    Log.d("SP_AREA",sharedPreferences.getString("AREA" ,""));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                areaSelect = AREAlist[0];
//                Log.d("SP_AREA",sharedPreferences.getString("AREA" ,""));

            }
        });


        oldType.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.font, LampTypeArray));

        oldType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oldTypeSelect = LampTypeArray[position];
                sharedPreferences.edit().putString("O_TYPE" , oldTypeSelect).apply();
                if(position>0){
                    O_LampIndex = position-1;
                    try {
                        oldPower.setText(new JSONObject(LampAllArray[O_LampIndex]).getString("WATTS"));
                        sharedPreferences.edit().putInt("otype_index" , position).apply();
                        sharedPreferences.edit().putString("O_WATTS" , String.valueOf(oldPower.getText())).apply();
//                        Log.d("SP_O_TYPE",sharedPreferences.getString("O_TYPE" ,""));
//                        Log.d("SP_O_WATTS",sharedPreferences.getString("O_WATTS" ,""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    oldPower.setText("");
                    sharedPreferences.edit().putString("O_WATTS" , String.valueOf(oldPower.getText())).apply();
//                    Log.d("SP_O_TYPE",sharedPreferences.getString("O_TYPE" ,""));
//                    Log.d("SP_O_WATTS",sharedPreferences.getString("O_WATTS" ,""));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                oldTypeSelect = LampTypeArray[0];
                sharedPreferences.edit().putString("O_TYPE" , oldTypeSelect).apply();
//                Log.d("SP_O_TYPE",sharedPreferences.getString("O_TYPE" ,""));
//                Log.d("SP_O_WATTS",sharedPreferences.getString("O_WATTS" ,""));
                //oldTypeSelect = String.valueOf(testlist.get(0));
                //Log.d("00",oldTypeSelect);
            }
        });

        newType.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.font, LampTypeArray));
        newType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTypeSelect = LampTypeArray[position];
                sharedPreferences.edit().putString("N_TYPE" , newTypeSelect).apply();
                if(position>0){
                    N_LampIndex = position-1;
                    try {
                        newPower.setText(new JSONObject(LampAllArray[N_LampIndex]).getString("WATTS"));
                        sharedPreferences.edit().putInt("ntype_index" , position).apply();
                        sharedPreferences.edit().putString("N_WATTS" , String.valueOf(newPower.getText())).apply();
//                        Log.d("SP_N_TYPE",sharedPreferences.getString("N_TYPE" ,""));
//                        Log.d("SP_N_WATTS",sharedPreferences.getString("N_WATTS" ,""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    newPower.setText("");
                    sharedPreferences.edit().putString("N_WATTS" , String.valueOf(newPower.getText())).apply();
//                    Log.d("SP_N_TYPE",sharedPreferences.getString("N_TYPE" ,""));
//                    Log.d("SP_N_WATTS",sharedPreferences.getString("N_WATTS" ,""));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                newTypeSelect = LampTypeArray[0];
                sharedPreferences.edit().putString("N_TYPE" , newTypeSelect).apply();
//                Log.d("SP_N_TYPE",sharedPreferences.getString("N_TYPE" ,""));
//                Log.d("SP_N_WATTS",sharedPreferences.getString("N_WATTS" ,""));
            }
        });

        //GoogleAPI
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    private void setClickListener() {
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.connect();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lng = String.valueOf(longitude.getText());
                lat = String.valueOf(latitude.getText());
                Intent intent = new Intent(dataEdit.this,MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("loclat", lat);
                bundle.putString("loclng", lng);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(getImageByCamera, 1);

            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(getImageByCamera, 2);
            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(getImageByCamera, 3);
            }
        });

        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(getImageByCamera, 4);
            }
        });

        photo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(getImageByCamera, 5);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          if (number.getText().toString().trim().length() != 0) {
                                              try {
                                                  SLNO = String.valueOf(number.getText());
                                                  AREA = areaSelect;
                                                  O_KINDSEQNO = new JSONObject(LampAllArray[O_LampIndex]).getString("LAMPNO");
                                                  N_KINDSEQNO = new JSONObject(LampAllArray[N_LampIndex]).getString("LAMPNO");
                                                  QTY = String.valueOf(amount.getText());
                                                  LAMPNO_PICS= SLNO+"_1.jpg";
                                                  B_PICS= SLNO+"_2.jpg";
                                                  I_PICS= SLNO+"_3.jpg";
                                                  A_PICS= SLNO+"_4.jpg";
                                                  P_PICS= SLNO+"_5.jpg";
                                                  CITYNO= "1";
                                                  uploadData();
                                              } catch (JSONException e) {
                                                  e.printStackTrace();
                                              }
                                          }else {
                                              Toast.makeText(getApplicationContext(),"請輸入路燈編號", Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                  }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK && requestCode==0 ){
            lng = data.getExtras().getString("fixlng");
            lat = data.getExtras().getString("fixlat");
            longitude.setText(lng);
            latitude.setText(lat);
        }
        if (resultCode == RESULT_OK && requestCode == 1) {

                bitmap = data.getExtras().getParcelable("data");
                LAMPNO_PICS_FILE = imgconvert.convert(bitmap);
                sharedPreferences.edit().putString("LAMPNO_PICS_FILE" , LAMPNO_PICS_FILE).apply();
                photo1.setImageBitmap(bitmap);

        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            bitmap = data.getExtras().getParcelable("data");
            B_PICS_FILE = imgconvert.convert(bitmap);
            sharedPreferences.edit().putString("B_PICS_FILE" , B_PICS_FILE).apply();
            photo2.setImageBitmap(bitmap);
        }
        if (resultCode == RESULT_OK && requestCode == 3) {
            bitmap = data.getExtras().getParcelable("data");
            I_PICS_FILE = imgconvert.convert(bitmap);
            sharedPreferences.edit().putString("I_PICS_FILE" , I_PICS_FILE).apply();
            photo3.setImageBitmap(bitmap);
        }
        if (resultCode == RESULT_OK && requestCode == 4) {
            bitmap = data.getExtras().getParcelable("data");
            A_PICS_FILE = imgconvert.convert(bitmap);
            sharedPreferences.edit().putString("A_PICS_FILE" , A_PICS_FILE).apply();
            photo4.setImageBitmap(bitmap);
        }
        if (resultCode == RESULT_OK && requestCode == 5) {
            bitmap = data.getExtras().getParcelable("data");
            P_PICS_FILE = imgconvert.convert(bitmap);
            sharedPreferences.edit().putString("P_PICS_FILE" , P_PICS_FILE).apply();
            photo5.setImageBitmap(bitmap);
        }


    }
    protected void uploadData() throws JSONException{
        RequestQueue mQuene = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://"+serverIP+ getResources().getString(R.string.url_Upload)+getResources().getString(R.string.key)+"&SEQNO="+SEQNO+"&CLSEQNO="+CLSEQNO+"&CITYNO="+CITYNO+"&hash="+hash ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        json_obj = response;
                        try {
                            status = new JSONObject(String.valueOf(json_obj)).getString("status");
                            Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                            Log.d("RE",response);
                            Log.d("Detail", String.valueOf(param2));
                            if(status.equals("success")){
                                Toast.makeText(getApplicationContext(), "上傳成功", Toast.LENGTH_SHORT).show();
                                //Log.d("SUCCESS","上傳成功");
                                Intent intent = new Intent(dataEdit.this,menu.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("refresh","Yes");
//                                intent.putExtras(bundle);
                                setResult(RESULT_OK,intent);
//                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"上傳失敗", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

//                        if (response.equals("OK")) {
//                            Toast.makeText(getApplicationContext(), "上傳成功", Toast.LENGTH_SHORT).show();
//                            number.setText("");
//                            Calendar cal = Calendar.getInstance();
//                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            nowtime = sf.format(cal.getTime());
//                            time.setText(nowtime);
//                            latitude.setText("");
//                            lat = "";
//                            longitude.setText("");
//                            lng = "";
//                            address.setText("");
//                            amount.setText("");
//                            oldType.setSelection(0);
//                            oldTypeSelect = oldTypeArray[0];
//                            oldPower.setText("");
//                            newType.setSelection(0);
//                            newTypeSelect = newTypeArray[0];
//                            newPower.setText("");
//                            photo1.setImageResource(android.R.drawable.ic_menu_camera);
//                            LAMPNO_PICS_FILE = "";
//                            photo2.setImageResource(android.R.drawable.ic_menu_camera);
//                            B_PICS_FILE = "";
//                            photo3.setImageResource(android.R.drawable.ic_menu_camera);
//                            I_PICS_FILE = "";
//                            photo4.setImageResource(android.R.drawable.ic_menu_camera);
//                            A_PICS_FILE = "";
//                            photo5.setImageResource(android.R.drawable.ic_menu_camera);
//                            P_PICS_FILE = "";
//                            SEQNO = "0";
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("USERID",user);
                param.put("SEQNO", SEQNO);
                param.put("CLSEQNO",CLSEQNO);
                param.put("SLNO",SLNO);
                param.put("CITYNO",CITYNO);
                param.put("AREA",AREA);
                param.put("O_KINDSEQNO",O_KINDSEQNO);
                param.put("N_KINDSEQNO",N_KINDSEQNO);
                param.put("LNG",lng);
                param.put("LAT",lat);
                param.put("QTY",QTY);
                param.put("LAMPNO_PICS",LAMPNO_PICS);//燈牌照
                if(LAMPNO_PICS_FILE!= null)
                param.put("LAMPNO_PICS_FILE",LAMPNO_PICS_FILE);
                param.put("B_PICS",B_PICS);//換裝前
                if(B_PICS_FILE!= null)
                param.put("B_PICS_FILE",B_PICS_FILE);
                param.put("I_PICS",I_PICS);//換裝中
                if(I_PICS_FILE!= null)
                param.put("I_PICS_FILE",I_PICS_FILE);
                param.put("A_PICS",A_PICS);//換裝後
                if(A_PICS_FILE!= null)
                param.put("A_PICS_FILE",A_PICS_FILE);
                param.put("P_PICS",P_PICS);//電器盒
                if(P_PICS_FILE!= null)
                param.put("P_PICS_FILE",P_PICS_FILE);

//                Log.d("WORKCLASS",user);
//                Log.d("SEQNO", SEQNO);
//                Log.d("CLSEQNO",CLSEQNO);
//                Log.d("CITYNO",CITYNO);
//                Log.d("AREA",AREA);
//                Log.d("O_KINDSEQNO",O_KINDSEQNO);
//                Log.d("N_KINDSEQNO",N_KINDSEQNO);
//                Log.d("LNG",lng);
//                Log.d("LAT",lat);
//                Log.d("QTY",QTY);
//                Log.d("LAMPNO_PICS",LAMPNO_PICS);//燈牌照
//                Log.d("LAMPNO_PICS_FILE",LAMPNO_PICS_FILE);
//                Log.d("B_PICS",B_PICS);//換裝前
//                Log.d("B_PICS_FILE",B_PICS_FILE);
//                Log.d("I_PICS",I_PICS);//換裝中
//                Log.d("I_PICS_FILE",I_PICS_FILE);
//                Log.d("A_PICS",A_PICS);//換裝後
//                Log.d("A_PICS_FILE",A_PICS_FILE);
//                Log.d("P_PICS",P_PICS);//電器盒
//                Log.d("P_PICS_FILE",P_PICS_FILE);

                return param;

            }
        };
        mQuene.add(stringRequest);
    }
    protected void GetLightDetail() throws JSONException {
        RequestQueue mQuene = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://"+serverIP+ getResources().getString(R.string.light_Detail) +getResources().getString(R.string.key)+"&SEQNO="+SEQNO+"&CLSEQNO="+CLSEQNO+"&hash="+hash2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json_obj = response;
                        //Log.d("00","&SEQNO="+SEQNO+"&CLSEQNO="+CLSEQNO+"&hash="+hash2);
                        try {
                            status = new JSONObject(json_obj).getString("status");
                            Log.d("R",response);
                            if(status.equals("success")) {
                                detail =new JSONObject(new JSONObject(json_obj).getString("data"));
                                longitude.setText(detail.getString("LNG"));
                                latitude.setText(detail.getString("LAT"));
                                amount.setText(detail.getString("QTY"));
                                O_KINDSEQNO = detail.getString("O_KINDSEQNO");
                                LAMPNO_PICS = detail.getString("LAMPNO_PICS");
                                B_PICS = detail.getString("B_PICS");
                                I_PICS = detail.getString("I_PICS");
                                A_PICS = detail.getString("A_PICS");
                                P_PICS = detail.getString("P_PICS");
                                oldPower.setText(detail.getString("WATTS"));
                                //Log.d("DATA", String.valueOf(detail));
                                for(int i=0;i<LampAllArray.length;i++){
                                    if(O_KINDSEQNO.equals(new JSONObject(LampAllArray[i]).getString("LAMPNO"))){
                                        oldType.setSelection(i+1);
                                        //oldTypeSelect=new JSONObject(LampAllArray[i]).getString("LAMPNO");
                                    }
                                }
                                //url_photo = LAMPNO_PICS;
                                //Log.d("XXX",url_photo);
                                start_DL();



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


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            if (isLAST){
                longitude.setText(this.getIntent().getExtras().getString("LNG"));
                latitude.setText(this.getIntent().getExtras().getString("LAT"));
            }else {
                lng = String.valueOf(mLocation.getLongitude());
                lat = String.valueOf(mLocation.getLatitude());
                longitude.setText(lng);
                latitude.setText(lat);
            }

        } else
            Toast.makeText(getApplicationContext(), "定位失敗", Toast.LENGTH_SHORT).show();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(this)
                    .setTitle("確定返回?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                }
                            }).show();
        }
        return true;
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

