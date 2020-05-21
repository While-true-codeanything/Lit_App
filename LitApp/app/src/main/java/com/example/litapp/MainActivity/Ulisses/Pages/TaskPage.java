package com.example.litapp.MainActivity.Ulisses.Pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litapp.MainActivity.Ulisses.Adapters.TaskPageAdapter;
import com.example.litapp.MainActivity.Utilities.NetworkUtility;
import com.example.litapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class TaskPage extends Fragment {
    Context context;
    Toolbar toolbar;
    static String ref;

    public TaskPage(Context context, String ref) {
        this.context = context;
        this.ref = ref;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_page, container, false);
    }

    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        new SetData(false).execute();
        BottomNavigationView nav = getActivity().findViewById(R.id.tmenu);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Text_data:
                        new SetData(false).execute();
                        return true;
                    case R.id.Files_data:
                        new SetData(true).execute();
                        return true;
                }
                return false;
            }
        });
    }

    private class SetData extends AsyncTask<String, Void, ArrayList<String[]>> {
        private ProgressDialog pd;
        boolean neededFiles;
        LinearLayout l;

        public SetData(boolean neededFiles) {
            this.neededFiles = neededFiles;
            l = getActivity().findViewById(R.id.task_page_layout);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Loading", true,
                    false);
            l.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<String[]> doInBackground(String... urls) {
            try {
                ArrayList<String[]> allDt = new ArrayList<>();
                String[] p = new String[1];
                p[0] = NetworkUtility.getTime(ref, context);
                allDt.add(p);
                if (neededFiles) {
                    allDt.add(NetworkUtility.GetFiles(ref, context));
                } else {
                    allDt.add(NetworkUtility.getText(ref, context));
                }
                return allDt;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> result) {
            pd.dismiss();
            toolbar.setTitle(result.get(0)[0]);
            l.setVisibility(View.VISIBLE);
            RecyclerView tsk = getActivity().findViewById(R.id.task_content);
            tsk.setAdapter(new TaskPageAdapter(result.get(1), context, neededFiles));
        }
    }
}
