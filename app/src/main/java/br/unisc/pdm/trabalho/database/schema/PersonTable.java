package br.unisc.pdm.trabalho.database.schema;

import android.provider.BaseColumns;

public abstract class PersonTable implements BaseColumns {

    static public final String TABLE_NAME               = "PERSON";
    static public final String NAME                     = "name";
    static public final String EMAIL                    = "email";
    static public final String REGISTRATION_NUMBER      = "registrationNumber";
    static public final String PHOTO                    = "photo";

}
