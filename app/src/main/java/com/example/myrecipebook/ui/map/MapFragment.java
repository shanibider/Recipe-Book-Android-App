package com.example.myrecipebook.ui.map;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myrecipebook.R;
import com.example.myrecipebook.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    FloatingActionButton fab;

    //6.Attach the Adapter
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_activity);
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng tlv_port = new LatLng(32.104957, 34.777859);
        googleMap.addMarker(new MarkerOptions()
                .position(tlv_port)
                .title("TLV Port Supermarket"));

        LatLng tlv_king_george = new LatLng(32.07780312815079, 34.77811656159381);
        googleMap.addMarker(new MarkerOptions()
                .position(tlv_king_george)
                .title("TLV King George Supermarket"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tlv_port, 13));
    }
}

