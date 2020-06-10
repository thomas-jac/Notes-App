package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "Notes_DB";

    private static final String NOTES_TABLE = "notes";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String ENTRY = "entry";

    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_TABLE = "CREATE TABLE " + NOTES_TABLE
                + "(" + ID + " integer PRIMARY KEY autoincrement, "
                + TITLE + " TEXT, "
                + ENTRY + " TEXT)";

        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE " + NOTES_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    public void upgrade(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE " + NOTES_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    public void addNote(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, note.getTitle());
        values.put(ENTRY, note.getEntry());

        db.insert(NOTES_TABLE, null, values);
        db.close();
    }

    public Note getNote(int id){
        SQLiteDatabase db = getReadableDatabase();
        Note note = new Note();

        Cursor cursor = db.query(NOTES_TABLE,
                new String[]{ID, TITLE, ENTRY},
                ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if(cursor != null){
            cursor.moveToFirst();

            if(!TextUtils.isEmpty(cursor.getString(0))){
                note.setId(Integer.parseInt(cursor.getString(0)));
            }
            else{
                note.setId(0);
            }

            note.setTitle(cursor.getString(1));
            note.setEntry(cursor.getString(2));

            cursor.close();
        }
        return note;
    }

    public List<Note> getAllNotes(){
        SQLiteDatabase db = getReadableDatabase();
        List<Note> noteList = new ArrayList<>();

        String query = "SELECT * FROM " + NOTES_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                if(!TextUtils.isEmpty(cursor.getString(0))){
                    note.setId(Integer.parseInt(cursor.getString(0)));
                }
                else{
                    note.setId(0);
                }

                note.setTitle(cursor.getString(1));
                note.setEntry(cursor.getString(2));

                noteList.add(note);
            }while(cursor.moveToNext());

            cursor.close();
        }
        return noteList;
    }

    public void updateNote(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, note.getTitle());
        values.put(ENTRY, note.getEntry());

        db.update(NOTES_TABLE,
                values,
                ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(NOTES_TABLE,
                ID + "=?",
                new String[]{String.valueOf(note.getId())});

        db.close();
    }

    public int getNoteCount(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
