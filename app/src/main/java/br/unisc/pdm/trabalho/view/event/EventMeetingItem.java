package br.unisc.pdm.trabalho.view.event;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.unisc.pdm.trabalho.database.model.EventMeeting;

public class EventMeetingItem {

    private Calendar date = Calendar.getInstance();
    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();

    public EventMeetingItem() {
        this.date = Calendar.getInstance();
        this.startTime = Calendar.getInstance();
        this.startTime.set(Calendar.HOUR_OF_DAY, 0);
        this.startTime.set(Calendar.MINUTE, 0);

        this.endTime = Calendar.getInstance();
        this.endTime.set(Calendar.HOUR_OF_DAY, 0);
        this.endTime.set(Calendar.MINUTE, 0);
    }

    public EventMeetingItem(EventMeeting eventMeeting) {
        if(eventMeeting.getDate() != null) {
            this.date.setTime(eventMeeting.getDate());
        } else {
            this.date.setTime(zeroInstance().getTime());
        }

        if(eventMeeting.getStart_time() != null) {
            this.startTime.setTime(eventMeeting.getStart_time());
        } else {
            this.startTime.setTime(zeroInstance().getTime());
        }

        if(eventMeeting.getEnd_time() != null) {
            this.endTime.setTime(eventMeeting.getEnd_time());
        } else {
            this.endTime.setTime(zeroInstance().getTime());
        }
    }

    public EventMeetingItem(Calendar selectedDate, Calendar selectedStartTime, Calendar selectedEndTime) {
        if(selectedDate != null) {
            this.date = selectedDate;
        } else {
            this.date = Calendar.getInstance();
        }

        if(selectedStartTime != null) {
            this.startTime = selectedStartTime;
        } else {
            this.startTime = Calendar.getInstance();
            this.startTime.set(Calendar.HOUR_OF_DAY, 0);
            this.startTime.set(Calendar.MINUTE, 0);
        }

        if(selectedEndTime != null) {
            this.endTime = selectedEndTime;
        } else {
            this.endTime = Calendar.getInstance();
            this.endTime.set(Calendar.HOUR_OF_DAY, 0);
            this.endTime.set(Calendar.MINUTE, 0);
        }
    }

    private Calendar zeroInstance() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);

        return c;
    }

    public Date getDate() {
        return date.getTime();
    }

    public Date getStartTime() {
        return startTime.getTime();
    }

    public Date getEndTime() {
        return endTime.getTime();
    }

    public LinearLayout createViewItem(Context context) {
        final LinearLayout eventMeeting = new LinearLayout(context);
        eventMeeting.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        eventMeeting.setOrientation(LinearLayout.HORIZONTAL);

        final TextView eventMeetingDate = new TextView(context);
        eventMeetingDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        updateLabelDate(eventMeetingDate, date);
        eventMeeting.addView(eventMeetingDate);

        final TextView eventMeetingStartTime = new TextView(context);
        eventMeetingStartTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        updateLabelTime(eventMeetingStartTime, startTime);
        eventMeeting.addView(eventMeetingStartTime);

         /*
        TextView labelToTime = new TextView(this);
        labelToTime.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.10f));
        labelToTime.setTextAppearance(this, R.style.TextAppearance_AppCompat_Medium);
        labelToTime.setText(getString(R.string.label_to_time));
        eventMeeting.addView(labelToTime);
    */

        final TextView eventMeetingEndTime = new TextView(context);
        eventMeetingEndTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        updateLabelTime(eventMeetingEndTime, endTime);
        eventMeeting.addView(eventMeetingEndTime);

        return eventMeeting;
    }

    public LinearLayout createItem(Context context) {
        final LinearLayout eventMeeting = new LinearLayout(context);
        eventMeeting.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        eventMeeting.setOrientation(LinearLayout.HORIZONTAL);

        final EditText eventMeetingDate = new EditText(context);
        eventMeetingDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        updateLabelDate(eventMeetingDate, date);

        //Picker Date
        final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate(eventMeetingDate, date);
            }
        };
        eventMeetingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(view.getContext(), dateDialog, date.get(Calendar.YEAR),
                            date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        eventMeeting.addView(eventMeetingDate);


        final EditText eventMeetingStartTime = new EditText(context);
        eventMeetingStartTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        updateLabelTime(eventMeetingStartTime, startTime);

        //Picker Start Time
        final TimePickerDialog.OnTimeSetListener startTimeDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startTime.set(Calendar.MINUTE, minute);
                updateLabelTime(eventMeetingStartTime, startTime);
            }
        };
        eventMeetingStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new TimePickerDialog(view.getContext(), startTimeDialog, startTime.get(Calendar.HOUR_OF_DAY),
                            startTime.get(Calendar.MINUTE), true).show();
                }
            }
        });
        eventMeeting.addView(eventMeetingStartTime);

         /*
        TextView labelToTime = new TextView(this);
        labelToTime.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.10f));
        labelToTime.setTextAppearance(this, R.style.TextAppearance_AppCompat_Medium);
        labelToTime.setText(getString(R.string.label_to_time));
        eventMeeting.addView(labelToTime);
    */

        final EditText eventMeetingEndTime = new EditText(context);
        eventMeetingEndTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        updateLabelTime(eventMeetingEndTime, endTime);

        //Picker End Time
        final TimePickerDialog.OnTimeSetListener endTimeDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endTime.set(Calendar.MINUTE, minute);
                updateLabelTime(eventMeetingEndTime, endTime);
            }
        };
        eventMeetingEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new TimePickerDialog(view.getContext(), endTimeDialog, endTime.get(Calendar.HOUR_OF_DAY),
                            endTime.get(Calendar.MINUTE), true).show();
                }
            }
        });
        eventMeeting.addView(eventMeetingEndTime);

        return eventMeeting;
    }

    private void updateLabelDate(TextView textView, Calendar calendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textView.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabelTime(TextView textView, Calendar calendar) {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textView.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabelDate(EditText editText, Calendar calendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabelTime(EditText editText, Calendar calendar) {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        editText.setText(sdf.format(calendar.getTime()));
    }

}
