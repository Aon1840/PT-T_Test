package com.example.ptt_test_mobile.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.ptt_test_mobile.Model.Image;
import com.example.ptt_test_mobile.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //    Database Detail
    private static final String DATABASE_NAME = "PT-T_Database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "user";
    private static final String TABLE_IMAGE = "image";

    //    User Table Attribute
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    //    Image Table Attribute
    private static final String COLUMN_IMAGE_ID = "image_id";
    private static final String COLUMN_IMAGE_IMAGE = "image";
    private static final String COLUMN_IMAGE_USER_ID = "user_id";

    //    SQL for Create User Table
    private String CREATE_USER_TABLE = "CREATE TABLE "+TABLE_USER+" ("
        + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_USER_EMAIL + " TEXT,"
        + COLUMN_USER_PASSWORD + " TEXT);";

    //    SQL for Create Image Table
    private String CREATE_IMAGE_TABLE = "CREATE TABLE "+TABLE_IMAGE+" ("
            + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_IMAGE_IMAGE + " BLOB,"
            + COLUMN_IMAGE_USER_ID + " INTEGER);";
    //    SQL for Drop User Table
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS "+TABLE_USER;

    //    SQL for Drop Image Table
    private String DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS "+TABLE_IMAGE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        sqLiteDatabase.execSQL(DROP_IMAGE_TABLE);

        onCreate(sqLiteDatabase);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void addPhoto(byte[] image, int userId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_IMAGE, image);
        values.put(COLUMN_IMAGE_USER_ID, userId);

        db.insert(TABLE_IMAGE, null, values);
        db.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public List<Image> getAllImage(int user_id){
        String[] columns = {
                COLUMN_IMAGE_ID,
                COLUMN_IMAGE_IMAGE,
                COLUMN_IMAGE_USER_ID
        };

        String selection = COLUMN_IMAGE_USER_ID + " = ?";
        String[] selctionArgs = {Integer.toString(user_id)};

        List<Image> imageList = new ArrayList<Image>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGE,
                columns,
                selection,
                selctionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Image image = new Image();
                image.setImage_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_ID))));
                image.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_IMAGE)));
                image.setImage_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_USER_ID))));

                imageList.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return imageList;
    }

    public int getUserId(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int num=0;
        if( cursor != null && cursor.moveToFirst() ){
            num = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
            cursor.close();
        }

        db.close();

        return num;
    }

    public boolean checkEmail(String email){
        String[] columns = {
                COLUMN_USER_ID
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }

        return false;
    }


    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
