package com.example.fyp.Company;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CompanySuspendedDrivers extends Fragment {

    RecyclerView recyclerView;
    ArrayList<CompanyAllCustomersModel> list;

    public CompanySuspendedDrivers() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_driver_requests, container, false);
        list=new ArrayList<>();

        getUnregiteredDrivers();

        recyclerView=view.findViewById(R.id.companyDriverRecycler);
        LinearLayoutManager ll=new LinearLayoutManager(this.getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(ll);


        return view;
    }

    public void getUnregiteredDrivers(){

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot dd : dataSnapshot.getChildren()) {
                        String c = dd.child("suspended").getValue(String.class);
                        if (c.equals("yes")) {
                            Log.d("xyz", dd.child("name").getValue(String.class));
                            String dId = dd.getKey();
                            String name = dd.child("name").getValue(String.class);
                            String image = dd.child("image").getValue(String.class);
                            String mobile = dd.child("mobile").getValue(String.class);
                            list.add(new CompanyAllCustomersModel(name, mobile, image, dId));
                            CompanySuspendedDriversAdapter Adapter = new CompanySuspendedDriversAdapter(getActivity(), list);
                            recyclerView.setAdapter(Adapter);
                        }
                    }



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


}
