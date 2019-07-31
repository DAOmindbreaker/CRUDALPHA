package com.dika.crudalpha.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "berita_db";

    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( Berita.CREATE_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS " + Berita.TABLE_NAME );

        onCreate( db );

    }

    public long insertBerita(String judul, String isi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Berita.COLUMN_TITLE, judul);
        values.put(Berita.COLUMN_CONTENT, isi);

        long id = db.insert(Berita.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Berita getBerita(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = ((SQLiteDatabase) db).query( Berita.TABLE_NAME,
                new String[]{Berita.COLUMN_ID, Berita.COLUMN_TITLE, Berita.COLUMN_CONTENT,
                Berita.COLUMN_TIMESTAMP}, Berita.COLUMN_ID + "=?",
                new  String[]{String.valueOf( id )}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Berita berita = new Berita(
                cursor.getInt( cursor.getColumnIndex( Berita.COLUMN_ID )),
                cursor.getString( cursor.getColumnIndex( Berita.COLUMN_TITLE )),
                cursor.getString( cursor.getColumnIndex( Berita.COLUMN_CONTENT )),
                cursor.getString( cursor.getColumnIndex( Berita.COLUMN_TIMESTAMP)));

                cursor.close();

                return berita;
    }

    public List<Berita> getAllBerita(){
        List<Berita> listBerita = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Berita.TABLE_NAME + " ORDER BY " + Berita.COLUMN_TIMESTAMP + "DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null );

        if (cursor.moveToFirst()) {
            do {
                Berita berita = new Berita();
                berita.setId( cursor.getInt( cursor.getColumnIndex( Berita.COLUMN_ID )));
                berita.setJudul( cursor.getString( cursor.getColumnIndex( Berita.COLUMN_TITLE )));
                berita.setIsi( cursor.getString( cursor.getColumnIndex( Berita.COLUMN_CONTENT )));
                berita.setTimestamp( cursor.getString( cursor.getColumnIndex( Berita.COLUMN_TIMESTAMP )));

                listBerita.add( berita );
            } while (cursor.moveToNext());
        }

        db.close();

        return listBerita;
    }

    public int getBeritaCount(){
        String countQuery = "SELECT * FROM " + Berita.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery( countQuery, null );
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateBerita(Berita berita){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( Berita.COLUMN_TITLE, berita.getJudul());
        values.put( Berita.COLUMN_CONTENT, berita.getIsi());

        return db.update( Berita.TABLE_NAME, values, Berita.COLUMN_ID + " = ? ",
                new String[]{String.valueOf( berita.getId())});
    }

    public void deleteBerita(Berita berita) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( Berita.TABLE_NAME, Berita.COLUMN_ID + " = ? ",
                new String[]{String.valueOf( berita.getId())});
        db.close();
    }

}
