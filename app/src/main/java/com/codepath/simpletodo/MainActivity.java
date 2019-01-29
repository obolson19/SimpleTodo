package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemAdapter;
    ListView lvitems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvitems = (ListView) findViewById(R.id.lvitems);
        lvitems.setAdapter(itemAdapter);

        // mock data
        //items.add("first item");
        //items.add("second item");

        setupListviewlistener();

    }

    public void onAddItem(View v) {
        EditText etNewitem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewitem.getText().toString();
        itemAdapter.add(itemText);
        etNewitem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(),"item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListviewlistener() {
        Log.i("MainActivity", "setting up listener on list view");
        lvitems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "item removed from list: " + position);
                items.remove(position);
                itemAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private File getDatafile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private  void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDatafile(), Charset.defaultCharset()));
        } catch (IOException e) {
           Log.e("Mainactivity", "Error reading file", e);
           items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDatafile(), items);
        } catch (IOException e) {
            Log.e("Mainactivity", "Error writing file", e);
        }
    }
}

