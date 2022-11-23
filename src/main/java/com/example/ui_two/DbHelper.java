//package com.example.ui_two;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DbHelper extends SQLiteOpenHelper {
//
//    private static final int DATABASE_VERSION = 1;
//    private static final String CREATE_TABLE = "create table "+DbContract.TABLE_NAME+
//            "(id integer primary key autoincrement,"+DbContract.TITLE+" text,"+DbContract.IMAGE+" text);";
//    private static final String DROP_TABLE = "drop table if exists "+DbContract.TABLE_NAME;
//
//    public DbHelper(Context context)
//    {
//        super(context,DbContract.DATABASE_NAME,null,DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL(DROP_TABLE);
//        onCreate(sqLiteDatabase);
//
//    }
//    public void saveToLocalDatabase(String title, String image)
//    {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DbContract.TITLE,title);
//        contentValues.put(DbContract.IMAGE,image);
////        contentValues.put(String.valueOf(DbContract.PRICE),price);
////        contentValues.put(String.valueOf(DbContract.RATING),rating);
//        getReadableDatabase().insert(DbContract.TABLE_NAME,null,contentValues);
//
//    }
//    public Cursor readFromLocalDatabase(SQLiteDatabase database)
//    {
//        String[] projection = {DbContract.TITLE,DbContract.IMAGE};
//        return (database.query(DbContract.TABLE_NAME,projection,null,null,null,null,null));
//    }
//    public void updateLocalDatabase(String title,String image, double price, float rating)
//    {
//        ContentValues contentValues = new ContentValues();
//         contentValues.put(DbContract.IMAGE,image);
//         String selection =  DbContract.TITLE+" LIKE ?";
//         String[] selection_args = {title};
//        getReadableDatabase().update(DbContract.TABLE_NAME,contentValues,selection,selection_args);
//    }
//}
