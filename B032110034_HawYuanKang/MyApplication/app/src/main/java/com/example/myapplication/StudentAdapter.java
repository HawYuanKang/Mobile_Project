package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Vector;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
        private final LayoutInflater inflater;
        //private  final Vector<Student> students;
        private final List<Student> students;

        public StudentAdapter(LayoutInflater inflater, List<Student> students) {
                this.inflater = inflater;
                this.students = students;
        }

        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new StudentViewHolder(inflater.inflate(R.layout.student_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
                holder.setStudent(students.get(position));
        }

        @Override
        public int getItemCount() {
                return students.size();
        }
}
