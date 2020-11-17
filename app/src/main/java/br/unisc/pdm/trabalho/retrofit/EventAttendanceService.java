package br.unisc.pdm.trabalho.retrofit;

import java.util.List;

import br.unisc.pdm.trabalho.database.model.EventAttendance;
import br.unisc.pdm.trabalho.retrofit.json.Fault;
import br.unisc.pdm.trabalho.retrofit.json.FaultEvent;
import br.unisc.pdm.trabalho.retrofit.json.Present;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by José on 14/06/2015.
 */
public interface EventAttendanceService {

    @GET("/event_attendances.json")
    List<EventAttendance> listAll();

    @GET("/event_attendances/presents/{id}.json")
    List<Present> listPresentsByEvent(@Path("id") long event_id);

    @GET("/event_attendances/faults/{id}.json")
    List<List<Fault>> listFaultsByEvent(@Path("id") long event_id);

}