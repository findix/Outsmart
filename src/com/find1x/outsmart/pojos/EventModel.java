package com.find1x.outsmart.pojos;

/**
 * Created by Sean on 2015/3/29.
 */
public class EventModel {
    public long calendarId;
    public String title;
    public String eventLocation;
    public long dtstart;
    public int hasAlarm;
    public long eventId;
    public int positionInList;

    public EventModel(long calendarId, String title, String eventLocation, long dtstart, int hasAlarm, long eventId) {
        this.calendarId = calendarId;
        this.title = title;
        this.eventLocation = eventLocation;
        this.dtstart = dtstart;
        this.hasAlarm = hasAlarm;
        this.eventId = eventId;
    }
}
