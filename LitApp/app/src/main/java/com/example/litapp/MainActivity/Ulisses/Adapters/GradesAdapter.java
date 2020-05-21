package com.example.litapp.MainActivity.Ulisses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litapp.MainActivity.Ulisses.Uliss;
import com.example.litapp.R;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ViewHolder> {
    private String[] Data;
    Uliss parent;
    public LayoutInflater inflater;

    public GradesAdapter(String[] a, Context context, Uliss u) {
        Data = a;
        parent = u;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GradesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View subj = inflater.inflate(R.layout.grade_item, parent, false);
        return new ViewHolder(subj);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesAdapter.ViewHolder holder, int position) {
        final String s = Data[position];
        holder.Field.setText(s);
        View.OnClickListener next = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.SetCurClassView(Integer.parseInt(s.split(" ")[0]));
            }
        };
        holder.Field.setOnClickListener(next);
    }

    @Override
    public int getItemCount() {
        return Data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Field;

        ViewHolder(View view) {
            super(view);
            Field = view.findViewById(R.id.grd);
        }
    }
}
