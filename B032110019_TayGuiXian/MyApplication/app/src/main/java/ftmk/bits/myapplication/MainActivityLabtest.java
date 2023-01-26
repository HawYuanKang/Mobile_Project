package ftmk.bits.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ftmk.bits.myapplication.databinding.ActivityLoginBinding;
import ftmk.bits.myapplication.databinding.ActivityMainLabtestBinding;

public class MainActivityLabtest extends AppCompatActivity {

    ActivityMainLabtestBinding binding;
    Executor executor;
    Handler handler;
    Bitmap bitmap = null;
    Bitmap bitmap1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_labtest);

        binding = ActivityMainLabtestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toast.makeText(this, "Welcome Mate", Toast.LENGTH_SHORT).show();

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null  && networkInfo.isConnected())
        {
            // the background task executor and handler is done within this checking
            //….
            //….
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL ImageURL = new URL("https://abmauri.com.my/wp-content/uploads/2021/06/ab_mauri_donut_recipe.jpg");
                        HttpURLConnection connection = (HttpURLConnection) ImageURL.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        bitmap = BitmapFactory.decodeStream(inputStream,null,options);

                        URL ImageURL1 = new URL("https://bazaronlinesgbuloh.my/wp-content/uploads/2021/03/bomboloni-red-velvet-rm1.00-scaled.jpg");
                        HttpURLConnection connection1 = (HttpURLConnection) ImageURL1.openConnection();
                        connection1.setDoInput(true);
                        connection1.connect();
                        InputStream inputStream1 = connection1.getInputStream();
                        BitmapFactory.Options options1 = new BitmapFactory.Options();
                        options1.inPreferredConfig = Bitmap.Config.RGB_565;
                        bitmap1 = BitmapFactory.decodeStream(inputStream1,null,options1);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {  // this is to update main thread -- UI
                        @Override
                        public void run() {
                            binding.imageViewFirst.setImageBitmap(bitmap);
                            binding.imageViewSecond.setImageBitmap(bitmap1);
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


    public void fn_calculate(View view) {
        double total_price = 0;
        int item1quantity = Integer.parseInt(binding.editTextItem1Quantity.getText().toString());
        int item2quantity = Integer.parseInt(binding.editTextItem2Quantity.getText().toString());
        int total_item = item1quantity+item2quantity;
        total_price = (3.5*item1quantity) + (2.5*item2quantity);
        Toast.makeText(this, "Total price is "+total_price, Toast.LENGTH_SHORT).show();

        binding.textViewTotalitem.setText("Total item: "+total_item);
        binding.textViewTotalprice.setText("Total price to pay: RM " + total_price);
    }
}