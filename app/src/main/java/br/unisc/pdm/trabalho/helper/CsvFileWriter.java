package br.unisc.pdm.trabalho.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.EventAttendance;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.PeopleService;
import br.unisc.pdm.trabalho.retrofit.json.Fault;
import br.unisc.pdm.trabalho.retrofit.json.FaultEvent;
import br.unisc.pdm.trabalho.retrofit.json.Present;

public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String PRESENTS_FILE_HEADER = "event_id, person_registration_number, person_id, person_name, person_email, check_in, check_out";
    private static final String FAULTS_FILE_HEADER = "event_id, person_registration_number, person_id, person_name, person_email, event_meeting_start_date";
    private static final String PEOPLE_FILE_HEADER = "name, email, registration_number, photo";

    public  String writePeopleCsvFile(Context context, List<Person> people) {
        StringBuilder csv = new StringBuilder();

        //Write the CSV file header
        csv.append(PEOPLE_FILE_HEADER.toString());

        //Add a new line separator after the header
        csv.append(NEW_LINE_SEPARATOR);

        ApiFactory<PeopleService> factory = new ApiFactory<>(PeopleService.class);
        PeopleService peopleService = factory.getService();

        for (Person person : people) {
            csv.append(person.getName());
            csv.append(COMMA_DELIMITER);
            csv.append(person.getEmail());
            csv.append(COMMA_DELIMITER);
            csv.append(person.getRegistration_number());
            csv.append(COMMA_DELIMITER);
            csv.append(person.getPhoto());
            csv.append(NEW_LINE_SEPARATOR);
        }

        return csv.toString();
    }

    public String writeFaultsCsvFile(Context context, List<List<Fault>> faultEvents) {
        StringBuilder csv = new StringBuilder();

        //Write the CSV file header
        csv.append(FAULTS_FILE_HEADER.toString());

        //Add a new line separator after the header
        csv.append(NEW_LINE_SEPARATOR);

        ApiFactory<PeopleService> factory = new ApiFactory<>(PeopleService.class);
        PeopleService peopleService = factory.getService();

        //Write a new student object list to the CSV file
        if(faultEvents != null) {
            for(List<Fault> fe :faultEvents) {
                if(fe != null) {
                    for (Fault fault : fe) {
                        csv.append(fault.getEvent_id());
                        csv.append(COMMA_DELIMITER);
                        csv.append(fault.getRegistration_number());
                        csv.append(COMMA_DELIMITER);
                        csv.append(fault.getPerson_id());
                        csv.append(COMMA_DELIMITER);
                        csv.append(fault.getPerson_name());
                        csv.append(COMMA_DELIMITER);
                        csv.append(fault.getEmail());
                        csv.append(COMMA_DELIMITER);
                        csv.append(fault.getStart_date());
                        csv.append(NEW_LINE_SEPARATOR);
                    }
                }
            }
        }

        Log.i("myApp", "Faltas:" + csv.toString());
        return csv.toString();
    }

    public String writeCsvFile(Context context, List<Present> attendances) {
        StringBuilder csv = new StringBuilder();

        //Write the CSV file header
        csv.append(PRESENTS_FILE_HEADER.toString());

        //Add a new line separator after the header
        csv.append(NEW_LINE_SEPARATOR);

        ApiFactory<PeopleService> factory = new ApiFactory<>(PeopleService.class);
        PeopleService peopleService = factory.getService();

        //Write a new student object list to the CSV file
        for (Present attendance : attendances) {
            csv.append(attendance.getEvent_id());
            csv.append(COMMA_DELIMITER);
            csv.append(attendance.getRegistration_number());
            csv.append(COMMA_DELIMITER);
            csv.append(attendance.getPerson_id());
            csv.append(COMMA_DELIMITER);
            csv.append(attendance.getName());
            csv.append(COMMA_DELIMITER);
            csv.append(attendance.getEmail());
            csv.append(COMMA_DELIMITER);
            csv.append(attendance.getCheck_in());
            csv.append(COMMA_DELIMITER);
            csv.append(attendance.getCheck_out());
            csv.append(NEW_LINE_SEPARATOR);
        }

        Log.i("myApp", "Presenças:" + csv.toString());
        return csv.toString();
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }

}