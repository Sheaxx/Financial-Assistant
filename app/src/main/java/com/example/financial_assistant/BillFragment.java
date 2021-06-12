package com.example.financial_assistant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BillFragment extends Fragment {
  private ListView listView;
  private ArrayList<String> title = new ArrayList<>();
  private ArrayList<String> type = new ArrayList<>();
  private ArrayList<String> content = new ArrayList<>();
  private ArrayList<String> date = new ArrayList<>();
  private ArrayList<Double> count = new ArrayList<>();
  private Context context;
  MainActivity.MyHelper myHelper;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View billLayout = inflater.inflate(R.layout.bill_fragment,container,false);
    listView = (ListView)billLayout.findViewById(R.id.lv);
    context = getActivity();
    myHelper = new MainActivity.MyHelper(context);
    SQLiteDatabase db = myHelper.getReadableDatabase();
    Cursor cursor = db.query("information",null,null,null,null,null,null);
    if (cursor.getCount() == 0) {
      Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show();
    } else {
      cursor.moveToFirst();
      title.add(cursor.getString(0));
      type.add(cursor.getString(1));
      content.add(cursor.getString(2));
      count.add(Double.parseDouble(cursor.getString(3)));
      date.add(cursor.getString(4));
      while (cursor.moveToNext()) {
        title.add(cursor.getString(0));
        type.add(cursor.getString(1));
        content.add(cursor.getString(2));
        count.add(Double.parseDouble(cursor.getString(3)));
        date.add(cursor.getString(4));
      }
    }
    cursor.close();
    db.close();
    MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
    listView.setAdapter(myBaseAdapter);
    return billLayout;
  }

  class MyBaseAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      return title.size();
    }

    @Override
    public Object getItem(int position) {
      return title.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = View.inflate(context,R.layout.list_item,null);
      TextView tvTitle = (TextView)view.findViewById(R.id.item_title);
      TextView tvDate = (TextView)view.findViewById(R.id.item_date);
      TextView tvType = (TextView)view.findViewById(R.id.item_type);
      tvTitle.setText(title.get(position));
      tvDate.setText(date.get(position));
      tvType.setText(type.get(position) + " " + String.format("%.2f",count.get(position)));
      return view;
    }
  }
}
