package com.example.diario_iriarivas;

public class entry {
    private String title;
    private String date;
    private int moodImage;
    private String text;
    private boolean favourite;

    public entry(String title, String date, int moodImage, String text, boolean favourite) {
        this.title = title;
        this.date = date;
        this.moodImage = moodImage;
        this.text = text;
        this.favourite = favourite;
    }

    public entry (){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMoodImage() {
        return moodImage;
    }

    public void setMoodImage(int moodImage) {
        this.moodImage = moodImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
