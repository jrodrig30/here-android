package br.unisc.pdm.trabalho.database.model;

import java.util.Date;

/**
 * Created by José on 24/06/2015.
 */
public class DateTime extends Date {

    public DateTime(long readLong) {
        super(readLong);
    }

    public DateTime(Date date) {
        super(date.getTime());
    }

}
