package com.example.fyp.Customer;


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

import com.example.fyp.Customer.CustomerAcceptedOrderModel;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class customerAcceptedOrders extends Fragment {

    private RecyclerView recyclerView;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_accepted_orders, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView=view.findViewById(R.id.customerAcceptedOrderRecyclerView);
        final FragmentActivity c = getActivity();

        LinearLayoutManager ll=new LinearLayoutManager(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(ll);

        Log.d("nothing","aiwen");


        final ArrayList<CustomerAcceptedOrderModel> list = new ArrayList<>();

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("MyDrivers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()){
                    list.clear();
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot data : dataSnapshot.getChildren()) {
                        final String requestId = data.getKey();

                        DatabaseReference gotoToWorkingRequests = FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(data.getKey());
                        gotoToWorkingRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnap) {
                                list.clear();
                                if (isAdded()) {
                                    if (datasnap.exists()) {

                                        String dname = datasnap.child("driverName").getValue(String.class);
                                        String dImage = datasnap.child("driverImage").getValue(String.class);
                                        String dMobile = datasnap.child("driverMobile").getValue(String.class);
                                        String driverId = datasnap.child("driverId").getValue(String.class);
                                        String orderName = datasnap.child("orderId").getValue(String.class);
                                        Log.d("MyCheck", requestId + "     " + driverId);
                                        list.add(new CustomerAcceptedOrderModel(dname, dImage, dMobile, requestId, driverId, orderName));
                                        CustomerAcceptedOrderAdapter adapter = new CustomerAcceptedOrderAdapter(getActivity(), list);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        databaseReference.child(data.getKey()).removeValue();
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
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
