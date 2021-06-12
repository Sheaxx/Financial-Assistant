package com.example.financial_assistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

  static class MyHelper extends SQLiteOpenHelper {
    public MyHelper(@Nullable Context context) {
      super(context, "fa.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("CREATE TABLE information(title VARCHAR(50) PRIMARY KEY,type VARCHAR(20),content VARCHAR(255),count DOUBLE,date VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }
}