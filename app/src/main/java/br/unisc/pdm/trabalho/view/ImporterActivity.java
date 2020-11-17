package br.unisc.pdm.trabalho.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.PeopleService;

public class ImporterActivity extends ActionBarActivity {

    private EditText mURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importer);

        mURL = (EditText) findViewById(R.id.tfURL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_importer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_import_people) {
            try {
                ApiFactory<PeopleService> apiFactory = new ApiFactory<>(PeopleService.class);
                PeopleService peopleService = apiFactory.getService();

                // Create a URL for the desired page
                URL url = new URL(mURL.getText().toString());
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;

                while ((str = in.readLine()) != null) {
                    String[] line = str.split(";");

                    Person person = new Person();
                    person.setName(line[0]);
                    person.setEmail(line[1]);
                    person.setRegistration_number(line[2]);

                    //FIXME Considerando que vai enviar a foto para o servidor próprio
                    //String[] filenameParts = line[3].split("/");
                    //String filename = filenameParts[filenameParts.length-1];
                    //person.setPhoto(filename);
                    person.setPhoto(line[3]);

                    peopleService.insert(person);
                }

                Toast.makeText(this, "Pessoas importadas com sucesso.", Toast.LENGTH_LONG).show();
                in.close();
            } catch(MalformedURLException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Erro ao importar pessoas.", Toast.LENGTH_LONG).show();
            } catch(IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Erro ao importar pessoas.", Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
