package br.unisc.pdm.cliente;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.unisc.pdm.cliente.helper.PreferencesHelper;


public class UserInfoActivity extends ActionBarActivity {

    private EditText mEditRegistrationId;
    private PreferencesHelper mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mPreferences = new PreferencesHelper(this);
        mEditRegistrationId = (EditText) findViewById(R.id.editRegistrationId);

        String regId = mPreferences.getRegistrationNumber();
        if (regId != null) {
            mEditRegistrationId.setText(regId);
        }
    }

    public void onSaveButtonClicked(View view) {
        String registrationId = mEditRegistrationId.getText().toString();

        if (mPreferences.setRegistrationNumber(registrationId)) {
            Toast.makeText(this, R.string.toast_reg_number_saved, Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, R.string.toast_reg_number_save_error, Toast.LENGTH_SHORT).show();
        }
    }

}
