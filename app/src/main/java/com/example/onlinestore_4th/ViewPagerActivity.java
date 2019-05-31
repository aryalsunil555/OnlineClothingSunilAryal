package com.example.onlinestore_4th;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import adapter.ViewPagerAdapter;
import fragment.LoginFragment;
import fragment.RegistrationFragment;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter viewPageAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragment(new LoginFragment(), "Login");
        viewPageAdapter.addFragment(new RegistrationFragment(), "Registration");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
