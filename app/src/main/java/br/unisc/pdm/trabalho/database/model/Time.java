package br.unisc.pdm.trabalho.database.model;

import java.util.Date;

/**
 * Created by José on 24/06/2015.
 */
public class Time extends Date {

    public Time(long readLong) {
        super(readLong);
    }

    public Time(Date date) {
        super(date.getTime());
    }

}
