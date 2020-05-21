package com.example.litapp.MainActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.litapp.MainActivity.MainScreen.MainScreen;
import com.example.litapp.MainActivity.Settings.Settings;
import com.example.litapp.MainActivity.TimeTable.TimeTable;
import com.example.litapp.MainActivity.Ulisses.Uliss;
import com.example.litapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static MainActivity m2;

    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        m2 = this;
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.menu);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(new MainScreen(MainActivity.this));
                        setTitle("Lit_App");
                        return true;
                    case R.id.navigation_uliss:
                        loadFragment(new Uliss(MainActivity.this));
                        setTitle("Uliss");
                        return true;
                    case R.id.navigation_timetable:
                        TimeTable timetable = new TimeTable(MainActivity.this);
                        loadFragment(timetable);
                        setTitle("Timetable");
                        return true;
                    case R.id.navigation_info:
                        Settings s = new Settings(MainActivity.this);
                        loadFragment(s);
                        setTitle("Settings");
                        return true;
                }
                return false;
            }
        });
        loadFragment(new MainScreen(MainActivity.this));
    }
}
