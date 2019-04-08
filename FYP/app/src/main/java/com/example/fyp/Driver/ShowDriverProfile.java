package com.example.fyp.Driver;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.fyp.Driver.Setting;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;



import de.hdodenhof.circleimageview.CircleImageView;

public class  ShowDriverProfile extends Fragment {
    private TextView nameText, phoneText, dlText, cnicText;
    CircleImageView imageView;
    private StorageReference mStorageReference;
    private RadioButton smallPickp, largeTruck, mediumTruck;
    Button editBtn;

    private static final int GALLERY_PICK = 1;
    Uri filePath;
    String uid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);

        View view=inflater.inflate(R.layout.activity_show_driver_profile,container, false);

        nameText = view.findViewById(R.id.dSettingName);
        phoneText = view.findViewById(R.id.dSettingPhone);
        dlText = view.findViewById(R.id.dSettingLicense);
        cnicText = view.findViewById(R.id.dSettingCnic);
        imageView = view.findViewById(R.id.showprofile_image);
        editBtn=view.findViewById(R.id.editProfile);


        smallPickp =view.findViewById(R.id.dsettingSmallPickup);
        largeTruck = view.findViewById(R.id.dsettingLargeTruck);
        mediumTruck = view.findViewById(R.id.dsettingMediumTruck);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageReference= FirebaseStorage.getInstance().getReference();

        //final String id = getIntent().getStringExtra("user_id");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameText.setText(dataSnapshot.child("name").getValue(String.class));
                phoneText.setText(dataSnapshot.child("mobile").getValue(String.class));
                dlText.setText(dataSnapshot.child("license").getValue(String.class));
                cnicText.setText(dataSnapshot.child("cnic").getValue(String.class));
                String type=dataSnapshot.child("truckType").getValue(String.class);
                if (type.equals("1")){
                    smallPickp.setChecked(true);
                }
                if (type.equals("2")){
                    mediumTruck.setChecked(true);
                }
                if (type.equals("3")){
                    largeTruck.setChecked(true);
                }
                String image = dataSnapshot.child("image").getValue().toString();

                //String s = dataSnapshot.child("image").getValue(String.class);
                if (!image.equals("default")) {
                    Picasso.get().load(image).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });
        return view;

    }

}


