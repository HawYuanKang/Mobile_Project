package com.example.mobile_project.fragements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile_project.LoginActivity;
import com.example.mobile_project.MyPostActivity;
import com.example.mobile_project.User;
import com.example.mobile_project.databinding.FragmentLoginBinding;

import com.example.mobile_project.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_login, container, false);
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        binding.buttonLogin.setOnClickListener(this::fnLogin);
        binding.resetPassword.setOnClickListener(this::fn_reset);
        return binding.getRoot();
    }

    private void fnLogin(View view){

        if ("".equals(binding.editTextEmail.getText().toString()) || "".equals(binding.editTextPassword.getText().toString()))
        {
            Toast.makeText(getContext(), "Please fill in all the field", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Logging in");
            progressDialog.show();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.editTextEmail.getText().toString(),binding.editTextPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MyPostActivity.class);
                    intent.putExtra("email", binding.editTextEmail.getText().toString());
                    startActivity(intent);
                    getActivity().finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    public void fn_reset(View view) {
        if (binding.editTextEmail.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "Please enter you email", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Sending Main");
            progressDialog.show();
            FirebaseAuth.getInstance().sendPasswordResetEmail(binding.editTextEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Check your email to change reset password", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            });
        }
    }
}