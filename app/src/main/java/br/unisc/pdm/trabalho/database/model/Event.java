package br.unisc.pdm.trabalho.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event extends AbstractModel {

    private String name;
    private Date start_date;
    private Date end_date;
    private String description;
    private List<EventMeeting> EventMeeting = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EventMeeting> getEventMeeting() {
        return EventMeeting;
    }

    public void setEventMeeting(List<EventMeeting> eventMeeting) {
        this.EventMeeting = eventMeeting;
    }

    @Override
    public String toString() {
        return getName();
    }

}
