package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityThreadedBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedActivity extends AppCompatActivity {

    ActivityThreadedBinding tbind;
    Bitmap bp, bitmap;
    Executor executor;
    Handler handler;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threaded);
        tbind = ActivityThreadedBinding.inflate(getLayoutInflater());
        setContentView(tbind.getRoot());

        executor= Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        tbind.btnTakePic.setOnClickListener(this::fnTakePic);
        tbind.btnBack.setOnClickListener(this::GoBack);
        tbind.btnAsyncTask.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if(networkInfo != null  && networkInfo.isConnected())
                {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //URL ImageURL = new URL("https://ftmk.utem.edu.my/web/wp-content/uploads/2020/02/cropped-Logo-FTMK.png");
                                URL ImageURL = new URL("https://abmauri.com.my/wp-content/uploads/2021/06/ab_mauri_donut_recipe.jpg");
                                HttpURLConnection connection = (HttpURLConnection) ImageURL.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream inputStream = connection.getInputStream();
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.RGB_565;
                                bitmap = BitmapFactory.decodeStream(inputStream,null,options);

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {  // this is to update main thread -- UI
                                @Override
                                public void run() {
                                    tbind.imgVwProfile.setImageBitmap(bitmap);
                                }
                            });
                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Network!! Please add data plan or connect to wifi network!", Toast.LENGTH_SHORT).show();
                }
            }
        }));

        Intent intent = getIntent();
        String OtherActVar = intent.getStringExtra("NAME");
        tbind.textView3.setText("Welcome to Threaded Activity, "+OtherActVar);

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

    public void fnTakePic(View view){

        Runnable run = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tbind.textView3.setText("This is your picture hehehe");
                    }
                });
            }
        };
        Thread thread = new Thread(run);
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bp = (Bitmap)  data.getExtras().get("data");
        tbind.imgVwProfile.setImageBitmap(bp);
    }

    public void GoBack(View view){
        Intent intent = new Intent(getApplicationContext(),FirstActivity.class);
        intent.putExtra("bpImg",bp);
        intent.putExtra("txtchg","Your image changed");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),FirstActivity.class);
        intent.putExtra("bpImg",bp);
        intent.putExtra("txtchg","Your image changed");
        startActivity(intent);
    }
}