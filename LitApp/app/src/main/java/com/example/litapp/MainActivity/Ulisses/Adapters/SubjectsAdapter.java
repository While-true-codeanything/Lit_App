package com.example.litapp.MainActivity.Ulisses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litapp.MainActivity.DataClasses.Subject;
import com.example.litapp.MainActivity.Ulisses.Uliss;
import com.example.litapp.R;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {
    public ArrayList<Subject> Data;
    public Context context;
    public LayoutInflater inflater;
    Uliss parent;

    public SubjectsAdapter(ArrayList<Subject> a, Context context, Uliss p) {
        this.context = context;
        parent = p;
        Data = a;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SubjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View subj = inflater.inflate(R.layout.subject_item, parent, false);
        return new ViewHolder(subj);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsAdapter.ViewHolder holder, int position) {
        final Subject sub = Data.get(position);
        if (sub.getSubject().length() > 41)
            holder.Subject.setText(sub.getSubject().substring(0, 41) + "\n" + sub.getTname());
        else {
            holder.Subject.setText(sub.getSubject() + "\n" + sub.getTname());
        }
        View.OnClickListener next = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.SetCurTaskView(sub.getRef());
            }
        };
        holder.Subject.setOnClickListener(next);
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Subject;

        ViewHolder(View view) {
            super(view);
            Subject = view.findViewById(R.id.Subject);
        }
    }
}
