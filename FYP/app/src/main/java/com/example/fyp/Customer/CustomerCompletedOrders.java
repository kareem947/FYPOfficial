package com.example.fyp.Customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCompletedOrders extends Fragment {


    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_completed_orders, container, false);
    }

}
