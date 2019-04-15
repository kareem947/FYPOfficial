package com.example.fyp.Driver.DriverRequests;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.fyp.Customer.CustomerCompletedOrderAdapter;
import com.example.fyp.Customer.CustomerCompletedOrderModel;
import com.example.fyp.Driver.DriverCompletedOrderAdapter;
import com.example.fyp.Driver.DriverCompletedOrderModel;
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

public class DriverCompleteOrders extends Fragment {

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_complete_orders, container, false);








        recyclerView=view.findViewById(R.id.DriverCompletedOrderRecycler);
        final FragmentActivity c = getActivity();

        LinearLayoutManager ll=new LinearLayoutManager(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(ll);




        final ArrayList<DriverCompletedOrderModel> list = new ArrayList<>();

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("DriverCompleted").child(uid);
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
                                        String cname = datasnap.child("customerName").getValue(String.class);
                                        String cImage = datasnap.child("customerImage").getValue(String.class);
                                        String time=getDate((datasnap.child("timeStamp").getValue(Long.class)));
                                        Log.d("timeStamp",time);
                                        String cmobile = datasnap.child("customerNumber").getValue(String.class);

                                        list.add(new DriverCompletedOrderModel(cname, cImage, requestId, cmobile, time));
                                        DriverCompletedOrderAdapter adapter = new DriverCompletedOrderAdapter(getActivity(), list);
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
