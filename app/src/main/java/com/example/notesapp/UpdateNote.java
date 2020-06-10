package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UpdateNote extends AppCompatActivity {

    private EditText et_title, et_entry;
    private FloatingActionButton fab_submit;
    private Context context;
    private DBHandler dbHandler;
    private Toolbar toolbar;
    private int pos_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        context = this;

        et_title = findViewById(R.id.title_update_text);
        et_entry = findViewById(R.id.entry_update_text);
        fab_submit = findViewById(R.id.check_update_button);
        toolbar = findViewById(R.id.update_toolbar);

        dbHandler = new DBHandler(context);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_left_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Intent intent = getIntent();
        et_title.setText(intent.getStringExtra("title"));
        et_entry.setText(intent.getStringExtra("entry"));
        pos_id = intent.getIntExtra("id", 0);

        fab_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String entry = et_entry.getText().toString();

                if(!TextUtils.isEmpty(title) || !TextUtils.isEmpty(entry)){
                    Note note = new Note(title, entry);
                    note.setId(intent.getIntExtra("id", 0));

                    dbHandler.updateNote(note);
                    Toast.makeText(context, "Note Updated!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MainActivity.class));
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Please Fill Out The Fields")
                            .setNegativeButton("OK", null)
                            .show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        int flag = 0;

        switch (id) {
            case android.R.id.home:

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Go Back?");
                builder.setMessage("All Changes Will Be Lost");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                builder.setNegativeButton("No", dialogClickListener);
                builder.setPositiveButton("Yes", dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                flag = 1;
                break;

            case R.id.delete_button:
                final String title1 = et_title.getText().toString();
                final String entry1 = et_entry.getText().toString();

                if(TextUtils.isEmpty(title1) && TextUtils.isEmpty(entry1)){
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setMessage("Cannot Delete Empty Note")
                            .setNegativeButton("OK", null)
                            .show();
                    flag = 1;
                    break;
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Delete Note?");
                builder1.setMessage("All Data Will Be Lost");

                DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                Note note1 = new Note(pos_id, title1, entry1);
                                dbHandler.deleteNote(note1);
                                Toast.makeText(context, "Note Deleted!", Toast.LENGTH_SHORT).show();

                                DBHandler dbHandler1 = new DBHandler(context);
                                List<Note> noteList;
                                noteList = dbHandler1.getAllNotes();

                                dbHandler1.upgrade();

                                for(int j=0; j<noteList.size(); j++){
                                    Note note = new Note(noteList.get(j).getTitle(), noteList.get(j).getEntry());
                                    dbHandler1.addNote(note);
                                }

                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }

                    }
                };

                builder1.setNegativeButton("No", dialogClickListener1);
                builder1.setPositiveButton("Yes", dialogClickListener1);
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                flag = 1;
                break;

            case R.id.share_button:
                String title3 = et_title.getText().toString();
                String entry3 = et_entry.getText().toString();

                if(TextUtils.isEmpty(title3) && TextUtils.isEmpty(entry3)){
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                    builder3.setMessage("Cannot Share Empty Note")
                            .setNegativeButton("OK", null)
                            .show();
                }

                else{
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Share Your Thoughts";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                flag = 1;
                break;

            default:
                return super.onOptionsItemSelected(item);

        }

        if(flag == 1){
            return  true;
        }
        else{
            return  false;
        }
    }
}