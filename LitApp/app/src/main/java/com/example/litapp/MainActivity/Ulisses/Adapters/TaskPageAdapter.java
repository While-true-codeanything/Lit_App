package com.example.litapp.MainActivity.Ulisses.Adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litapp.R;

public class TaskPageAdapter extends RecyclerView.Adapter<TaskPageAdapter.ViewHolder> {
    private String[] Data;
    private boolean neededfile;
    public Context context;
    public LayoutInflater inflater;

    public TaskPageAdapter(String[] a, Context context, boolean neededfile) {
        this.context = context;
        Data = a;
        this.neededfile = neededfile;
        inflater = LayoutInflater.from(context);
    }

    public TaskPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View subj;
        if (neededfile) subj = inflater.inflate(R.layout.file_item, parent, false);
        else subj = inflater.inflate(R.layout.text_item, parent, false);
        return new ViewHolder(subj);
    }

    public void onBindViewHolder(@NonNull final TaskPageAdapter.ViewHolder holder, int position) {
        holder.Field.setText(Html.fromHtml(Data[position], null, null));
        holder.Field.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public int getItemCount() {
        return Data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Field;

        ViewHolder(View view) {
            super(view);
            if (neededfile) {
                Field = view.findViewById(R.id.fil);
            } else {
                Field = view.findViewById(R.id.text);
            }
        }
    }
}
