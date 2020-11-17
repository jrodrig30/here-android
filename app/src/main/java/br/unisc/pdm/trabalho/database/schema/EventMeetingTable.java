package br.unisc.pdm.trabalho.database.schema;

import android.provider.BaseColumns;

public abstract class EventMeetingTable implements BaseColumns {

    static public final String TABLE_NAME = "EVENT_MEETING";
    static public final String EVENT_ID   = "event_id";
    static public final String DATE       = "date";
    static public final String START_TIME = "start_time";
    static public final String END_TIME   = "end_time";
    static public final String DETAILS    = "details";

}
