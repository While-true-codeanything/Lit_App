package com.example.litapp.MainActivity.Ulisses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litapp.MainActivity.DataClasses.Task;
import com.example.litapp.MainActivity.MainActivity;
import com.example.litapp.MainActivity.Ulisses.Pages.TaskPage;
import com.example.litapp.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    public ArrayList<Task> Data;
    public Context context;
    public LayoutInflater inflater;

    public TaskAdapter(ArrayList<Task> a, Context context, boolean neededPl) {
        this.context = context;
        ArrayList<Task> need = new ArrayList<Task>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).isPlugged()) {
                if (neededPl) need.add(a.get(i));
            } else {
                if (!neededPl) need.add(a.get(i));
            }
        }
        Data = need;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View subj = inflater.inflate(R.layout.task_item, parent, false);
        return new TaskAdapter.ViewHolder(subj);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, final int position) {
        final Task ts = Data.get(position);
        holder.Task.setText(ts.getText());
        if (ts.isPlugged())
            holder.Task.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        View.OnClickListener next = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.m2.setTitle(ts.getText());
                MainActivity.m2.loadFragment(new TaskPage(context, Data.get(position).getRef()));
            }
        };
        holder.Task.setOnClickListener(next);
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Task;

        ViewHolder(View view) {
            super(view);
            Task = view.findViewById(R.id.Task);
        }
    }
}

