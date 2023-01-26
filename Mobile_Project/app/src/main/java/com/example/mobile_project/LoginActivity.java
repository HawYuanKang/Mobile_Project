package com.example.mobile_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_project.databinding.ActivityLoginBinding;
import com.example.mobile_project.fragements.MyViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import com.example.mobile_project.databinding.ActivityLoginBinding;
import com.example.mobile_project.fragements.MyViewPagerAdapter;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyPostActivity.the_email = "loading";

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.viewPager.setAdapter(myViewPagerAdapter);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}