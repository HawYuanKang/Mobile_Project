package com.example.mobile_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mobile_project.databinding.ActivityDiscoverBinding;
import com.example.mobile_project.databinding.ActivityMyPostBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscoverActivity extends AppCompatActivity {

    ActivityDiscoverBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    DatabaseReference reff;
    User user;
    ProgressDialog progressDialog;
    Adapter_display_user myAdapter;
    ArrayList<User> list;
    String value1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        binding = ActivityDiscoverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //////////navigationview
        getSupportActionBar().setTitle("Discover");
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
                    case R.id.nav_mypost_activity:
                        intent = new Intent(getApplicationContext(),MyPostActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.nav_createpost_activity:
                        intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.nav_discover_activity:_activity:
                    intent = new Intent(getApplicationContext(), DiscoverActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.nav_logout:
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
        /////////////load users
        binding.recyclerDisplayUser.setHasFixedSize(true);
        binding.recyclerDisplayUser.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new Adapter_display_user(this,list);
        binding.recyclerDisplayUser.setAdapter(myAdapter);
        Query query1 = FirebaseDatabase.getInstance().getReference("User").orderByChild("username").startAt(value1).endAt(value1+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                myAdapter.notifyDataSetChanged();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (!user.getEmail().equals(MyPostActivity.the_email)){
                        list.add(user);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ///////click setting
        myAdapter.setOnItemClickListener(new Adapter_display_user.OnItemClickListener() {
            @Override
            public void onItemClick(String getid) {
                ////open edit page then with getid
                Intent intent = new Intent(DiscoverActivity.this, OtherPostActivity.class);
                intent.putExtra("email", getid);
                startActivity(intent);
            }
        });

        binding.edtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                value1=binding.edtSearchUser.getText().toString();
                /////////////load users
                binding.recyclerDisplayUser.setHasFixedSize(true);
                binding.recyclerDisplayUser.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                list = new ArrayList<>();
                myAdapter = new Adapter_display_user(getApplicationContext(),list);
                binding.recyclerDisplayUser.setAdapter(myAdapter);
                Query query1 = FirebaseDatabase.getInstance().getReference("User").orderByChild("username").startAt(value1).endAt(value1+"\uf8ff");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        myAdapter.notifyDataSetChanged();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            if (!user.getEmail().equals(MyPostActivity.the_email)){
                                list.add(user);
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                ///////click setting
                myAdapter.setOnItemClickListener(new Adapter_display_user.OnItemClickListener() {
                    @Override
                    public void onItemClick(String getid) {
                        ////open edit page then with getid
                        Intent intent = new Intent(DiscoverActivity.this, OtherPostActivity.class);
                        intent.putExtra("email", getid);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    ///for navigation click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}