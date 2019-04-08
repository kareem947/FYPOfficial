package com.example.fyp.Customer;


import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerAllOrders extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    CustomerOrderPagerAdapter customerOrderPagerAdapter;


    public CustomerAllOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_all_orders, container, false);

        customerOrderPagerAdapter=new CustomerOrderPagerAdapter(getChildFragmentManager());

        customerOrderPagerAdapter.addFragment(new customerAcceptedOrders(),"AcceptedOrders");
        customerOrderPagerAdapter.addFragment(new CustomerPendingOrders(),"PendingOrders");
        tabLayout = view.findViewById(R.id.customerorderTabLayout);
        viewPager = view.findViewById(R.id.customerorderViewPager);
        viewPager.setAdapter(customerOrderPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



        return view;
    }

}
