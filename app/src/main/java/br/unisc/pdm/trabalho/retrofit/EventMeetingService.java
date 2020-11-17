package br.unisc.pdm.trabalho.retrofit;

import java.util.List;

import br.unisc.pdm.trabalho.database.model.Event;
import br.unisc.pdm.trabalho.database.model.EventMeeting;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.database.schema.EventTable;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by José on 14/06/2015.
 */
public interface EventMeetingService {

    @DELETE("/event_meetings/event/{id}.json")
    ApiResponse deleteByEvent(@Path("id") long id);

    @POST("/event_meetings.json")
    ApiResponse insert(@Body EventMeeting eventMeeting);

    @GET("/event_meetings.json")
    List<EventMeeting> listAll();

}