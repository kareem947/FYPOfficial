package com.example.fyp.Customer;


import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCompletedOrders extends Fragment {

    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_completed_orders, container, false);

        recyclerView=view.findViewById(R.id.customerCompletedOrderRecycler);
        final FragmentActivity c = getActivity();

        LinearLayoutManager ll=new LinearLayoutManager(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(ll);




        final ArrayList<CustomerCompletedOrderModel> list = new ArrayList<>();

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("CustomerCompleted").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()){
                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot data : dataSnapshot.getChildren()) {
                            final String requestId = data.getKey();

                            DatabaseReference gotoCompletedOrders = FirebaseDatabase.getInstance().getReference().child("CompletedRequests").child(data.getKey());
                            gotoCompletedOrders.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnap) {
                                    if (datasnap.exists()) {

                                        Log.d("timeStampppp",datasnap.child("timeStamp").getValue(Long.class).toString());
                                        String dname = datasnap.child("driverName").getValue(String.class);
                                        String dImage = datasnap.child("driverImage").getValue(String.class);
                                        String time=getDate((datasnap.child("timeStamp").getValue(Long.class)));
                                        Log.d("timeStamp",time);
                                        String orderName = datasnap.child("orderId").getValue(String.class);

                                            list.add(new CustomerCompletedOrderModel(dname, dImage, requestId, orderName, time));
                                            CustomerCompletedOrderAdapter adapter = new CustomerCompletedOrderAdapter(getActivity(), list);
                                            recyclerView.setAdapter(adapter);

                                    }
                                    else{
                                        databaseReference.child(data.getKey()).removeValue();
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


    public String getDate(Long time){
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(time*1000);

        String date= DateFormat.format("dd-MM-yyyy  hh:mm",calendar).toString();

        return date;
    }

}
