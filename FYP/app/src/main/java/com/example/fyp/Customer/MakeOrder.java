package com.example.fyp.Customer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fyp.Customer.AddItem.AddItem;
import com.example.fyp.Customer.AddItem.ItemAdapter;
import com.example.fyp.Customer.AddItem.ItemModel;
import com.example.fyp.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MakeOrder extends Fragment implements View.OnClickListener, Serializable {

    private Button pickUpBtn;
    private Button dropOffBtn;
    private Button bookOrder;
    private Button addItemBtn;
    String pickupAddress;
    String deliveryAddress;
    Button track;
    RecyclerView recyclerView;
    EditText customerName, customerCnic, customerNumber, rcvrName, rcvrCnic, rcvrNumber,orderName;

    double pickupLat, pickUpLon, dropOffLat, dropOffLon;

    private final static int PICKUP_PLACE_PICKER_REQUEST = 999;
    private final static int DROPOFF_PLACE_PICKER_REQUEST = 998;
    Button btnDatePicker, btnTimePicker;
    TextView txtDate, txtTime;
    final private int SECOND_ACTIVITY_REQUEST_CODE = 50;

    private int mYear, mMonth, mDay, mHour, mMinute;
    String itemName;
    private RadioButton smallPickp, largeTruck, mediumTruck, cash , online;
    private int truckType = 0, paymnetMethod= 0;

    String cusName,cusImage,cusMobile,cusCnic;


    String quantity;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_make_order, container, false);

        pickUpBtn = view.findViewById(R.id.pickup_btn);
        dropOffBtn = view.findViewById(R.id.dropoff_btn);

        smallPickp =view.findViewById(R.id.dsettingSmallPickup);
        largeTruck = view.findViewById(R.id.dsettingLargeTruck);
        mediumTruck = view.findViewById(R.id.dsettingMediumTruck);
        cash=view.findViewById(R.id.cashPayment);
        online= view.findViewById(R.id.onlinePayment);
        customerName = view.findViewById(R.id.customerName);
        customerCnic = view.findViewById(R.id.customerCnic);
        customerNumber = view.findViewById(R.id.customerNumber);
        orderName = view.findViewById(R.id.orderName);
        recyclerView = view.findViewById(R.id.itemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pickUpBtn=view.findViewById(R.id.pickup_btn);
        dropOffBtn=view.findViewById(R.id.dropoff_btn);


        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference getCustomerInfo= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid);

        getCustomerInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    cusName=dataSnapshot.child("name").getValue(String.class);
                    cusImage=dataSnapshot.child("image").getValue(String.class);
                    cusMobile=dataSnapshot.child("mobile").getValue(String.class);
                    cusCnic=dataSnapshot.child("cnic").getValue(String.class);
                    customerName.setText(cusName);
                    customerNumber.setText(cusMobile);
                    customerCnic.setText(cusCnic);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        pickUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        Intent mansoor = null;

        try {
            mansoor = intentBuilder.build(getActivity());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        // Start the Intent by requesting a result, identified by a request code.
        startActivityForResult(mansoor, PICKUP_PLACE_PICKER_REQUEST);
            }
        });
        dropOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent mansoor = null;

                try {
                    mansoor = intentBuilder.build(getActivity());
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(mansoor, DROPOFF_PLACE_PICKER_REQUEST);
            }
        });







        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            ArrayList<ItemModel> newPlaylist = (ArrayList<ItemModel>) bundle.getSerializable("newPlaylist");
            if (newPlaylist.size()!=0) {
                ItemAdapter myadapter = new ItemAdapter(getActivity(), newPlaylist);
                recyclerView.setAdapter(myadapter);
            }
        }






        rcvrName = view.findViewById(R.id.rcvrName);
        rcvrCnic = view.findViewById(R.id.rcvrCnic);
        rcvrNumber = view.findViewById(R.id.rcvrNumber);


        addItemBtn = view.findViewById(R.id.addItemButton);
        bookOrder = view.findViewById(R.id.makeOrder);

        bookOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cName, cCnic, cNumber, rName, rCnic, rNumber,ordername;
                cName = customerName.getText().toString();
                cCnic = customerCnic.getText().toString();
                ordername=orderName.getText().toString();
                cNumber = customerNumber.getText().toString();

                rName = rcvrName.getText().toString();
                rCnic = rcvrCnic.getText().toString();
                rNumber = rcvrNumber.getText().toString();
                if(smallPickp.isChecked())
                    truckType = 1;
                else if(mediumTruck.isChecked())
                    truckType = 2;
                else if(largeTruck.isChecked())
                    truckType = 3;

                if (cash.isChecked())
                    paymnetMethod=10;
                else if (online.isChecked())
                    paymnetMethod=20;

                if (cName.equals("")||ordername.equals("") || cCnic.equals("") || cNumber.equals("") || rName.equals("") || rCnic.equals("") || rNumber.equals("") || pickupAddress.equals("") || deliveryAddress.equals("") || truckType==0 || paymnetMethod == 0) {
                    Toast.makeText(getActivity(), "Complete all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ConfirmOrder.class);
                    intent.putExtra("cName", cName);
                    intent.putExtra("cCnic", cCnic);
                    intent.putExtra("cNumber", cNumber);
                    intent.putExtra("rName", rName);
                    intent.putExtra("orderName",ordername);
                    intent.putExtra("rCnic", rCnic);
                    intent.putExtra("truckType",Integer.toString(truckType));
                    intent.putExtra("paymentMethod", Integer.toString(paymnetMethod));
                    intent.putExtra("rNumber", rNumber);
                    intent.putExtra("cusImage",cusImage);
                    intent.putExtra("pickup", pickupAddress);
                    intent.putExtra("delivery", deliveryAddress);
                    intent.putExtra("year", mYear);
                    intent.putExtra("month", mMonth);
                    intent.putExtra("day", mDay);
                    intent.putExtra("hour", mHour);
                    intent.putExtra("minute", mMinute);
                    intent.putExtra("pickuplat", pickupLat);
                    intent.putExtra("pickuplon", pickUpLon);
                    intent.putExtra("dropofflat", dropOffLat);
                    intent.putExtra("dropofflon", dropOffLon);
                    startActivity(intent);
                }


            }
        });


