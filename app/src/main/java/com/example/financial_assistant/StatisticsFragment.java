package com.example.financial_assistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment implements DatePicker.OnDateChangedListener {
  private TextView tvDate;
  private double outgoing = 0, incoming = 0;
  private ArrayList<String> type = new ArrayList<>();
  private ArrayList<Double> count = new ArrayList<>();

  private int year, month, day;
  private StringBuffer date = new StringBuffer();
  private Context context;
  MainActivity.MyHelper myHelper;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    context = getActivity();
    View statisticsLayout = inflater.inflate(R.layout.statistics_fragment,container,false);
    tvDate = (TextView)statisticsLayout.findViewById(R.id.statistics_date);
    tvDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (date.length() > 0) { //清除上次记录的日期
              date.delete(0, date.length());
            }
            tvDate.setText(date.append(String.valueOf(year)).append("-").append(String.valueOf(month + 1)).append("-").append(day));
            Log.i("1date", "onCreateView: " + date);
            dialog.dismiss();
            myHelper = new MainActivity.MyHelper(context);
            SQLiteDatabase db = myHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from information where date=?", new String[]{date.toString()});
            Log.i("date", "onCreateView: " + date.toString());
            if (cursor.getCount() == 0) {
              Toast.makeText(context,"该日期没有账单记录",Toast.LENGTH_SHORT).show();
            } else {
              cursor.moveToFirst();
              type.add(cursor.getString(1));
              count.add(Double.parseDouble(cursor.getString(3)));
              while (cursor.moveToNext()) {
                type.add(cursor.getString(1));
                count.add(Double.parseDouble(cursor.getString(3)));
              }
            }
            cursor.close();
            db.close();
            Log.i("type", "onCreateView: " + type);
            Log.i("count", "onCreateView: " + count);
            for (int i=0;i<type.size();i++) {
              if (type.get(i).equals("支出")) {
                outgoing += count.get(i);
              } else {
                incoming += count.get(i);
              }
            }
            TextView tvOutgoing = (TextView)statisticsLayout.findViewById(R.id.statistics_outgoing);
            TextView tvIncoming = (TextView)statisticsLayout.findViewById(R.id.statistics_incoming);
            TextView tvSum = (TextView)statisticsLayout.findViewById(R.id.statistics_sum);
            tvOutgoing.setText(String.valueOf(outgoing));
            tvIncoming.setText(String.valueOf(incoming));
            tvSum.setText(String.valueOf(incoming - outgoing));
          }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(context, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month, day, StatisticsFragment.this);
        tvDate.setText("");
      }
    });
    return statisticsLayout;
  }

  @Override
  public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    this.year = year;
    this.month = monthOfYear;
    this.day = dayOfMonth;
  }
}
