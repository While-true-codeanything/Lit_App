package com.example.litapp.MainActivity.TimeTable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.litapp.MainActivity.DataClasses.Row;
import com.example.litapp.MainActivity.Utilities.DataUtility;
import com.example.litapp.MainActivity.Utilities.NetworkUtility;
import com.example.litapp.R;

import java.util.ArrayList;

public class TimeTable extends Fragment {
    private Context context;
    private float downX;

    public TimeTable(Context ct) {
        context = ct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timetable_page, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();
        DataUtility.SetCurDay(context);
        TextView tv = getActivity().findViewById(R.id.Day);
        TextView lb = getActivity().findViewById(R.id.group);
        lb.setText(new DataUtility(context).getData(DataUtility.Class));
        tv.setText(DataUtility.DayName());
        SetData d = new SetData();
        d.execute();
        ConstraintLayout tab = getActivity().findViewById(R.id.timetable_page_layout);
        tab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                TextView tv = getActivity().findViewById(R.id.Day);
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
                                if (DataUtility.GetDay() != new DataUtility(context).MaxDays()) {
                                    DataUtility.SetDay(DataUtility.GetDay() + 1);
                                    new SetData().execute();
                                    tv.setText(DataUtility.DayName());
                                    return true;
                                }
                            }
                            if (deltaX > 0) {
                                if (DataUtility.GetDay() != 1) {
                                    DataUtility.SetDay(DataUtility.GetDay() - 1);
                                    new SetData().execute();
                                    tv.setText(DataUtility.DayName());
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private class SetData extends AsyncTask<String, Void, ArrayList<Row>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Row> doInBackground(String... urls) {
            return NetworkUtility.GetTimetable(new DataUtility(context).getData(DataUtility.Class), DataUtility.GetDay());
        }

        @Override
        protected void onPostExecute(ArrayList<Row> result) {
            ArrayList<Row> Data = result;
            TableLayout tab = getActivity().findViewById(R.id.Table);
            tab.removeViewsInLayout(2, tab.getChildCount() - 2);
            for (int i = 0; i < Data.size(); i++) {
                addTimetableRow(Data, i, tab);
            }
        }
    }

    private void addTimetableRow(ArrayList<Row> Data, int i, TableLayout tab) {
        int size = 50;
        int LeftPadding = 63;
        int UpPadding = 48;
        int HightDivider = 12;
        int WidthDivider = 2;
        TableRow tr = new TableRow(context);
        TextView tv = new TextView(context);
        tv.setPadding(LeftPadding, UpPadding, 0, 0);
        tv.setBackgroundResource(R.drawable.num);
        tv.setTextColor(Color.WHITE);
        tv.setHeight(size);
        tv.setWidth(size);
        TextView tv2 = new TextView(context);
        tv2.setPadding(LeftPadding, UpPadding, 0, 0);
        tv2.setBackgroundResource(R.drawable.rectangle);
        TextView tv3 = new TextView(context);
        tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout lv = new LinearLayout(context);
        ConstraintLayout l2 = getActivity().findViewById(R.id.timetable_page_layout);
        tv.setText(Integer.toString(Data.get(i).number));
        tv2.setText(Data.get(i).cab);
        tv3.setText(Data.get(i).Time);
        lv.addView(tv);
        lv.addView(tv2, l2.getWidth() / WidthDivider, l2.getHeight() / HightDivider);
        WidthDivider++;
        lv.addView(tv3, l2.getWidth() / WidthDivider, l2.getHeight() / HightDivider);
        tr.addView(lv);
        tab.addView(tr);
        tr = new TableRow(context);
        tr.addView(new Space(context), 1, 4);
        tab.addView(tr);
    }
}
