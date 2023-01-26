package com.example.mobile_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project.databinding.ActivityMyPostBinding;
import com.example.mobile_project.databinding.ActivityOtherPostBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OtherPostActivity extends AppCompatActivity {

    ActivityOtherPostBinding binding;
    String other_email;

    DatabaseReference reff;
    ProgressDialog progressDialog;
    Adapter_display_otherpost myAdapter;
    ArrayList<Post> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_post);
        binding = ActivityOtherPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        other_email = intent.getStringExtra("email");

        getSupportActionBar().setTitle("Other Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///////// load profile
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("email").equalTo(other_email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String get_username = dataSnapshot.child("username").getValue(String.class);
                    String get_email = dataSnapshot.child("email").getValue(String.class);
                    String get_profile = dataSnapshot.child("profile").getValue(String.class);
                    binding.textViewUsername6.setText(get_username);
                    binding.textViewEmail2.setText(get_email);
                    Glide.with(getApplicationContext()).load(get_profile).into(binding.imageView4);
                }
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OtherPostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });

        /////////////retrieve post
        binding.recyclerMypost.setHasFixedSize(true);
        binding.recyclerMypost.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new Adapter_display_otherpost(this,list);
        binding.recyclerMypost.setAdapter(myAdapter);
        Query query1 = FirebaseDatabase.getInstance().getReference("Post").orderByChild("email").equalTo(other_email);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                myAdapter.notifyDataSetChanged();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    list.add(post);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ///////click setting
        myAdapter.setOnItemClickListener(new Adapter_display_otherpost.OnItemClickListener() {
            @Override
            public void onItemClick(String getid) {
                ///update like
                Toast.makeText(OtherPostActivity.this, getid, Toast.LENGTH_SHORT).show();
            }
        });
    }
}