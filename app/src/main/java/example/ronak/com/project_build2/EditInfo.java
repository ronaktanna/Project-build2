package example.ronak.com.project_build2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditInfo extends AppCompatActivity {

    EditText title,note;
    DatabaseHandler databaseHandler;
    boolean choice;
    int key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        title = (EditText)findViewById(R.id.title);
        note = (EditText)findViewById(R.id.info);
        databaseHandler = new DatabaseHandler(this);
        Intent intent = getIntent();
        key = intent.getIntExtra("UpdateInd", -1);

        if(key >= 0){
            choice = false;
            Items items = databaseHandler.getItem(key);
            title.setText(items.getTitle());
            note.setText(items.getText_data());
            //Toast.makeText(getApplicationContext(), "Key being edited is: "+String.valueOf(key), Toast.LENGTH_SHORT).show();
        }else{
            choice = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Items item = new Items();

        if(!title.getText().toString().equals("") && !note.getText().toString().equals("") && choice) {
            item.setTitle(title.getText().toString());
            item.setText_data(note.getText().toString());
            databaseHandler.addListItem(item);
            Toast.makeText(getApplicationContext(), "Note Added", Toast.LENGTH_SHORT).show();
        }
        else if(!title.getText().toString().equals("") && !note.getText().toString().equals("") && !choice){
            item.setKey_id(key);
            item.setTitle(title.getText().toString());
            item.setText_data(note.getText().toString());
            databaseHandler.updateItem(item);
            choice = true;
            Toast.makeText(getApplicationContext(), "Note edited", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Note discarded", Toast.LENGTH_SHORT).show();
        }
    }
}
