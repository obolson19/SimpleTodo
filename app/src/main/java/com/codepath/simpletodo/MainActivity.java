package com.codepath.simpletodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    // keys used for passing data between activity
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";


    ArrayList<String> items;
    ArrayAdapter<String> itemAdapter;
    ListView lvitems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvitems = (ListView) findViewById(R.id.lvitems);
        readItems();
        itemAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items){
            @Override
            public View getView(int position, View mamaView, ViewGroup parent){
                View mama = super.getView(position, mamaView, parent);
                TextView papa = (TextView) mama.findViewById(android.R.id.text1);
                papa.setTextColor(Color.GREEN);
                return mama;
            }
        };

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

        // set up item listener for edit (regular click)
        lvitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create the new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // pass the data bein edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    // handle result from edit activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // extra updated activity from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            // extra original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model with the new item at the edited position
            items.set(position, updatedItem);
            // notify the adapter that the model changed
            itemAdapter.notifyDataSetChanged();
            // persist the changed model
           writeItems();
           // notify the user the operation completed ok
            Toast.makeText(this, "Item updated succesfully", Toast.LENGTH_SHORT).show();
        }
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

