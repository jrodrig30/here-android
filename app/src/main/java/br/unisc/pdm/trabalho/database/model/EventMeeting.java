package br.unisc.pdm.trabalho.database.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventMeeting extends AbstractModel {

    private Date date;
    private Time start_time;
    private Time end_time;
    private String details;
    private String event_id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String toString() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        String dateString = "";
        if(date != null) {
            dateString = dateFormatter.format(date);
        }

        String startTimeString = "";
        if(start_time != null) {
            startTimeString = timeFormatter.format(start_time);
        }

        String endTimeString = "";
        if(end_time != null) {
            endTimeString = timeFormatter.format(end_time);
        }

        return String.format("%s - %s às %s", dateString, startTimeString, endTimeString);
    }

}
