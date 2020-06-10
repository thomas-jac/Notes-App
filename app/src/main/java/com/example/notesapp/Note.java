package com.example.notesapp;

public class Note {

    int id;
    String title, entry;

    public Note() {
    }

    public Note(int id, String title, String entry) {
        this.id = id;
        this.title = title;
        this.entry = entry;
    }

    public Note(String title, String entry) {
        this.title = title;
        this.entry = entry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
