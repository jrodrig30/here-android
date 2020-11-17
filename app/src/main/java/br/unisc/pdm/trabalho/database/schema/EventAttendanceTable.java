package br.unisc.pdm.trabalho.database.schema;

import android.provider.BaseColumns;

public abstract class EventAttendanceTable implements BaseColumns {

    static public final String TABLE_NAME               = "EVENT_ATTENDANCE";
    //static public final String EVENT_ID                 = "eventId";
    static public final String PERSON_ID                = "personId";
    static public final String CHECK_IN                 = "checkIn";
    static public final String CHECK_OUT                = "checkOut";

}
