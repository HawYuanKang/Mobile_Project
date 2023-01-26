package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityFirstBinding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class FirstActivity extends AppCompatActivity {
    ActivityFirstBinding binding;
    String name, textchg;
    int age;
    Bitmap bp;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCal.setOnClickListener(this::CalculateAge);
        binding.btnNext.setOnClickListener(this::NextActivity);
        Intent intent= getIntent();
        String VarNameChg = intent.getStringExtra("txtchg");
        Bitmap VarImgChg = (Bitmap) intent.getParcelableExtra("bpImg");
        textchg=VarNameChg;
        bp=VarImgChg;

        //binding.txtImageChange.setText(textchg);
        binding.imgPassHere.setImageBitmap(bp);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_menu);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                        case R.id.nav_main_activity:
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_camera_activity:
                        intent = new Intent(getApplicationContext(), FirstActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_register_activity:_activity:
                    intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_student_activity:
                        intent = new Intent(getApplicationContext(), StudentMainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_restapi_activity:
                        intent = new Intent(getApplicationContext(), GetRESTAPI.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_settings:
                        Toast.makeText(getApplicationContext(),"You navigated to Setting Screen", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_logout:
                        Toast.makeText(getApplicationContext(),"You are logged out! See ya!", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CalculateAge(View view){
        try {
            name = binding.txtName.getText().toString();
            age = Integer.parseInt(binding.txtAge.getText().toString());
            binding.textView.setText("Hello "+name+", your age is "+ (2022 -age) + "years old");
        }
        catch (Exception x) {
            binding.textView.setText("Don't leave them blank.");
        }

    }

    public void NextActivity(View view){
        Intent intent = new Intent(getApplicationContext(),ThreadedActivity.class);
        intent.putExtra("NAME",binding.textView.getText().toString());
        intent.putExtra("AGE",age);
        startActivity(intent);
    }
}