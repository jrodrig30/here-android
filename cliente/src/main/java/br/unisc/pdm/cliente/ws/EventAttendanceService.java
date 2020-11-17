package br.unisc.pdm.cliente.ws;

import retrofit.http.Body;
import retrofit.http.POST;

public interface EventAttendanceService {

    @POST("/event_attendances.json")
    ApiResponse registerAttendance(@Body EventAttendanceObject obj);

}
