package br.unisc.pdm.trabalho.retrofit;

import java.util.List;

import br.unisc.pdm.trabalho.database.model.Person;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by José on 14/06/2015.
 */
public interface PeopleService {

    @GET("/people.json")
    List<Person> listPeople();

    @GET("/people/search_by_name/{name}.json")
    List<Person> select(@Path("name") String name);

    //Abstract DAO
    @GET("/people/view/{id}.json")
    Person get(@Path("id") long id);

    @POST("/people.json")
    ApiResponse insert(@Body Person person);

    @POST("/people/{id}.json")
    ApiResponse update(@Body Person person, @Path("id") long id);

    @DELETE("/people/{id}.json")
    ApiResponse delete(@Path("id") long id);

}