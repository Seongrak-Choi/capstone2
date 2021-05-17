package com.example.capstone2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static NaverMap naverMap;
    String latitude;
    String longtitude;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        latitude = (String) intent.getSerializableExtra("latitude");
        longtitude = (String) intent.getSerializableExtra("longtitude");

    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

    }

}
