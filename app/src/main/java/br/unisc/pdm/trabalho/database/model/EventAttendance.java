package br.unisc.pdm.trabalho.database.model;

import java.util.Date;

public class EventAttendance extends AbstractModel {

    private long id;

    private long eventDate_id;

    private long person_id;

    private DateTime check_in;

    private DateTime check_out;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventDate_id() {
        return eventDate_id;
    }

    public void setEventDate_id(long eventDate_id) {
        this.eventDate_id = eventDate_id;
    }

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public DateTime getCheck_in() {
        return check_in;
    }

    public void setCheck_in(DateTime check_in) {
        this.check_in = check_in;
    }

    public DateTime getCheck_out() {
        return check_out;
    }

    public void setCheck_out(DateTime check_out) {
        this.check_out = check_out;
    }
}