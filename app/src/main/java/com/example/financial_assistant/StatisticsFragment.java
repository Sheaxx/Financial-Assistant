package com.example.financial_assistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment implements DatePicker.OnDateChangedListener {
  private TextView tvDate;

  private int year, month, day;
  private StringBuffer date = new StringBuffer();
  private Context context;

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
            tvDate.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month + 1)).append("月").append(day).append("日"));
            dialog.dismiss();
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
