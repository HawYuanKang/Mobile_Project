package com.example.mobile_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mobile_project.databinding.ActivityCreatePostBinding;
import com.example.mobile_project.databinding.ActivityDiscoverBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity {

    ActivityCreatePostBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    StorageReference storageReference;
    String theURL;
    ProgressDialog progressDialog;
    DatabaseReference reff;
    String initial_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /////load key test
        theURL = "https://firebasestorage.googleapis.com/v0/b/mobile-project-182b4.appspot.com/o/pepeface.jpg?alt=media&token=2b66e243-ff6f-49d8-89e6-dd9bade71397";
        reff = FirebaseDatabase.getInstance().getReference("Post");
        initial_key = reff.push().getKey();

        //////////navigationview
        getSupportActionBar().setTitle("Create Post");
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

        ////////////////////
    }

    ///for navigation click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fn_clear(View view) {
        binding.editTextTitle.setText("");
        binding.edittextDescription.setText("");
    }

    public void fn_camera(View view) {
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
            binding.imageView2.setImageBitmap(meme);
            ////upload image and get url
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Data");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference sr = storageReference.child(initial_key+".jpg");
            sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(CreatePostActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            theURL = uri.toString();
                            progressDialog.cancel();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreatePostActivity.this, "Network Fail", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreatePostActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            });
        }
    }

    public void fn_post(View view) {
        /////check fill
        if (binding.editTextTitle.getText().toString().equals("")||binding.edittextDescription.getText().toString().equals("")){
            Toast.makeText(this, "Please set your title/description", Toast.LENGTH_SHORT).show();
        }
        else {
            ////loading
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Data");
            progressDialog.show();
            //////////////
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy KK:mm aaa", Locale.getDefault());
            String formattedDate = df.format(c);
            Post post = new Post();
            reff = FirebaseDatabase.getInstance().getReference("Post");
            post.setTitle(binding.editTextTitle.getText().toString());
            post.setDescription(binding.edittextDescription.getText().toString());
            post.setDatetime(formattedDate);
            post.setEmail(MyPostActivity.the_email);
            post.setImageURL(theURL);
            post.setId(initial_key);
            reff.child(post.getId()).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreatePostActivity.this, "Post Successfully", Toast.LENGTH_SHORT).show();
                    reff = FirebaseDatabase.getInstance().getReference("Post");
                    initial_key = reff.push().getKey();
                    fn_clear(view);
                    progressDialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreatePostActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            });
        }
    }
}