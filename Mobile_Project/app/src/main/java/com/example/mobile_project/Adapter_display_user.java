package com.example.mobile_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter_display_user extends RecyclerView.Adapter<Adapter_display_user.MyViewHolder> {

    Context context;
    ArrayList<User> list;

    private Adapter_display_user.OnItemClickListener listener;
    /////////////////////////////////
    public interface OnItemClickListener{
        void onItemClick(String getdaid);
    }

    public  void setOnItemClickListener(Adapter_display_user.OnItemClickListener clickListener){
        listener = clickListener;
    }
    /////////////////////////////////

    public Adapter_display_user(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_display_user.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_display_user,parent,false);
        return  new Adapter_display_user.MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_display_user.MyViewHolder holder, int position) {

        User user = list.get(position);
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());
        Glide.with(context).load(user.getProfile()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, email;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView, Adapter_display_user.OnItemClickListener listener){
            super(itemView);

            username = itemView.findViewById(R.id.textView_r_title);
            email = itemView.findViewById(R.id.textView_r_datetime);
            imageView = itemView.findViewById(R.id.imageView1);

            itemView.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), id.getText().toString() , Toast.LENGTH_SHORT).show();
                    listener.onItemClick(email.getText().toString());
                }
            });
        }
    }

}