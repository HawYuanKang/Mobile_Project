package com.example.mobile_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project.databinding.ActivityEditPostBinding;
import com.example.mobile_project.databinding.ActivityMyPostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class EditPostActivity extends AppCompatActivity {

    ActivityEditPostBinding binding;
    String currentid;
    Post post;
    StorageReference storageReference;
    String theURL;
    ProgressDialog progressDialog;
    DatabaseReference reff;
    String keyforimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        binding = ActivityEditPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reff = FirebaseDatabase.getInstance().getReference("Post");
        keyforimage = reff.push().getKey();

        Intent intent = getIntent();
        currentid = intent.getStringExtra("id");
        //Toast.makeText(this, currentid, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle("Edit Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //////////////////
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("Post").orderByChild("id").equalTo(currentid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    post = dataSnapshot.getValue(Post.class);
                    binding.editTextTitle2.setText(post.getTitle());
                    binding.edittextDescription2.setText(post.getDescription());
                    theURL = post.getImageURL();
                    if (!post.getImageURL().equals("empty")){
                        Glide.with(getApplicationContext()).load(post.getImageURL()).into(binding.imageView3);
                    }
                }
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            binding.imageView3.setImageBitmap(meme);
            ////upload image and get url
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Data");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference sr = storageReference.child(keyforimage+".jpg");
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
                            Toast.makeText(EditPostActivity.this, "Network Fail", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditPostActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            });
        }
    }

    public void fn_update(View view) {
        ///push with same id
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        reff = FirebaseDatabase.getInstance().getReference("Post");
        post.setTitle(binding.editTextTitle2.getText().toString());
        post.setDescription(binding.edittextDescription2.getText().toString());
        post.setImageURL(theURL);
        post.setId(currentid);
        reff.child(post.getId()).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditPostActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                reff = FirebaseDatabase.getInstance().getReference("Post");
                progressDialog.cancel();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditPostActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });
    }

    public void fn_delete(View view) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this post permanently?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog = new ProgressDialog(EditPostActivity.this);
                        progressDialog.setMessage("Loading Data");
                        progressDialog.show();
                        Query query1 = FirebaseDatabase.getInstance().getReference("Post").orderByChild("id").equalTo(currentid);
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.cancel();
                                            Toast.makeText(EditPostActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.cancel();
                                            Toast.makeText(EditPostActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.cancel();
                                Toast.makeText(EditPostActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();
    }
}