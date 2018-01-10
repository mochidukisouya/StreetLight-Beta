package com.example.streetlight;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.util.ArrayList;
/**
 * Created by Mochiduki on 2018/1/10.
 */

public class menu_2 extends AppCompatActivity {
    TextView Last_textview,Un_textView;
    ListView Last_list_view,list_view;
    private ArrayList<String> lightlist = new ArrayList<>();
    String[] lampdetail={"0","1","2","3","4","5","6","7","8","9"};
    JSONArray LIST;
    private ArrayAdapter<String> listAdapter;

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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_2);
        Last_textview= (TextView)findViewById(R.id.Last_textView);
        Un_textView = (TextView)findViewById(R.id.Un_textView);
        Last_list_view = (ListView)findViewById(R.id.Last_list_view);
        list_view = (ListView)findViewById(R.id.list_view);
        lampdetail=this.getIntent().getExtras().getStringArray("lampdetail");
        for (int i =0;i<lampdetail.length;i++){
            Log.d("lampdetail",lampdetail[i]);
        }
        addlist();

    }
    public void addlist(){
        Last_list_view.setAdapter(null);
        lightlist.clear();
//        for (int i = 0; i < LIST.length(); i++) {
//            try {
//                lampdetail[2] = String.valueOf(LIST.getJSONObject(i).getString("SLNO"));
//                lampdetail[7] = String.valueOf(LIST.getJSONObject(i).getString("AREA"));
//                lampdetail[8] = String.valueOf(LIST.getJSONObject(i).getString("LAMPSTYLE"));
//                lampdetail[9] = String.valueOf(LIST.getJSONObject(i).getString("WATTS"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            lightlist.add("路燈編號:" + lampdetail[2] + "　地區:" + lampdetail[7] + "\n燈種:" + lampdetail[8] + "　瓦特數:" + lampdetail[9] + "W");
//        }
        lightlist.add("路燈編號:" + lampdetail[2] + "　地區:" + lampdetail[7] + "\n燈種:" + lampdetail[8] + "　瓦特數:" + lampdetail[9] + "W");
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
        Last_list_view.setAdapter(listAdapter);
        Last_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "你選擇的是" + lightlist.get(position), Toast.LENGTH_SHORT).show();

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
                        //SEQNO = lampdetail[0];
                        //CLSEQNO = lampdetail[1];

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }
        });
    }

}
