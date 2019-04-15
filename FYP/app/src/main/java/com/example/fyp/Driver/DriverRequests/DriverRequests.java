package com.example.fyp.Driver.DriverRequests;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverRequests extends Fragment {

    private RecyclerView recyclerView;

    ArrayList<String> list;
    String image="default";
     ArrayList<Requestmodel> listt;


    ArrayList<String> list1;
    ArrayList<String> list2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);

        View view=inflater.inflate(R.layout.activity_driver_requests,container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        list1=new ArrayList<>();
        list2=new ArrayList<>();

        final FragmentActivity c = getActivity();

        LinearLayoutManager ll=new LinearLayoutManager(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(ll);



        getAssignedCustomer();



        return view;
    }


    public void getAssignedCustomer(){
        final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference gotoCustomerIds=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid).child("customerIds");
        gotoCustomerIds.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                list = new ArrayList<>();
                listt = new ArrayList<>();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    Log.d("ooperwala1", dataSnapshot1.getKey());

                    DatabaseReference gotoRequests = FirebaseDatabase.getInstance().getReference().child("Requests").child(dataSnapshot1.child("requestId").getValue().toString()).child(dataSnapshot1.getKey());
                    gotoRequests.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot data) {
                            if (isAdded()) {
                                // DataSnapshot dataSnapshot2=dataSnapshot;
                                if (data.exists()) {
                                /*
                                list.add(dataSnapshot1.getKey());
                                Log.d("ooperwala",data.getKey());*/


                                    final String name = data.child("customerName").getValue().toString();
                                    Log.d("ooperwala1", data.child("customerName").getValue().toString() + "Kareeeeeem");

                                    final String pick = data.child("pickup").getValue().toString();
                                    final String drop = data.child("delivery").getValue().toString();
                                    String paymentMethod = data.child("paymentMethod").getValue().toString();
                                    if (paymentMethod.equals("10")) {
                                        paymentMethod = "Cash Payment";
                                    } else {
                                        paymentMethod = "Online Payment";
                                    }
                                    final String requestId = dataSnapshot1.getKey();
                                    final String id = dataSnapshot1.child("requestId").getValue().toString();
                                    Log.d("Recycler", name);

                                    DatabaseReference xyz = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(id);
                                    final String finalPaymentMethod = paymentMethod;
                                    xyz.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (isAdded()){
                                                if (dataSnapshot.exists()) {
                                                    image = dataSnapshot.child("image").getValue(String.class);
                                                    if (image.equals("") || name.equals("") || pick.equals("") || drop.equals("") || requestId.equals("") || finalPaymentMethod.equals("")) {
                                                        return;
                                                    } else {
                                                        listt.add(new Requestmodel(name, pick, drop, id, requestId, image, finalPaymentMethod));
                                                        requestadapter myadapter = new requestadapter(getActivity(), listt);
                                                        recyclerView.setAdapter(myadapter);
                                                        recyclerView.setHasFixedSize(true);
                                                    }
                                                }
                                        }
                                    }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    Log.d("EveryThing", name + pick + drop + paymentMethod + image);

/*
                                listt.add(new Requestmodel(name, pick, drop, id, image, paymentMethod));*/

                                } else {
                                    gotoCustomerIds.child(dataSnapshot1.getKey()).removeValue();
                                }
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });

                }
            }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

