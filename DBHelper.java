package com.example.datamanager01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "sqlite"; //データベース名
    private static int DB_VERSION = 1; //バーション

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //テーブル作成用SQL文の設定
        String sql = "CREATE TABLE Products (";
        sql += "id INTEGER PRIMARY KEY AUTOINCREMENT,";
        sql += "name TEXT NOT NULL,";
        sql += "price INTEGER NOT NULL);";

        //テーブル作成用SQL文を実行
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
