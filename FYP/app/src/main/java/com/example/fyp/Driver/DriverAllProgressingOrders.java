package com.example.fyp.Driver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fyp.Customer.CustomerAcceptedOrderAdapter;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverAllProgressingOrders extends Fragment {


    private RecyclerView recyclerView;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_all_progressing_orders, container, false);

        recyclerView=view.findViewById(R.id.driverProgressingOrdersRecyclerView);
        final FragmentActivity c = getActivity();
        LinearLayoutManager ll=new LinearLayoutManager(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(ll);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final ArrayList<DriverAllProgressingOrdersModel> list = new ArrayList<>();

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid).child("working");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (isAdded()){
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot data : dataSnapshot.getChildren()) {
                        final String requestId = data.getKey();

                        DatabaseReference gotoToWorkingRequests = FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(data.getKey());
                        gotoToWorkingRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnap) {
                                if (datasnap.exists()) {

                                    String cname = datasnap.child("customerName").getValue(String.class);
                                    String cImage = datasnap.child("customerImage").getValue(String.class);
                                    String cMobile = datasnap.child("customerNumber").getValue(String.class);
                                    String pickup = datasnap.child("pickup").getValue(String.class);
                                    String dropoff = datasnap.child("delivery").getValue(String.class);
                                    String customerId = datasnap.child("customerId").getValue(String.class);
                                    Log.d("everyThing", cname + "     " + cImage + "     " + cMobile + "     " + pickup + "     " + dropoff);
                                    list.add(new DriverAllProgressingOrdersModel(cname, cImage, cMobile, requestId, customerId, pickup, dropoff));
                                    DriverAllProgressingOrdersAdapter adapter = new DriverAllProgressingOrdersAdapter(getActivity(), list);
                                    recyclerView.setAdapter(adapter);

                                }
                                else {
                                        databaseReference.child(data.getKey()).removeValue();
                                }

/*
                                recyclerView.setHasFixedSize(true);
*/
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return view;
    }

}
