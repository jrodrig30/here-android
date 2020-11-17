package br.unisc.pdm.trabalho.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.unisc.pdm.trabalho.database.model.EventAttendance;
import br.unisc.pdm.trabalho.database.schema.EventAttendanceTable;
import br.unisc.pdm.trabalho.database.schema.EventMeetingTable;
import br.unisc.pdm.trabalho.database.schema.EventTable;
import br.unisc.pdm.trabalho.database.schema.PersonTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "trabalho3.db";

    private static final String SQL_CREATE_TABLE_PERSON =
            "CREATE TABLE " + PersonTable.TABLE_NAME + " (" +
                    PersonTable._ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PersonTable.NAME    + " TEXT, " +
                    PersonTable.REGISTRATION_NUMBER + " TEXT, " +
                    PersonTable.EMAIL   + " TEXT, " +
                    PersonTable.PHOTO  + " TEXT  " +
            ")";

    private static final String SQL_CREATE_TABLE_EVENT =
            "CREATE TABLE " + EventTable.TABLE_NAME + " (" +
                    EventTable._ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EventTable.NAME        + " TEXT, " +
                    EventTable.START_DATE  + " TEXT, " +
                    EventTable.END_DATE    + " TEXT, " +
                    EventTable.DESCRIPTION + " TEXT  " +
            ")";

    private static final String SQL_CREATE_TABLE_EVENT_MEETINGS =
            "CREATE TABLE " + EventMeetingTable.TABLE_NAME + " (" +
                    EventMeetingTable._ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EventMeetingTable.EVENT_ID   + " INTEGER, " +
                    EventMeetingTable.DATE       + " TEXT, " +
                    EventMeetingTable.START_TIME + " TEXT, " +
                    EventMeetingTable.END_TIME   + " TEXT, " +
                    EventMeetingTable.DETAILS    + " TEXT, " +
                    "FOREIGN KEY(" + EventMeetingTable.EVENT_ID + ") REFERENCES " +
                        EventTable.TABLE_NAME + "(" + EventTable._ID + ")" +
            ")";

    private static final String SQL_CREATE_TABLE_EVENT_ATTENDANCE =
            "CREATE TABLE " + EventAttendanceTable.TABLE_NAME + " (" +
                    EventAttendanceTable._ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EventAttendanceTable.PERSON_ID    + " INTEGER, " +
                    EventAttendanceTable.CHECK_IN + " DATETIME, " +
                    EventAttendanceTable.CHECK_OUT   + " DATETIME, " +
                    "FOREIGN KEY(" + EventAttendanceTable.PERSON_ID + ") REFERENCES " + PersonTable.TABLE_NAME + "(" + PersonTable._ID + ") " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PERSON);
        db.execSQL(SQL_CREATE_TABLE_EVENT);
        db.execSQL(SQL_CREATE_TABLE_EVENT_MEETINGS);
        db.execSQL(SQL_CREATE_TABLE_EVENT_ATTENDANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //FIXME Resolver isso
        db.execSQL(SQL_CREATE_TABLE_PERSON);
        db.execSQL(SQL_CREATE_TABLE_EVENT_ATTENDANCE);
    }

}