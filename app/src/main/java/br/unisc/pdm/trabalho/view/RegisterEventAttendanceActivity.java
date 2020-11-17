package br.unisc.pdm.trabalho.view;

import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.EventAttendance;
import br.unisc.pdm.trabalho.database.model.EventMeeting;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.helper.DateHelper;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.EventMeetingService;
import br.unisc.pdm.trabalho.retrofit.PeopleService;

public class RegisterEventAttendanceActivity extends ActionBarActivity {

    private Spinner mPerson;
    private Spinner mEventMeeting;
    private EditText mCheckInDate;
    private EditText mCheckInTime;
    private EditText mCheckOutDate;
    private EditText mCheckOutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event_attendance);

        mPerson = (Spinner) findViewById(R.id.editPerson);
        ApiFactory<PeopleService> factory = new ApiFactory<>(PeopleService.class);
        PeopleService peopleService = factory.getService();
        List<Person> people = peopleService.listPeople();

        ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(this, android.R.layout.simple_dropdown_item_1line, people);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPerson.setAdapter(adapter);

        mEventMeeting = (Spinner) findViewById(R.id.editEventMeeting);

        /*
        ApiFactory<EventMeetingService> eventMeetingApiFactory = new ApiFactory<>(EventMeetingService.class);
        EventMeetingService eventMeetingService = eventMeetingApiFactory.getService();
        List<EventMeeting> eventMeeting = eventMeetingService.listAll();

        ArrayAdapter<EventMeeting> adapterEventMeeting = new ArrayAdapter<EventMeeting>(this, android.R.layout.simple_dropdown_item_1line, eventMeeting);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEventMeeting.setAdapter(adapterEventMeeting);

        mCheckInDate = (EditText) findViewById(R.id.editCheckInDate);
        mCheckInTime = (EditText) findViewById(R.id.editCheckInTime);
        mCheckOutDate = (EditText) findViewById(R.id.editCheckOutDate);
        mCheckOutTime = (EditText) findViewById(R.id.editCheckOutTime);
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_event_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register_event_attendance) {
            if( save() ) {
                Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean save() {
/*
        Person person = ((Person) mPerson.getSelectedItem());
        if(person == null) {
            Toast.makeText(this, "Selecione uma pessoa", Toast.LENGTH_LONG).show();
            return false;
        }

        EventMeeting eventMeeting = ((EventMeeting) mEventMeeting.getSelectedItem());
        if(eventMeeting == null) {
            Toast.makeText(this, "Selecione um encontro", Toast.LENGTH_LONG).show();
            return false;
        }

        EventAttendance eventAttendance = new EventAttendance();
        eventAttendance.setEventDate_id(eventMeeting.getId());
        eventAttendance.setPerson_id(person.getId());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try {
            Date checkIn = formatter.parse(String.format("%s %s", mCheckInDate.getText(), mCheckInTime.getText()));
            eventAttendance.setCheckIn(checkIn);

            Date checkOut = formatter.parse(String.format("%s %s", mCheckOutDate.getText(), mCheckOutTime.getText()));
            eventAttendance.setCheckOut(checkOut);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        EventAttendanceDao dao = new EventAttendanceDao(db);

        boolean success = (dao.insert(eventAttendance) > 0);

        db.close();
        return success;
        */
        return true;
    }

}
