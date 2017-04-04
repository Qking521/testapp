package com.example.wangqiang.activitys;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wangqiang.app.R;
import com.example.wangqiang.data.Person;

import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class LitepalActivity extends Activity {

    static final String TAG = LitepalActivity.class.getName();

    private List<Person> mPersonLists = new ArrayList<>();

    private TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litepal);
        initViews();
        initData();
    }

    private void initViews() {
        mResultText = (TextView) findViewById(R.id.textView);
    }

    private void initData() {
        Person person1 = new Person();
        person1.setAge(11);
        person1.setName("wang");
        mPersonLists.add(person1);
        Person person2 = new Person();
        person2.setAge(15);
        person2.setName("qiang");
        mPersonLists.add(person2);
        Person person3 = new Person();
        person3.setAge(23);
        person3.setName("you");
        mPersonLists.add(person3);
    }

    public void add(View view){
        Log.d(TAG, "add: ");
        for (Person mPersonList : mPersonLists) {
            mPersonList.save();
        }
    }

    public void update(View view) {
        Log.d(TAG, "update: ");
    }

    public void delete(View view) {
        Log.d(TAG, "delete: ");
    }

    public void query(View view) {
        Log.d(TAG, "query: ");
        List<Person> persons = DataSupport.findAll(Person.class);
        StringBuilder sb = new StringBuilder();
        for (Person person : persons) {
            sb.append("name: ").append(person.getName()).append(" ")
              .append("age: ").append(String.valueOf(person.getAge())).append("\n");
        }
        mResultText.setText(sb.toString());
    }
}
