package com.example.fyp.Customer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderPagerAdapter extends FragmentPagerAdapter {

    public List<Fragment> fragments = new ArrayList<>();
    public List<String> titles = new ArrayList<>();

    public CustomerOrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    public void addFragment(Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
    }

    public CharSequence getPageTitle (int position){
        return titles.get(position);
    }
}
