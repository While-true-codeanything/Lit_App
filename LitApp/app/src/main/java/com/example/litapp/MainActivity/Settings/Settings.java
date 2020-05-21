package com.example.litapp.MainActivity.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.litapp.MainActivity.Utilities.DataUtility;
import com.example.litapp.MainActivity.Utilities.NetworkUtility;
import com.example.litapp.R;

public class Settings extends Fragment {
    static Context context;
    static private DataUtility dt;

    public Settings(Context x) {
        context = x;
        dt = new DataUtility(context);
    }

    static private Spinner Yr;
    static public String[] Years;
    Spinner spin2;
    Spinner spin;
    int Sip;

    public int[] GetClassid(String cl) {
        String[] s = cl.split("_");
        String[] c = getActivity().getResources().getStringArray(R.array.Classes);
        String[] g = getActivity().getResources().getStringArray(R.array.Group);
        int[] r = new int[2];
        for (int i = 0; i < c.length; i++) {
            if (Integer.parseInt(s[0]) == Integer.parseInt(c[i])) {
                r[0] = i;
                break;
            }
        }
        for (int i = 0; i < g.length; i++) {
            if (Integer.parseInt(s[1]) == Integer.parseInt(g[i])) {
                r[1] = i;
                break;
            }
        }
        return r;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_page, container, false);
    }

    @Override
    public void onStart() {
        ConstraintLayout temp = getActivity().findViewById(R.id.settings_page_layout);
        new SetAllData().execute();
        super.onStart();
    }

    private class SetAllData extends AsyncTask<String, Void, String[]> {
        private ProgressDialog pd;
        ConstraintLayout temp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            temp = getActivity().findViewById(R.id.settings_page_layout);
            temp.setVisibility(View.INVISIBLE);
            pd = ProgressDialog.show(context, "", "Loading", true,
                    false);
        }

        @Override
        protected String[] doInBackground(String... urls) {
            return NetworkUtility.GetYears(context);
        }

        @Override
        protected void onPostExecute(String[] result) {
            temp.setVisibility(View.VISIBLE);
            pd.dismiss();
            setUserInfoandQText();
            Years = result;
            setYearSpin();
            int[] ids = GetClassid(dt.getData(DataUtility.Class));
            spin.setSelection(ids[0], true);
            spin2.setSelection(ids[1], true);
            Sip = spin.getSelectedItemPosition();
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String item = (String) parent.getItemAtPosition(position);
                    String[] s = dt.getData(DataUtility.Class).split("_");
                    s[0] = item;
                    String gr = spin2.getSelectedItem().toString();
                    if (item.equals("5") && (gr.equals("5") || gr.equals("6"))) {
                        Toast.makeText(context, "Не изменен класс для расписания!Не существует 5_5 или 5_6",
                                Toast.LENGTH_LONG).show();
                        spin.setSelection(Sip);
                    } else {
                        dt.setData(s[0] + "_" + spin2.getSelectedItem().toString(), DataUtility.Class);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            Sip = spin2.getSelectedItemPosition();
            spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String item = (String) parent.getItemAtPosition(position);
                    String[] s = dt.getData(DataUtility.Class).split("_");
                    s[1] = item;
                    String gr = spin.getSelectedItem().toString();
                    if (gr.equals("5") && (item.equals("5") || item.equals("6"))) {
                        Toast.makeText(context, "Не изменен класс для расписания!Не существует 5_5 или 5_6",
                                Toast.LENGTH_LONG).show();
                        spin.setSelection(Sip);
                    } else {
                        dt.setData(gr + "_" + s[1], DataUtility.Class);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void setUserInfoandQText() {
        TextView tv = getActivity().findViewById(R.id.You);
        String text = tv.getText() + " " + dt.getData(DataUtility.Login);
        tv.setText(text);
        tv = getActivity().findViewById(R.id.Quite);
        text = (String) tv.getText();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dt.DeleteData();
                getActivity().finish();
            }
        });
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        tv.setText(content);
    }

    private void setYearSpin() {
        Yr = getActivity().findViewById(R.id.Year);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin = getActivity().findViewById(R.id.Class);
        spin2 = getActivity().findViewById(R.id.Group);
        Yr.setAdapter(adapter);
        int i;
        for (i = 0; i < Years.length; i++) {
            if (Years[i].equals(dt.getData(DataUtility.Year))) break;
        }
        Yr.setSelection(i);
        Yr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                dt.setData(item, DataUtility.Year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
