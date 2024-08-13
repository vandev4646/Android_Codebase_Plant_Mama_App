package com.example.plantmamaapp_v2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;


import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {
    //required constructor
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //******NEED TO CREATE A TABLE WHICH WILL STORE PHOTO PATHS BASED ON THE PLANT ID****


    //create the required tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table plants(id Integer primary key AUTOINCREMENT, plantID text,plantName text, dateAquired text, plantProfilePic text,height text,plantType text, careNotes text, description text, email text)";
        sqLiteDatabase.execSQL(qry1);
    }


    //method to create a a plant in the database
    public void createPlant(String plantID2, String plantName2, String dateAquired2, String plantProfilePic2, String height2, String plantType2, String careNotes2, String description2, String email) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        String str[] = new String[1];
        str[0] = plantID2.toString();
        Cursor c = db.rawQuery("select * from plants where plantID=?",str);
        if (c.moveToFirst()) {
            throw new Exception("Plant already exists. Please enter information for a different plant baby");
        }
        ContentValues cv = new ContentValues();
        cv.put("plantID", plantID2);
        cv.put("plantName", plantName2);
        cv.put("dateAquired", dateAquired2);
        cv.put("plantProfilePic", plantProfilePic2);
        cv.put("height", height2);
        cv.put("plantType", plantType2);
        cv.put("careNotes", careNotes2);
        cv.put("description", description2);
        cv.put("email", email);
        SQLiteDatabase db2 = getWritableDatabase();
        db2.insert("plants", null, cv);
        db2.close();
    }


    //this method will get all the plants the user has be referencing their email address
    public ArrayList plantsByUser (String email){
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String str[] = new String[1];
        str[0] = email;
        Cursor c = db.rawQuery("select * from plants where email = ?", str);


        if(c.moveToFirst()){
            do{
                arr.add(c.getString(1)+"$"+c.getString(2)+"$"+c.getString(3)+"$"+c.getString(4)+"$"+c.getString(5)+"$"+c.getString(6)+"$"+c.getString(7)+"$"+c.getString(8)+"$"+c.getString(9));
            } while (c.moveToNext());
        }
        db.close();
        return arr;
    }


    //*****NEED TO CREATE A METHOD WHICH GETS THE ALL THE PICS FOR THE PLANT BASED ON PLANT ID




    //currently not using this method
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

}
