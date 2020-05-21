package com.example.litapp.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.litapp.MainActivity.Utilities.DataUtility;
import com.example.litapp.MainActivity.MainActivity;
import com.example.litapp.MainActivity.Utilities.NetworkUtility;
import com.example.litapp.R;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    static TextView login;
    static TextView password;

    private boolean isOnline() {
        ConnectivityManager checker = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = checker.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        if (isOnline() && !new DataUtility(LoginActivity.this).CheckIfFirstStart()) {
            final Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
        if (!isOnline()) {
            Toast.makeText(LoginActivity.this, "Нет подключения к интернету !Повторите попытку позже!",
                    Toast.LENGTH_LONG).show();
        }
        Button b = findViewById(R.id.Submit);
        login = findViewById(R.id.Login);
        password = findViewById(R.id.Password);
        b.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    Check c = new Check(login.getText().toString(), password.getText().toString());
                    c.execute();
                } else {
                    Toast.makeText(LoginActivity.this, "Не удалось войти! Проверьте доступность интернета или правильность данных!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class Check extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pd;
        String login;
        String password;

        public Check(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(LoginActivity.this, "", "Checking...", true,
                    false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                boolean b = NetworkUtility.CheckEnter(login, password);
                return b;
            } catch (IOException e) {
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if (result) {
                Spinner grL = findViewById(R.id.c);
                Spinner grL2 = findViewById(R.id.g);
                DataUtility dt = new DataUtility(LoginActivity.this);
                dt.setData("2019-2020", DataUtility.Year);
                if (grL.getSelectedItem().toString().equals("5") && (grL2.getSelectedItem().toString().equals("5") || grL2.getSelectedItem().toString().equals("6"))) {
                    dt.setData("5_1", DataUtility.Class);
                    Toast.makeText(LoginActivity.this, "Не cуществует 5_5 или 5_6! Был установлен класс 5_1!",
                            Toast.LENGTH_LONG).show();
                } else {
                    dt.setData(grL.getSelectedItem().toString() + "_" + grL2.getSelectedItem().toString(), DataUtility.Class);
                }
                dt.setData(LoginActivity.login.getText().toString(), DataUtility.Login);
                dt.setData(LoginActivity.password.getText().toString(), DataUtility.Password);
                final Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "Не удалось войти! Проверьте доступность сайта или правильность данных!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
