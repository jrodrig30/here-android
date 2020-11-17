package br.unisc.pdm.trabalho.view.person;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.ApiResponse;
import br.unisc.pdm.trabalho.retrofit.PeopleService;
import br.unisc.pdm.trabalho.view.helper.BitmapLoader;


public class PersonEditActivity extends ActionBarActivity {

    public static final String ARG_PERSON_ID = "person_id";
    private static final int REQUEST_TAKE_PHOTO = 1;

    private long mPersonId;
    private String mPersonPhoto;
    private File photoFile;

    private EditText mEditName;
    private EditText mEditEmail;
    private EditText mEditRegistrationNumber;
    private ImageView mEditPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BitmapLoader bitmapLoader = new BitmapLoader();

        mEditPhoto   = (ImageView) findViewById(R.id.editPhoto);
        mEditName    = (EditText) findViewById(R.id.editName);
        mEditRegistrationNumber = (EditText) findViewById(R.id.editRegistrationNumber);
        mEditEmail   = (EditText) findViewById(R.id.editEmail);

        Intent intent = getIntent();
        if (intent.hasExtra(ARG_PERSON_ID)) { //Editando o cadastro
            mPersonId = intent.getLongExtra(ARG_PERSON_ID, 0);

            ApiFactory<PeopleService> apiFactory = new ApiFactory<>(PeopleService.class);
            PeopleService peopleService = apiFactory.getService();
            Person person = peopleService.get(mPersonId);

            mPersonPhoto = person.getPhoto();

            if(mPersonPhoto == null || "".equals(mPersonPhoto)) {
                bitmapLoader.loadDefaultPhoto(mEditPhoto);
            } else {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = new File(storageDir.getAbsolutePath() + "//" + mPersonPhoto);
                if(photoFile.exists()) {
                    bitmapLoader.loadBitmap(photoFile.getAbsolutePath(), mEditPhoto);
                } else {
                    photoFile = null;
                    bitmapLoader.loadDefaultPhoto(mEditPhoto); //Existe algo na foto da pessoa, mas ela não foi encontrada
                }
            }

            mEditName.setText(person.getName());
            mEditEmail.setText(person.getEmail());
            mEditRegistrationNumber.setText(person.getRegistration_number());

            setTitle(getString(R.string.title_editing_person, person.getName()));
        } else {
            bitmapLoader.loadDefaultPhoto(mEditPhoto);
        }
    }

    //On click to change photo
    public void changePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast toast = Toast.makeText(this, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile)); //Diz onde a imagem vai ser salva
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            BitmapLoader bitmapLoader = new BitmapLoader();
            bitmapLoader.loadBitmap(photoFile.getAbsolutePath(), ((ImageView) findViewById(R.id.editPhoto)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_person_edit, menu);
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
                    String msg = getString(R.string.toast_person_saved, mEditName.getText().toString());
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private boolean save() {
        Person person = new Person();
        person.setName(mEditName.getText().toString());
        person.setEmail(mEditEmail.getText().toString());
        person.setRegistration_number(mEditRegistrationNumber.getText().toString());
        person.setPhoto("");

        if(photoFile != null) {
            int size = (int) photoFile.length();
            byte[] bytes = new byte[size];

            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(photoFile));
                buf.read(bytes, 0, bytes.length);
                buf.close();
                person.setPhoto(Base64.encodeToString(bytes, Base64.NO_WRAP));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        ApiFactory<PeopleService> factory = new ApiFactory<>(PeopleService.class);
        PeopleService peopleService = factory.getService();
        boolean success = false;

        try {
            if (mPersonId > 0) {
                person.setId(mPersonId);
                ApiResponse response = peopleService.update(person, mPersonId);
                success = response.getStatus();
            } else {
                ApiResponse response = peopleService.insert(person);
                success = response.getStatus();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return success;
    }

}