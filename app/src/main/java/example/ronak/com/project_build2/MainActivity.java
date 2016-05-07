package example.ronak.com.project_build2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> array;
    ArrayAdapter<String> adapter;
    List<Items> items;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditInfo.class);
                intent.putExtra("UpdateInd", -1);
                startActivity(intent);
            }
        });

        lv = (ListView)findViewById(R.id.listView);
        array = new ArrayList<String>();
        registerForContextMenu(lv);

        db = new DatabaseHandler(this);

        array.clear();
        items = new ArrayList<Items>();
        items = db.getAllItems();
        for(Items i: items){
            array.add(i.getTitle());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, array);
        lv.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(), "Retrieved", Toast.LENGTH_SHORT).show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                Items itemToUpdate = items.get(position);
                Intent intent = new Intent(MainActivity.this, EditInfo.class);
                intent.putExtra("UpdateInd", itemToUpdate.getKey_id());
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        array.clear();
        items = new ArrayList<Items>();
        items = db.getAllItems();
        /*for(Items i: items){
            array.add(i.getTitle());
        }*/
        for(Items i:items){
            if(i.getText_data().length()<=40){
                String title = i.getTitle();
                String data = i.getText_data();
                //array.add("TITLE: "+i.getTitle()+"\n\n"+i.getText_data());
                array.add(i.getTitle());
            }
            else {
                //array.add("TITLE: "+i.getTitle()+"\n\n"+i.getText_data().substring(0,40)+"...");
                array.add(i.getTitle());
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, array);
        lv.setAdapter(adapter);
        Log.e("LifeCycleMehtod==", "Resume");
        /* Fetch items from the database and put the items into the list view when the app opens. */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //Toast.makeText(getApplicationContext(),String.valueOf(info.position),Toast.LENGTH_SHORT).show();
        DatabaseHandler d;
        int pos;
        switch (item.getItemId()){

            case R.id.delete:

                d = new DatabaseHandler(this);
                pos = info.position;
                Items itemToDelete = items.get(pos);
                d.deleteItem(itemToDelete.getKey_id());
                items.remove(pos);
                array.remove(pos);
                adapter.notifyDataSetChanged();
                d.close();
                break;

        }

        return super.onContextItemSelected(item);
    }
}
