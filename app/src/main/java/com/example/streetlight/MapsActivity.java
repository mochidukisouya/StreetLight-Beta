package com.example.streetlight;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String[] location;
    String lat,lng;
    Button BTN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lat = this.getIntent().getExtras().getString("loclat");
        lng= this.getIntent().getExtras().getString("loclng");
        BTN = (Button)findViewById(R.id.BTN);
        setClickListener();
    }
    private void setClickListener() {
        BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,dataEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("fixlat", lat);
                bundle.putString("fixlng", lng);
//                bundle.putString("loclat", loclat);
//                bundle.putString("loclng", loclng);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(Double.valueOf(lat),Double.valueOf(lng));
        mMap.addMarker(new MarkerOptions().position(sydney).title(lng+","+lat)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Add a marker in Sydney and move the camera
   //     final MarkerOptions markerOptions= new MarkerOptions();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                location=latLng.toString().split(",");
                location[0]=location[0].split("\\(")[1];
                location[1]=location[1].split("\\)")[0];
                lat=location[0].split("\\.")[0]+"."+location[0].split("\\.")[1].substring(0,6);
                lng=location[1].split("\\.")[0]+"."+location[1].split("\\.")[1].substring(0,6);

                //LatLng sydney = new LatLng(Double.valueOf(lat),Double.valueOf(lng));
                //mMap.addMarker(new MarkerOptions().position(sydney).title(lat+","+lng));
                final LatLng PERTH = new LatLng(Double.valueOf(lat),Double.valueOf(lng));
                mMap.addMarker(new MarkerOptions().position(PERTH).title(lng+","+lat)).showInfoWindow();


                //mMap.addMarker(new MarkerOptions().position(PERTH).draggable(true).title(lat+","+lng));



                //mMap.moveCamera(CameraUpdateFactory.newLatLng(PERTH));

            }


        });


    }

}
