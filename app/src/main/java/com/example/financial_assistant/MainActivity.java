package com.example.financial_assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{
  private Button btnDate;

  View messageLayout;
  BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    bottomNavigationView = findViewById(R.id.bottomNav);

    if (savedInstanceState == null){
      getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new BillFragment()).commit();
    }

    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
          case R.id.menu_add:
            fragment = new AddFragment();
            break;
          case R.id.menu_bill:
            fragment = new BillFragment();
            break;
          case R.id.menu_statistics:
            fragment = new StatisticsFragment();
            break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
        return true;
      }
    });

  }



}