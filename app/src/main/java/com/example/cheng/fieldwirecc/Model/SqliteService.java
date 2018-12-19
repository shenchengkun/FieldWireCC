package com.example.cheng.fieldwirecc.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;

import java.util.ArrayList;
import java.util.List;

public class SqliteService extends SQLiteOpenHelper {

    protected static String CREATE_LAST_LIST = "CREATE TABLE IF NOT EXISTS last_list" +
            "(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "img_id VARCHAR, " +
            "link VARCHAR)";
    protected static String CREATE_SEARCH_HISTORY = "CREATE TABLE IF NOT EXISTS search_history" +
            "(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "search_key VARCHAR)";

    public SqliteService(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_LAST_LIST);
        sqLiteDatabase.execSQL(CREATE_SEARCH_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists last_list");
        sqLiteDatabase.execSQL("drop table if exists search_history");
        onCreate(sqLiteDatabase);
    }

    public List<String> getSearchHistory(){
        List<String> res = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM search_history",null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String searchKey = cursor.getString(1);
            res.add(searchKey);
        }
        return res;
    }

    public List<SearchResponseData> getLastList(){
        List<SearchResponseData> res = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM last_list",null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String imgID = cursor.getString(1);
            String link = cursor.getString(2);
            res.add(new SearchResponseData(imgID,link));
        }
        return res;
    }

    public void upgradeSearchHistory(List<String> history){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("drop table if exists search_history");
        sqLiteDatabase.execSQL(CREATE_SEARCH_HISTORY);
        SQLiteStatement statement = sqLiteDatabase.compileStatement(
                "INSERT INTO search_history VALUES (NULL, ?)");
        for (String s : history){
            statement.clearBindings();
            statement.bindString(1, s);
            statement.executeInsert();
        }
    }

    public void upgradeLastList(List<SearchResponseData> list){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("drop table if exists last_list");
        sqLiteDatabase.execSQL(CREATE_LAST_LIST);
        SQLiteStatement statement = sqLiteDatabase.compileStatement(
                "INSERT INTO last_list VALUES (NULL, ?,?)");
        for (SearchResponseData s : list){
            statement.clearBindings();
            statement.bindString(1, s.getId());
            statement.bindString(2, s.getLink());
            statement.executeInsert();
        }
    }
}
