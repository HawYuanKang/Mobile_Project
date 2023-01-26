package ftmk.bits.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {

    private final LayoutInflater layoutInflater;
    //private final Vector<Student> students;
    private final List<Student> list;


    public StudentAdapter(LayoutInflater layoutInflater, List<Student> list)
    {
        this.layoutInflater = layoutInflater;
        this.list = list;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new StudentViewHolder(layoutInflater.inflate(R.layout.item_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position)
    {
        holder.setStudent(list.get(position));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
