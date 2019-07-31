package com.dika.crudalpha.database;

public class Berita {
    public static final String TABLE_NAME = "berita";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "judul";
    public static final String COLUMN_CONTENT = "isi";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String judul;
    private String isi;
    private  String timestamp;

    public static final String CREATE_TABLE =
            " CREATE TABLE " + TABLE_NAME + "("+ COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE + "TEXT," + COLUMN_CONTENT + "TEXT," + COLUMN_TIMESTAMP + "DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    public Berita(){
    }

    public Berita(int id, String judul, String isi, String timestamp) {
        this.id = id;
        this.judul = judul;
        this.isi = isi;
        this.timestamp = timestamp;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul(){
        return judul;
    }

    public void setJudul(String judul){
        this.judul = judul;
    }

    public String getIsi(){
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
