package br.unisc.pdm.trabalho.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.model.Event;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.helper.CsvFileWriter;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.EventAttendanceService;
import br.unisc.pdm.trabalho.retrofit.PeopleService;
import br.unisc.pdm.trabalho.retrofit.json.Present;

public class ExportPeopleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_people);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_export_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_export_people) {
            ApiFactory<PeopleService> apiFactory = new ApiFactory<>(PeopleService.class);
            PeopleService peopleService = apiFactory.getService();

            List<Person> people = new ArrayList<>();
            try {
                people = peopleService.listPeople();
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            CsvFileWriter csvFileWriter = new CsvFileWriter();
            String csv = csvFileWriter.writePeopleCsvFile(getApplicationContext(), people);

            File downloadDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/people.csv");
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendEmail(File file) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setAction(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("message/rfc822"); //rfc822 para forçar a abrir somente clientes de e-mail
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { "" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Relatório de Pessoas");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        startActivity(Intent.createChooser(emailIntent, "Send Email Using: "));
    }
}
