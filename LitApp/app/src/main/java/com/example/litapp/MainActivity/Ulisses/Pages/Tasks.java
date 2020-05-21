package com.example.litapp.MainActivity.Ulisses.Pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litapp.MainActivity.DataClasses.Task;
import com.example.litapp.MainActivity.Ulisses.Adapters.TaskAdapter;
import com.example.litapp.MainActivity.Utilities.NetworkUtility;
import com.example.litapp.R;

import java.io.IOException;
import java.util.ArrayList;

public class Tasks extends Fragment {
    Context context;
    static String ref;
    static int cpage;
    static int maxpage;
    RecyclerView plg;
    RecyclerView tsk;
    private float downX;

    public Tasks(Context ct, String ref, int maxpage) {
        Tasks.ref = ref;
        cpage = 1;
        Tasks.maxpage = maxpage;
        context = ct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.uliss_innerpage, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        new SetData(ref, cpage).execute();
        plg = getActivity().findViewById(R.id.plugged);
        tsk = getActivity().findViewById(R.id.others);
        RecyclerView plg = getActivity().findViewById(R.id.plugged);
        RecyclerView tsk = getActivity().findViewById(R.id.others);
        View.OnTouchListener chg = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        float upX = event.getX();
                        float deltaX = downX - upX;
                        if (Math.abs(deltaX) > 300) {
                            if (deltaX < 0) {
                                if (cpage != maxpage) {
                                    cpage++;
                                    new SetData(ref, cpage).execute();
                                    return true;
                                }
                            }
                            if (deltaX > 0) {
                                if (cpage != 1) {
                                    cpage--;
                                    new SetData(ref, cpage).execute();
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                }
                return false;
            }
        };
        plg.setOnTouchListener(chg);
        tsk.setOnTouchListener(chg);
    }

    private class SetData extends AsyncTask<String, Void, ArrayList<Task>> {
        private ProgressDialog pd;
        String ref;
        int page;
        LinearLayout lay;

        public SetData(String ref, int page) {
            this.ref = ref;
            this.page = page;
            lay = getActivity().findViewById(R.id.uliss_innerpage_layout);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Loading", true,
                    false);
            lay.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<Task> doInBackground(String... urls) {
            try {
                return NetworkUtility.GetTasks(Tasks.ref, Tasks.cpage, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Task> result) {
            pd.dismiss();
            lay.setVisibility(View.VISIBLE);
            getActivity().setTitle("Page: " + cpage);
            plg.setAdapter(new TaskAdapter(result, context, true));
            TextView pl_label = getActivity().findViewById(R.id.pl_Label);
            if (plg.getAdapter().getItemCount() == 0) {
                pl_label.setText("");
            } else {
                pl_label.setText("Закрепленные");
            }
            tsk.setAdapter(new TaskAdapter(result, context, false));
            TextView npl_label = getActivity().findViewById(R.id.npl_Label);
            if (tsk.getAdapter().getItemCount() == 0) {
                npl_label.setText("");
            } else {
                npl_label.setText("Задания");
            }
        }
    }
}
