package com.example.myrecipebook.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.myrecipebook.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng tlv_port = new LatLng(32.104957, 34.777859);
        googleMap.addMarker(new MarkerOptions()
                .position(tlv_port)
                .title("TLV Port Supermarket"));

        LatLng tlv_king_george = new LatLng(32.07780312815079, 34.77811656159381);
        googleMap.addMarker(new MarkerOptions()
                .position(tlv_king_george)
                .title("TLV King George Supermarket"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tlv_port, 15));
    }
}
