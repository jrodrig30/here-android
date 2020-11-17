package br.unisc.pdm.trabalho.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.unisc.pdm.trabalho.MainActivity;
import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.Event;
import br.unisc.pdm.trabalho.database.model.EventAttendance;
import br.unisc.pdm.trabalho.helper.CsvFileWriter;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.EventAttendanceService;
import br.unisc.pdm.trabalho.retrofit.EventService;
import br.unisc.pdm.trabalho.retrofit.json.Fault;
import br.unisc.pdm.trabalho.retrofit.json.FaultEvent;

public class ReportNotAttendanceActivity extends ActionBarActivity {

    private Spinner mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_not_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //FIXME Medida paliativa que permite Network operations na thread principal
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mEvent = (Spinner) findViewById(R.id.editEvent);

        ApiFactory<EventService> apiFactory = new ApiFactory<>(EventService.class);
        EventService eventService = apiFactory.getService();
        List<Event> events = eventService.listAll();

        ArrayAdapter<Event> adapterEventMeeting = new ArrayAdapter<Event>(this, android.R.layout.simple_dropdown_item_1line, events);
        adapterEventMeeting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEvent.setAdapter(adapterEventMeeting);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_not_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_generate_report_not_attendance) {
            ApiFactory<EventAttendanceService> apiFactory = new ApiFactory<>(EventAttendanceService.class);
            EventAttendanceService eventAttendanceService = apiFactory.getService();

            Object selectedItem = mEvent.getSelectedItem();
            if(selectedItem == null) {
                Toast.makeText(this, "Selecione um evento", Toast.LENGTH_LONG).show();
            } else {
                List<List<Fault>> notAttendances = new ArrayList<>();
                try {
                    notAttendances = eventAttendanceService.listFaultsByEvent(((Event) selectedItem).getId());
                } catch(Exception ex) {
                    ex.printStackTrace();
                }

                CsvFileWriter csvFileWriter = new CsvFileWriter();
                String csv = csvFileWriter.writeFaultsCsvFile(getApplicationContext(), notAttendances);

                File downloadDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/report_faults.csv");
                FileWriter writer = null;
                try {
                    writer = new FileWriter(downloadDir);
                    writer.write(csv);
                    writer.close();

                    sendEmail(downloadDir);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.error_generate_report, Toast.LENGTH_LONG).show();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendEmail(File file) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setAction(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("message/rfc822"); //rfc822 para forçar a abrir somente clientes de e-mail
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { "" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Relatório de Presenças");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        startActivity(Intent.createChooser(emailIntent, "Send Email Using: "));
    }

}
