package com.example.streetlight;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
///**
// * Created by Kuo on 2017/9/18.
// */
//
//public class dataSelectBackUp extends Activity {
//    ArrayList<Integer> idList = new ArrayList<>();
//    ArrayList<String> numberList = new ArrayList<>();
//    ArrayList<String> coorList = new ArrayList<>();
//    ArrayList<String> typeList = new ArrayList<>();
//    ArrayList<String> photoList = new ArrayList<>();
//    EditText number;
//    Spinner type;
//    String[] type_array = {"select", "type1", "type2", "type3"};
//    String type_select;
//    Button search;
//    RecyclerView mRecyclerView;
//    MyAdapter mAdapter;
//    TextView text;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dataselect);

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

//原創作者 軟貓軟體 http://toimy.blogspot.com/
public class dataSelectBackUp extends Activity  implements OnClickListener {
    LinearLayout layout;
    private Button btn1;
    private int CurrentButtonNumber = 0; //CurrentButtonNumber流水號 設定物件ID
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //先建立一個 面板放置所有元件
        layout= new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);  //面板的擺設方式為垂直
        btn1 = new Button(this);
        btn1.setId(CurrentButtonNumber);
        CurrentButtonNumber++;
        btn1.setText("SoftCat Go Button");
        btn1.setOnClickListener(this);    //如果要這樣寫 需加入 implements OnClickListener 於 Activity
        layout.addView(btn1, 300, 200); //addView(物件,寬度高度)
        setContentView(layout);   //設定畫面顯示自己的面板
    }
    public void onClick(View v)
    {
        switch(v.getId()){

            case 0://增加按鈕
                Button TmpBtn = new Button(getApplicationContext());
                TmpBtn.setText("基隆市");
                TmpBtn.setId(CurrentButtonNumber);
                TmpBtn.setOnClickListener(this);
                LinearLayout.LayoutParams param =new LinearLayout.LayoutParams(300,200);
                CurrentButtonNumber++;
                layout.addView(TmpBtn, param);
                break;
            default://其他按鈕
                alertbox("I be hit.....oh...","you hit me . I am "+ String.valueOf(v.getId()));
                break;
        }
    }
    //顯示對話方塊
    protected void alertbox(String title, String mymessage)
    {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){}
                        })
                .show();
    }
}



//        number = (EditText) findViewById(R.id.number);
//        type = (Spinner) findViewById(R.id.type);
//       type.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, type_array));
//       type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                type_select = type_array[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                type_select = "select";
//            }
//        });
//        text = (TextView) findViewById(R.id.text);
//                search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (number.getText().toString().trim().length() != 0 && type_select.equals("select")) {
//                    getData("number", number.getText().toString().trim());
//                } else if (number.getText().toString().trim().length() == 0 && !type_select.equals("select")) {
//                    getData("type", type_select);
//                } else if (number.getText().toString().trim().length() != 0 && !type_select.equals("select")) {
//                    getData("number", number.getText().toString().trim(), "type", type_select);
//                } else {
//                    Toast.makeText(getApplicationContext(), "請輸入搜尋條件", Toast.LENGTH_SHORT).show();
//                }
//                recy();
//            }
//        });
//    }
//
//    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public TextView id, num, coor, type;
//            public ImageView photo;
//            public Button edit,delete;
//            public ViewHolder(View v) {
//                super(v);
//                id=(TextView)v.findViewById(R.id.id);
//                num=(TextView)v.findViewById(R.id.number);
//                coor=(TextView)v.findViewById(R.id.coordinates);
//                type=(TextView)v.findViewById(R.id.type);
//                photo=(ImageView)v.findViewById(R.id.photo);
//                edit=(Button)v.findViewById(R.id.edit);
//                delete=(Button)v.findViewById(R.id.delete);
//            }
//        }
//
//        public MyAdapter() {
//
//        }
//
//        @Override
//        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item, parent, false);
//            ViewHolder vh = new ViewHolder(v);
//            return vh;
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            holder.id.setText("項目："+idList.get(position));
//            holder.num.setText("編號："+numberList.get(position));
//            holder.coor.setText("座標："+coorList.get(position));
//            holder.type.setText("類型："+typeList.get(position));
//            holder.photo.setImageBitmap(imgconvert.convert(photoList.get(position)));
//            holder.edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return idList.size();
//        }
//    }
//
//    public void recy() {
//        mAdapter = new MyAdapter();
//        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//        text.setText("");
//        if (idList.size() == 0) {
//            text.setText("目前搜尋不到紀錄");
//        }
//    }
//
//    public void getData(String key, String value) {
//        listClear();
//    }
//
//    public void getData(String key0, String value0, String key1, String vlaue1) {
//        listClear();
//    }
//
//    public void listClear() {
//        idList.clear();
//        numberList.clear();
//        coorList.clear();
//        typeList.clear();
//        photoList.clear();
//    }
//}