/*
        btnDatePicker = view.findViewById(R.id.date);
*/
        btnTimePicker = view.findViewById(R.id.time);
/*
        txtDate = view.findViewById(R.id.datetxt);
*/
        txtTime = view.findViewById(R.id.timetext);

/*
        btnDatePicker.setOnClickListener(this);
*/
        btnTimePicker.setOnClickListener(this);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItem.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
                //startActivity(intent);
            }
        });

        return view;
    }
            @Override
            public void onClick(View v) {

          /*      if (v == btnDatePicker) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    int yearr = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    mYear=year;
                                    mMonth=monthOfYear+1;
                                    mDay=dayOfMonth;

                                    txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, yearr, month, day);
                    datePickerDialog.show();
                }*/
                if (v == btnTimePicker) {

                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minutee = c.get(Calendar.MINUTE);
                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    mHour=hourOfDay;
                                    mMinute=minute;

                                    txtTime.setText(hourOfDay + ":" + minute);
                                }
                            }, hour, minutee, false);
                    timePickerDialog.show();
                }
            }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKUP_PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(getContext(), data);
                pickupAddress = (String) pickUpPlace.getAddress();
                pickupLat=pickUpPlace.getLatLng().latitude;
                pickUpLon=pickUpPlace.getLatLng().longitude;


                String toastMsg = String.format("Pickup Location: %s", pickupAddress);
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                TextView pickupText = (TextView) getActivity().findViewById(R.id.pickupLocation_tv);
                pickupText.setText(pickupAddress);
            }
        } else if (requestCode == DROPOFF_PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Place dropOffPlace = PlacePicker.getPlace(getActivity(), data);
                dropOffLat=dropOffPlace.getLatLng().latitude;
                dropOffLon=dropOffPlace.getLatLng().longitude;
                deliveryAddress = (String) dropOffPlace.getAddress();
                String toastMsg = String.format("Drop Location: %s", deliveryAddress);
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                TextView deliveryText = (TextView) getActivity().findViewById(R.id.deliveryLocation_tv);
                deliveryText.setText(deliveryAddress);
            }
        }
    }


}


