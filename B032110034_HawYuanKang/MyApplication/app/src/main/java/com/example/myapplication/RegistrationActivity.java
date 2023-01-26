package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityRegistrationBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    DatePickerDialog datePicker;
    ActivityRegistrationBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fabAddUser.setEnabled(false);

        binding.edtBirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fnInvokeDatePicker();
            }
        });
        binding.edtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnInvokeDatePicker();
            }
        });
        binding.edtBirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fnCheckInput();
            }
        });

        binding.edtFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fnCheckInput();
            }
        });
        binding.edtAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fnCheckInput();
            }
        });
        binding.edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fnCheckInput();
            }
        });
        binding.edtPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fnCheckInput();
            }
        });

        binding.rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnCheckInput();
            }
        });

        binding.rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnCheckInput();
            }
        });

        binding.fabAddUser.setOnClickListener(this::fnAddUser);

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

    private void fnAddUser(View view) {

        String strFullName = binding.edtFullName.getText().toString();
        String strPwd = binding.edtPwd.getText().toString();
        String strEmail = binding.edtEmail.getText().toString();
        String strBirth = binding.edtBirthdate.getText().toString();
        String strAddress = binding.edtAddress.getText().toString();
        String strGender = "";

        if(binding.rbMale.isChecked())
            strGender = binding.rbMale.getText().toString();
        else if(binding.rbFemale.isChecked())
            strGender = binding.rbFemale.getText().toString();

        User user =new User(strFullName,strPwd,strAddress,strEmail,strBirth,strGender);

        Intent intent=new Intent(this,SecondActivity.class);
        intent.putExtra("objUser",user);
        startActivity(intent);
    }

    private void fnInvokeDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                binding.edtBirthdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        },year,month,day);
        datePicker.show();
    }

    private void fnCheckInput(){
        if(binding.edtFullName.getText().toString().contentEquals("")||
        binding.edtEmail.getText().toString().contentEquals("")||
        binding.edtAddress.getText().toString().contentEquals("")||
        binding.edtPwd.getText().toString().contentEquals("")||
        binding.edtBirthdate.getText().toString().contentEquals("")) {
            if(!binding.rbFemale.isChecked() && binding.rbMale.isChecked()) {
                binding.fabAddUser.setEnabled(false);
            }
        }
        else {
            binding.fabAddUser.setEnabled(true);
        }
    }
}