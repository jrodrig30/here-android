package br.unisc.pdm.trabalho.database.schema;

import android.provider.BaseColumns;

public abstract class EventTable implements BaseColumns {

    static public final String TABLE_NAME  = "EVENT";
    static public final String NAME        = "name";
    static public final String START_DATE  = "start_date";
    static public final String END_DATE    = "end_date";
    static public final String DESCRIPTION = "description";

}
