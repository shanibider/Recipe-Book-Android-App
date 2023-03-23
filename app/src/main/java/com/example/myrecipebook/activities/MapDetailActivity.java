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

// retrieves the details of a location from a Firebase Realtime Database and displays them
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

        //retrieves any extras that were passed to the activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //and updates the views with the markerTitle value
            String markerTitle = extras.getString("markerTitle");
            storeTitle.setText(markerTitle);
            updateTexts(markerTitle);
        }
    }

    //retrieves data from a Firebase Realtime Database using a DatabaseReference
    private void updateTexts(String markerTitle)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("locations");

        //adds valueEventListener to the reference, which listens for changes to the data at the location referenced by markerTitle
        reference.child(markerTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //the onDataChange() method is called with a DataSnapshot containing the updated data
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The method then extracts the address and hours from the LocationMarker object and updates the corresponding views
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



/*
Bundle-
used to store and transfer data between activities, fragments, and services within an app. It's a way to pass data from one component of the app to another without having to rely on global variables.
A bundle can be used to store any type of data that can be serialized and deserialized.
To create a bundle, you can use the putExtra() method on an Intent object, which is typically used to start a new activity or service.
In the receiving activity, you can retrieve the data from the bundle using the getXXX().

For example:
// Get the Bundle object from the Intent
Bundle bundle = getIntent().getExtras();

// Get the data from the Bundle
String value1 = bundle.getString("key1");
int value2 = bundle.getInt("key2");

*/