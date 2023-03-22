package com.example.myrecipebook.ui.slideshow;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myrecipebook.MainActivity;
import com.example.myrecipebook.R;
import com.example.myrecipebook.activities.EditProfileActivity;
import com.example.myrecipebook.activities.WelcomeActivity;
import com.example.myrecipebook.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    TextView profileName, profileUsername ,profileEmail;
    Button editProfile;
    ImageView userImage;

    @Override
    public void onResume() {
        super.onResume();

        UpdateUserData();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.activity_profile, container, false);

        profileEmail = root.findViewById(R.id.profileEmail);
        profileName = root.findViewById(R.id.profileName);
        profileUsername = root.findViewById(R.id.profileUsername);
        userImage = root.findViewById(R.id.user_image);


        editProfile = root.findViewById(R.id.edit_profile);




        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        UpdateUserData();

        return root;
    }

    void UpdateUserData()
    {
        auth = FirebaseAuth.getInstance();
        if(auth.getUid() != null)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("users");

            usersRef.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserData profileData = dataSnapshot.getValue(UserData.class);
                    if(profileData != null)
                    {
                        profileEmail.setText(profileData.getEmail());
                        profileName.setText(profileData.getName());
                        profileUsername.setText(profileData.getUsername());
                        if(!profileData.getProfileImage().isEmpty())
                        {
                            Picasso.get().load(profileData.getProfileImage()).into(userImage);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }

    }
}