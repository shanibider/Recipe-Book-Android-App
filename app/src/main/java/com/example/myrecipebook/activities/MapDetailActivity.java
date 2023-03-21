package com.example.myrecipebook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.LocationMarker;
import com.example.myrecipebook.models.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapDetailActivity extends AppCompatActivity {

    TextView storeTitle;
    TextView address;
    TextView hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);

        storeTitle = findViewById(R.id.titleNameStore);
        address = findViewById(R.id.AdrressName);
        hours = findViewById(R.id.StoreHours);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String markerTitle = extras.getString("markerTitle");
            storeTitle.setText(markerTitle);
            updateTexts(markerTitle);
        }
    }

    private void updateTexts(String markerTitle)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("locations");
        reference.child(markerTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LocationMarker locationMarker = dataSnapshot.getValue(LocationMarker.class);
                if(locationMarker != null)
                {
                    address.setText(locationMarker.getAddress());
                    hours.setText(locationMarker.getHours());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}