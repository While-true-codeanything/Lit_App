package com.example.litapp.MainActivity.Ulisses;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litapp.MainActivity.DataClasses.Subject;
import com.example.litapp.MainActivity.Ulisses.Adapters.GradesAdapter;
import com.example.litapp.MainActivity.Ulisses.Adapters.SubjectsAdapter;
import com.example.litapp.MainActivity.Ulisses.Pages.Tasks;
import com.example.litapp.MainActivity.Utilities.DataUtility;
import com.example.litapp.MainActivity.Utilities.NetworkUtility;
import com.example.litapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class Uliss extends Fragment {
    private static DataUtility dt;
    Context context;
    static String ref;
    public static RecyclerView sps;

    public Uliss(Context ct) {
        context = ct;
        dt = new DataUtility(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.uliss_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        sps = getActivity().findViewById(R.id.UlContent);
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.UpperView);
        final Uliss u = this;
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Grade_Data:
                        SetCurClassView(Integer.parseInt(dt.gr()));
                        return true;
                    case R.id.All_Data:
                        String[] s = new String[7];
                        s[0] = "5 классы";
                        s[1] = "6 классы";
                        s[2] = "7 классы";
                        s[3] = "8 классы";
                        s[4] = "9 классы";
                        s[5] = "10 классы";
                        s[6] = "11 классы";
                        sps.setAdapter(new GradesAdapter(s, context, u));
                        return true;
                }
                return false;
            }
        });
        new SetData(Integer.parseInt(dt.gr()), u).execute();
    }

    public void SetCurClassView(int gr) {
        new SetData(gr, this).execute();
    }

    public void SetCurTaskView(String ref) {
        Uliss.ref = ref;
        SetData2 temp = new SetData2(ref);
        temp.execute();
    }

    private class SetData extends AsyncTask<String, Void, ArrayList<Subject>> {
        Uliss u;
        private ProgressDialog pd;
        int grd;

        public SetData(int i, Uliss u) {
            grd = i;
            this.u = u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Loading", true,
                    false);
        }

        @Override
        protected ArrayList<Subject> doInBackground(String... urls) {
            try {
                return NetworkUtility.GetSubjects(dt.getData(DataUtility.Year), grd, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Subject> result) {
            pd.dismiss();
            sps.setAdapter(new SubjectsAdapter(result, context, u));
        }
    }

    private class SetData2 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog pd;
        String ref;

        public SetData2(String ref) {
            this.ref = ref;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Loading", true,
                    false);
        }

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                return NetworkUtility.Maxpage(Uliss.ref, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            pd.dismiss();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_content, new Tasks(context, ref, result));
            ft.commit();
        }
    }
}
