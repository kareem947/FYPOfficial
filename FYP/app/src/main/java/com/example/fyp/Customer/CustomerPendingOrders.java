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
public class CustomerPendingOrders extends Fragment {


    private RecyclerView recyclerView;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_pending_orders, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView=view.findViewById(R.id.customerPendingOrderRecyclerView);
        final FragmentActivity c = getActivity();

        LinearLayoutManager ll=new LinearLayoutManager(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(ll);

        Log.d("nothing","aiwen");


        final ArrayList<CustomerPendingOrderModel> list = new ArrayList<>();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Requests").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String name = dataSnapshot1.child("customerName").getValue(String.class);
                        String number = dataSnapshot1.child("customerNumber").getValue(String.class);
                        String orderName= dataSnapshot1.child("orderId").getValue(String.class);
                        String requestId=dataSnapshot1.getKey();
                        list.add(new CustomerPendingOrderModel(name,number,orderName,uid,requestId));

                    }
                    CustomerPendingOrderAdapter adapter = new CustomerPendingOrderAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



       /* databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Log.d("nothing",data.getKey());

                        DatabaseReference gotoToWorkingRequests = FirebaseDatabase.getInstance().getReference().child("Requests").child(data.getKey());
                        gotoToWorkingRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot data) {
                                if (data.exists()){

                                    String dname= data.child("driverName").getValue(String.class);
                                    String dImage= data.child("driverImage").getValue(String.class);
                                    String dMobile= data.child("driverMobile").getValue(String.class);
                                    list.add(new CustomerAcceptedOrderModel(dname,dImage,dMobile));


                                }
                                Log.d("nothingis",list.get(0).getDriverName()+list.get(0).getDriverMobile()+list.get(0).getImageView());
                                CustomerAcceptedOrderAdapter adapter = new CustomerAcceptedOrderAdapter(getActivity(), list);
                                recyclerView.setAdapter(adapter);
*//*
                                recyclerView.setHasFixedSize(true);
*//*
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
        });*/


        return view;
    }

}
