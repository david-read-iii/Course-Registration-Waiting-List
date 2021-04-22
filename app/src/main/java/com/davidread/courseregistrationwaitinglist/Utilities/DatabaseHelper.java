package com.davidread.courseregistrationwaitinglist.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.davidread.courseregistrationwaitinglist.Models.WaitingListEntry;

import java.util.ArrayList;

/**
 * This class provides functions to manipulate waiting list entry objects stored in an SQLite
 * database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database helper variables.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";

    /**
     * Constructs a database helper with context from the origin activity.
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the object is initially created. It creates the waiting list entry table.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableWaitingListEntryQuery = "CREATE TABLE " + WaitingListEntry.TABLE_NAME + "("
                + WaitingListEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WaitingListEntry.COLUMN_FIRST_NAME + " TEXT,"
                + WaitingListEntry.COLUMN_LAST_NAME + " TEXT,"
                + WaitingListEntry.COLUMN_COURSE + " TEXT,"
                + WaitingListEntry.COLUMN_PRIORITY + " TEXT"
                + ")";
        db.execSQL(createTableWaitingListEntryQuery);
    }

    /**
     * Called when the object is upgraded. It drops the old waiting list entry table and creates
     * a new one.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableWaitingListEntryQuery = "DROP TABLE IF EXISTS " + WaitingListEntry.TABLE_NAME;
        db.execSQL(dropTableWaitingListEntryQuery);
        onCreate(db);
    }

    /**
     * Inserts a waiting list entry object with the specified attributes into the database. It
     * returns the id of the inserted object.
     */
    public long insertWaitingListEntry(String firstName, String lastName, String course, String priority) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WaitingListEntry.COLUMN_FIRST_NAME, firstName);
        values.put(WaitingListEntry.COLUMN_LAST_NAME, lastName);
        values.put(WaitingListEntry.COLUMN_COURSE, course);
        values.put(WaitingListEntry.COLUMN_PRIORITY, priority);

        long id = db.insert(WaitingListEntry.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    /**
     * Returns the waiting list entry object stored in the database given its id. It returns a null
     * object if no such object is found.
     */
    public WaitingListEntry getWaitingListEntry(long id) {

        WaitingListEntry waitingListEntry = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + WaitingListEntry.TABLE_NAME + " WHERE " + WaitingListEntry.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            waitingListEntry = new WaitingListEntry(
                    cursor.getLong(cursor.getColumnIndex(WaitingListEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_COURSE)),
                    cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_PRIORITY))
            );
        }

        cursor.close();
        db.close();

        return waitingListEntry;
    }

    /**
     * Returns an array list containing all waiting list entry objects stored in the database.
     */
    public ArrayList<WaitingListEntry> getAllWaitingListEntries() {

        ArrayList<WaitingListEntry> waitingListEntries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + WaitingListEntry.TABLE_NAME + " ORDER BY " + WaitingListEntry.COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst())
            do {
                WaitingListEntry waitingListEntry = new WaitingListEntry(
                        cursor.getLong(cursor.getColumnIndex(WaitingListEntry.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_COURSE)),
                        cursor.getString(cursor.getColumnIndex(WaitingListEntry.COLUMN_PRIORITY))
                );
                waitingListEntries.add(waitingListEntry);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();

        return waitingListEntries;
    }

    /**
     * Updates the waiting list entry object with the specified old id with the specified new
     * attributes.
     */
    public void updateWaitingListEntry(long oldId, String newFirstName, String newLastName, String newCourse, String newPriority) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WaitingListEntry.COLUMN_FIRST_NAME, newFirstName);
        values.put(WaitingListEntry.COLUMN_LAST_NAME, newLastName);
        values.put(WaitingListEntry.COLUMN_COURSE, newCourse);
        values.put(WaitingListEntry.COLUMN_PRIORITY, newPriority);

        db.update(WaitingListEntry.TABLE_NAME, values, WaitingListEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(oldId)});
        db.close();
    }

    /**
     * Deletes the waiting list entry object with the specified id.
     */
    public void deleteWaitingListEntry(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WaitingListEntry.TABLE_NAME, WaitingListEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}