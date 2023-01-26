package com.example.myapplication;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentViewHolder extends RecyclerView.ViewHolder {
    private TextView lblFullName,lblStudNo,lblGender,lblEmail,lblState,lblBirthDate;

    private Student student;
    public  StudentViewHolder(@NonNull View itemview){
        super(itemview);
        this.lblFullName=itemview.findViewById(R.id.lblFullName);
        this.lblStudNo=itemview.findViewById(R.id.lblStudNo);
        this.lblGender=itemview.findViewById(R.id.lblGender);
        this.lblBirthDate=itemview.findViewById(R.id.lblBirthDate);
        this.lblEmail=itemview.findViewById(R.id.lblEmail);
        this.lblState=itemview.findViewById(R.id.lblState);

       // itemview.setOnClickListener(this::fnEdit);
    }

    private void fnEdit(View view){
        Toast.makeText(view.getContext(),"The student name: "+ student.getStrFullname(),Toast.LENGTH_SHORT).show();
    }

    public void setStudent(Student student){
        this.student=student;

        lblFullName.setText(student.getStrFullname());
        lblStudNo.setText(student.getStrStudNo());
        lblGender.setText(student.getStrGender());
        lblBirthDate.setText(student.getStrBirthdate());
        lblEmail.setText(student.getStrEmail());
        lblState.setText(student.getStrState());

    }

}
