package com.example.mobile_project.fragements;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile_project.User;
import com.example.mobile_project.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.mobile_project.User;
import com.example.mobile_project.databinding.FragmentRegisterBinding;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    DatabaseReference reff;
    User user;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_login, container, false);
        binding = FragmentRegisterBinding.inflate(inflater,container,false);
        binding.buttonRegister.setOnClickListener(this::fnRegister);
        return binding.getRoot();
    }

    private void fnRegister(View view){
        if ("".equals(binding.editTextUsername.getText().toString()) ||
                "".equals(binding.editTextEmail2.getText().toString()) ||
                "".equals(binding.editTextPassword3.getText().toString()) ||
                "".equals(binding.editTextPassword2.getText().toString()))
        {
            Toast.makeText(getContext(), "Please fill in all the field", Toast.LENGTH_SHORT).show();
        }
        else if (!binding.editTextPassword3.getText().toString().equals(binding.editTextPassword2.getText().toString()))
        {
            binding.editTextPassword3.setText("");
            binding.editTextPassword2.setText("");
            Toast.makeText(getContext(), "Password not same", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Creating Account");
            progressDialog.show();
            //////////////////////////
            user = new User();
            user.setUsername(binding.editTextUsername.getText().toString());
            user.setEmail(binding.editTextEmail2.getText().toString());
            user.setProfile("https://firebasestorage.googleapis.com/v0/b/mobile-project-182b4.appspot.com/o/pepeface.jpg?alt=media&token=2b66e243-ff6f-49d8-89e6-dd9bade71397");
            reff = FirebaseDatabase.getInstance().getReference().child("User");
            Query checkcustomer = FirebaseDatabase.getInstance().getReference("User").orderByChild("username").equalTo(user.getUsername());
            checkcustomer.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Toast.makeText(getContext(), "Username already exist", Toast.LENGTH_SHORT).show();
                        binding.editTextUsername.setText("");
                    }
                    else{
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(),binding.editTextPassword2.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        reff.push().setValue(user);
                                        binding.editTextUsername.setText("");
                                        binding.editTextEmail2.setText("");
                                        binding.editTextPassword3.setText("");
                                        binding.editTextPassword2.setText("");
                                        Toast.makeText(getContext(), "Register Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.editTextEmail2.setText("");
                                        Toast.makeText(getContext(), "Email already been used", Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}