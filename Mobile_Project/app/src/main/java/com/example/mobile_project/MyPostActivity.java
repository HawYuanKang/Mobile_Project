package com.example.mobile_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project.databinding.ActivityMainBinding;
import com.example.mobile_project.databinding.ActivityMyPostBinding;
import com.example.mobile_project.databinding.FragmentRegisterBinding;
import com.example.mobile_project.fragements.LoginFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MyPostActivity extends AppCompatActivity {

    ActivityMyPostBinding binding;
    static String the_email;

    DatabaseReference reff;
    User user;
    ProgressDialog progressDialog;
    Adapter_display_mypost myAdapter;
    ArrayList<Post> list;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    StorageReference storageReference;
    String theURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        binding = ActivityMyPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("My Post");

        if (the_email.equals("loading")){
            Intent intent = getIntent();
            the_email = intent.getStringExtra("email");
        }

        //////////navigationview
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

        ////////// load profile
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("email").equalTo(the_email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String get_username = dataSnapshot.child("username").getValue(String.class);
                    String get_email = dataSnapshot.child("email").getValue(String.class);
                    String get_profile = dataSnapshot.child("profile").getValue(String.class);
                    binding.textViewUsername.setText(get_username);
                    binding.textViewEmail.setText(get_email);
                    Glide.with(getApplicationContext()).load(get_profile).into(binding.imageView1);
                }
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyPostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });

        /////////////retrieve post
        binding.recyclerMypost.setHasFixedSize(true);
        binding.recyclerMypost.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new Adapter_display_mypost(this,list);
        binding.recyclerMypost.setAdapter(myAdapter);
        Query query1 = FirebaseDatabase.getInstance().getReference("Post").orderByChild("email").equalTo(the_email);
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
        myAdapter.setOnItemClickListener(new Adapter_display_mypost.OnItemClickListener() {
            @Override
            public void onItemClick(String getid) {
                ////open edit page then with getid
                Intent intent = new Intent(MyPostActivity.this, EditPostActivity.class);
                intent.putExtra("id", getid);
                startActivity(intent);
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

    public void fn_profile(View view) {
        ///capture profile picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK ){
            Bitmap meme = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            meme.compress(Bitmap.CompressFormat.JPEG,90,bytes);
            byte bb[] = bytes.toByteArray();
            binding.imageView1.setImageBitmap(meme);
            ////upload image and get url
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Changing Profile");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference sr = storageReference.child(the_email+".jpg");
            sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(CreatePostActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            theURL = uri.toString();
                            ////update profile link
                            Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("email").equalTo(the_email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String getid = dataSnapshot.getKey();
                                        User user = new User();
                                        user.setUsername(binding.textViewUsername.getText().toString());
                                        user.setEmail(binding.textViewEmail.getText().toString());
                                        user.setProfile(theURL);
                                        reff = FirebaseDatabase.getInstance().getReference("User");
                                        reff.child(getid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Glide.with(getApplicationContext()).load(theURL).into(binding.imageView1);
                                                progressDialog.cancel();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MyPostActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                                progressDialog.cancel();
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MyPostActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MyPostActivity.this, "Network Fail", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyPostActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            });
        }
    }
}