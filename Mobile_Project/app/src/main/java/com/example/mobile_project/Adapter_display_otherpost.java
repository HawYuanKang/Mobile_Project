package com.example.mobile_project;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_display_otherpost extends RecyclerView.Adapter<Adapter_display_otherpost.MyViewHolder> {

    Context context;
    ArrayList<Post> list;

    private Adapter_display_otherpost.OnItemClickListener listener;
    /////////////////////////////////
    public interface OnItemClickListener{
        void onItemClick(String getdaid);
    }

    public  void setOnItemClickListener(Adapter_display_otherpost.OnItemClickListener clickListener){
        listener = clickListener;
    }
    /////////////////////////////////

    public Adapter_display_otherpost(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_display_otherpost.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_display_otherpost,parent,false);
        return  new Adapter_display_otherpost.MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_display_otherpost.MyViewHolder holder, int position) {

        Post post = list.get(position);
        holder.title.setText(post.getTitle());
        holder.datetime.setText(post.getDatetime());
        holder.description.setText(post.getDescription());
        holder.id.setText(post.getId());
        Glide.with(context).load(post.getImageURL()).into(holder.imageView);

        //////////
        Query query2 = FirebaseDatabase.getInstance().getReference("Upvote").orderByChild("postid").equalTo(holder.id.getText().toString());
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.count = 0;
                holder.thumb0.setVisibility(View.VISIBLE);
                holder.thumb1.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String get_email = dataSnapshot.child("email").getValue(String.class);
                    if (get_email.equals(MyPostActivity.the_email)){
                        ///set button
                        holder.thumb0.setVisibility(View.GONE);
                        holder.thumb1.setVisibility(View.VISIBLE);
                    }
                    holder.count++;
                }
                holder.number.setText(holder.count+"");
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

        TextView title, datetime, description, id, number;
        ImageView imageView, thumb0, thumb1;
        int count;
        DatabaseReference reff;
        ProgressDialog progressDialog;

        public MyViewHolder(@NonNull View itemView, Adapter_display_otherpost.OnItemClickListener listener){
            super(itemView);

            title = itemView.findViewById(R.id.textView_r_title);
            datetime = itemView.findViewById(R.id.textView_r_datetime);
            description = itemView.findViewById(R.id.textView_r_description);
            id = itemView.findViewById(R.id.textView_id);
            imageView = itemView.findViewById(R.id.imageView20);
            thumb0 = itemView.findViewById(R.id.imageView);
            thumb1 = itemView.findViewById(R.id.imageView5);
            thumb0.setVisibility(View.VISIBLE);
            thumb1.setVisibility(View.GONE);
            number = itemView.findViewById(R.id.textView_r_count);


            thumb0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), id.getText().toString() , Toast.LENGTH_SHORT).show();
                    //listener.onItemClick(id.getText().toString());
                    ////////add upvote
                    progressDialog = new ProgressDialog(itemView.getContext());
                    progressDialog.setMessage("Loading Data");
                    progressDialog.show();
                    Upvote upvote = new Upvote();
                    reff = FirebaseDatabase.getInstance().getReference("Upvote");
                    upvote.setPostid(id.getText().toString());
                    upvote.setEmail(MyPostActivity.the_email);
                    reff.push().setValue(upvote).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.cancel();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(itemView.getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
            });

            thumb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), id.getText().toString() , Toast.LENGTH_SHORT).show();
                    //listener.onItemClick(id.getText().toString());
                    /////////delete upvote
                    progressDialog = new ProgressDialog(itemView.getContext());
                    progressDialog.setMessage("Loading Data");
                    progressDialog.show();
                    Query query1 = FirebaseDatabase.getInstance().getReference("Upvote").orderByChild("postid").equalTo(id.getText().toString());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                String get_email = dataSnapshot.child("email").getValue(String.class);
                                if (get_email.equals(MyPostActivity.the_email)){
                                    dataSnapshot.getRef().removeValue();
                                }
                            }
                            progressDialog.cancel();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(itemView.getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
            });
        }
    }

}
