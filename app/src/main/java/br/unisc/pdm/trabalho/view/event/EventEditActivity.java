package br.unisc.pdm.trabalho.view.event;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.Event;
import br.unisc.pdm.trabalho.database.model.EventMeeting;
import br.unisc.pdm.trabalho.database.model.Time;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.ApiResponse;
import br.unisc.pdm.trabalho.retrofit.EventMeetingService;
import br.unisc.pdm.trabalho.retrofit.EventService;


public class EventEditActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String ARG_EVENT_ID = "event_id";
    private static final String STATE_SAVE_ITEMS = "event_meeting_items";

    private long mEventId;

    private EditText mEditName;
    private EditText mEditStartDate;
    private EditText mEditEndDate;
    private EditText mEditDescription;
    private Button   mAddDates;

    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        mEditName        = (EditText) findViewById(R.id.editEventName);
        mEditStartDate   = (EditText) findViewById(R.id.editEventStartDate);
        mEditEndDate     = (EditText) findViewById(R.id.editEventEndDate);
        mEditDescription = (EditText) findViewById(R.id.editEventDescription);
        mAddDates        = (Button)   findViewById(R.id.btAdicionarDatas);

        mAddDates.setOnClickListener(this);

        //Picker Start Date
        final DatePickerDialog.OnDateSetListener startDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startDate.set(Calendar.YEAR, year);
                startDate.set(Calendar.MONTH, monthOfYear);
                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(mEditStartDate, startDate);
            }
        };
        mEditStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(view.getContext(), startDateDialog, startDate.get(Calendar.YEAR),
                            startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        //Picker End Date
        final DatePickerDialog.OnDateSetListener endDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, monthOfYear);
                endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(mEditEndDate, endDate);
            }
        };
        mEditEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(view.getContext(), endDateDialog, endDate.get(Calendar.YEAR),
                            endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(ARG_EVENT_ID)) {
            mEventId = intent.getLongExtra(ARG_EVENT_ID, 0);

            ApiFactory<EventService> apiFactory = new ApiFactory<>(EventService.class);
            EventService eventService = apiFactory.getService();
            Event event = eventService.get(mEventId);;
            List<EventMeeting> eventMeetings = event.getEventMeeting();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (event != null) {
                mEditName.setText(event.getName());
                mEditStartDate.setText(formatter.format(event.getStart_date()));
                mEditEndDate.setText(formatter.format(event.getEnd_date()));
                mEditDescription.setText(event.getDescription());

                setTitle(getString(R.string.title_editing_event, event.getName()));
            }

            if (eventMeetings != null) {
                LinearLayout eventMeetingsList = (LinearLayout) findViewById(R.id.list_event_meetings);
                for (EventMeeting eventMeeting : eventMeetings) {
                    EventMeetingItem item = new EventMeetingItem(eventMeeting);
                    eventMeetingsList.addView(item.createItem(this));
                    items.add(item);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, as long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                if (save()) {
                    String msg = getString(R.string.toast_event_saved, mEditName.getText().toString());
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<ArrayList<Date>> saveItems = new ArrayList<>();

        for (EventMeetingItem item : items) {
            ArrayList<Date> e = new ArrayList<>();
            e.add(item.getDate());
            e.add(item.getDate());
            e.add(item.getDate());
            saveItems.add(e);
        }

        outState.putSerializable(STATE_SAVE_ITEMS, saveItems);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LinearLayout eventMeetingsList = (LinearLayout) findViewById(R.id.list_event_meetings);
        ArrayList<ArrayList<Date>> savedItems =
                (ArrayList<ArrayList<Date>>)savedInstanceState.getSerializable(STATE_SAVE_ITEMS);

        if (savedItems != null) {
            for (ArrayList<Date> item : savedItems) {
                Calendar selectedDate = Calendar.getInstance();
                Calendar selectedStartTime = Calendar.getInstance();
                Calendar selectedEndTime = Calendar.getInstance();

                selectedDate.setTime(item.get(0));
                selectedStartTime.setTime(item.get(1));
                selectedEndTime.setTime(item.get(2));

                EventMeetingItem eventMeeting = new EventMeetingItem(selectedDate, selectedStartTime, selectedEndTime);
                eventMeetingsList.addView(eventMeeting.createItem(this));
                items.add(eventMeeting);
            }
        }
    }

    private List<EventMeetingItem> items = new ArrayList<>();


    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        editText.setText(sdf.format(calendar.getTime()));
    }

    private boolean save() {
        Event event = new Event();
        event.setName(mEditName.getText().toString());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String startDate = mEditStartDate.getText().toString();
            String endDate = mEditEndDate.getText().toString();
            event.setStart_date(formatter.parse(startDate));
            event.setEnd_date(formatter.parse(endDate));
        } catch(ParseException ex) {
            ex.printStackTrace();
        }
        event.setDescription(mEditDescription.getText().toString());

        //Insere novos eventos... não funcionará por enquanto a edição de encontros no evento
        ArrayList<EventMeeting> meetings = new ArrayList<>();
        for(EventMeetingItem item : items) {
            EventMeeting eventMeeting = new EventMeeting();
            eventMeeting.setDate(item.getDate());
            eventMeeting.setStart_time(new Time(item.getStartTime()));
            eventMeeting.setEnd_time(new Time(item.getEndTime()));
            meetings.add(eventMeeting);
        }
        event.setEventMeeting(meetings);

        ApiFactory<EventService> eventServiceApiFactory = new ApiFactory<>(EventService.class);
        EventService eventService = eventServiceApiFactory.getService();

        boolean success;

        if (mEventId > 0) {
            event.setId(mEventId);
            ApiResponse response = eventService.update(event, mEventId);
            success = response.getStatus();
        } else {
            ApiResponse response = eventService.insert(event);
            try {
                mEventId = Long.parseLong(response.getEvent_id());
                success = (mEventId > 0);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                success = false;
            }
        }

        return success;
    }

    // OnClick botão adicionar data
    @Override
    public void onClick(View view) {
        LinearLayout eventMeetings = (LinearLayout) findViewById(R.id.list_event_meetings);

        EventMeetingItem item = new EventMeetingItem();
        items.add(item);

        eventMeetings.addView(item.createItem(this));
    }
}