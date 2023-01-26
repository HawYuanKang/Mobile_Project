package com.example.mobile_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Adapter_display_mypost extends RecyclerView.Adapter<Adapter_display_mypost.MyViewHolder> {

    Context context;
    ArrayList<Post> list;

    private OnItemClickListener listener;
    /////////////////////////////////
    public interface OnItemClickListener{
        void onItemClick(String getdaid);
    }

    public  void setOnItemClickListener(Adapter_display_mypost.OnItemClickListener clickListener){
        listener = clickListener;
    }
    /////////////////////////////////

    public Adapter_display_mypost(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_display_mypost.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_display_mypost,parent,false);
        return  new Adapter_display_mypost.MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_display_mypost.MyViewHolder holder, int position) {

        Post post = list.get(position);
        holder.title.setText(post.getTitle());
        holder.datetime.setText(post.getDatetime());
        holder.description.setText(post.getDescription());
        holder.id.setText(post.getId());
        Glide.with(context).load(post.getImageURL()).into(holder.imageView);
        /////////////////
        Query query2 = FirebaseDatabase.getInstance().getReference("Upvote").orderByChild("postid").equalTo(holder.id.getText().toString());
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    holder.count++;
                }
                holder.upvote.setText("Upvote: "+holder.count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, datetime, description, id, upvote;
        ImageView imageView;
        int count;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);

            title = itemView.findViewById(R.id.textView_r_title);
            datetime = itemView.findViewById(R.id.textView_r_datetime);
            description = itemView.findViewById(R.id.textView_r_description);
            id = itemView.findViewById(R.id.textView_id);
            imageView = itemView.findViewById(R.id.imageView20);
            upvote = itemView.findViewById(R.id.textView_r_upvote);

            itemView.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), id.getText().toString() , Toast.LENGTH_SHORT).show();
                    listener.onItemClick(id.getText().toString());
                }
            });
        }
    }

}

