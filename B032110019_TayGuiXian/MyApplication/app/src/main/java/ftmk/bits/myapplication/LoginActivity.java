package ftmk.bits.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ftmk.bits.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void fn_login(View view) {
        if (binding.editTextUsername.getText().toString().equals("")||binding.editTextPassword.getText().toString().equals(""))
        {
            Toast.makeText(this, "Username and Password EditText should not be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(this, MainActivityLabtest.class);
            startActivity(intent);
        }

    }
}