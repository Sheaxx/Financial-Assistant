package com.example.financial_assistant;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFragment extends Fragment implements DatePicker.OnDateChangedListener{
  private RadioGroup rgType;
  private EditText edTitle;
  private EditText edContent;
  private EditText edCount;
  private Button btnDate;
  private Button btnAdd;

  private String type,title,content;
  private double count;
  private int year, month, day;
  private StringBuffer date = new StringBuffer();
  private Context context;
  MainActivity.MyHelper myHelper;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    context = getActivity();
    myHelper = new MainActivity.MyHelper(context);
    View addLayout = inflater.inflate(R.layout.add_fragment,container,false);

    rgType = (RadioGroup) addLayout.findViewById(R.id.rg_type);
    edTitle = (EditText)addLayout.findViewById(R.id.et_title);
    edContent = (EditText)addLayout.findViewById(R.id.et_content);
    edCount = (EditText)addLayout.findViewById(R.id.et_count);

    //选择日期
    btnDate = (Button)addLayout.findViewById(R.id.btn_date);
    btnDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (date.length() > 0) { //清除上次记录的日期
              date.delete(0, date.length());
            }
            btnDate.setText(date.append(String.valueOf(year)).append("-").append(String.valueOf(month + 1)).append("-").append(day));
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
        datePicker.init(year, month, day, AddFragment.this);
        btnDate.setText(year +  "-" + month + 1 + "-" + day);
      }
    });

    //添加记录
    btnAdd = (Button)addLayout.findViewById(R.id.btn_add);
    btnAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SQLiteDatabase db;
        ContentValues values;
        //获取单选框的值
        for (int i=0;i<rgType.getChildCount();i++) {
          RadioButton radioButton = (RadioButton)rgType.getChildAt(i);
          if (radioButton.isChecked()){
            type = radioButton.getText().toString();
            break;
          }
        }
        if (type == null) {
          Toast.makeText(context,"请选择支出或收入",Toast.LENGTH_SHORT).show();
          return;
        } else if (edTitle.getText() == null) {
          Toast.makeText(context,"请输入主题",Toast.LENGTH_SHORT).show();
          return;
        } else if (edContent.getText() == null) {
          Toast.makeText(context,"请输入金额",Toast.LENGTH_SHORT).show();
          return;
        }
        title = edTitle.getText().toString();
        content = edContent.getText().toString();
        count = Double.parseDouble(edCount.getText().toString());
        db = myHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("type",type);
        values.put("title",title);
        values.put("content",content);
        values.put("count",count);
        values.put("date", String.valueOf(date));
        db.insert("information",null,values);
        Toast.makeText(context,"记录已添加",Toast.LENGTH_SHORT).show();
        db.close();

        edTitle.setText("");
        edContent.setText("");
        edCount.setText("");
        btnDate.setText("选择日期");
        RadioButton radioButton1 = (RadioButton)addLayout.findViewById(R.id.outgoing);
        RadioButton radioButton2 = (RadioButton)addLayout.findViewById(R.id.incoming);
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
      }
    });

    return addLayout;
  }

  @Override
  public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    this.year = year;
    this.month = monthOfYear;
    this.day = dayOfMonth;
  }
}
