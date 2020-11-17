package br.unisc.pdm.trabalho.retrofit;

import java.util.List;

import br.unisc.pdm.trabalho.database.model.Event;
import br.unisc.pdm.trabalho.database.model.Person;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by José on 14/06/2015.
 */
public interface EventService {

    @GET("/events.json")
    List<Event> listAll();

    @GET("/events/search_by_name/{name}.json")
    List<Event> select(@Path("name") String name);

    //Abstract DAO
    @GET("/events/view/{id}.json")
    Event get(@Path("id") long id);

    @POST("/events.json")
    ApiResponse insert(@Body Event event);

    @POST("/events/{id}.json")
    ApiResponse update(@Body Event event, @Path("id") long id);

    @DELETE("/events/{id}.json")
    ApiResponse delete(@Path("id") long id);

}