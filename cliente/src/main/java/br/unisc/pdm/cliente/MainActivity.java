package br.unisc.pdm.cliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.unisc.pdm.cliente.helper.PreferencesHelper;
import br.unisc.pdm.cliente.ws.ApiFactory;
import br.unisc.pdm.cliente.ws.ApiResponse;
import br.unisc.pdm.cliente.ws.EventAttendanceObject;
import br.unisc.pdm.cliente.ws.EventAttendanceService;

public class MainActivity extends ActionBarActivity {

    private String mRegistrationNumber;

    static private final int REQUEST_QRCODE_READER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Se ainda não tem a matrícula da pessoa configurada, abre a activity para configurar
        // os dados do usuário.
        mRegistrationNumber = new PreferencesHelper(this).getRegistrationNumber();
        if (mRegistrationNumber == null || mRegistrationNumber.isEmpty()) {
            startActivity(new Intent(this, UserInfoActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnection();

        // Atualiza a matrícula porque pode ter vindo da tela de edição dos dados
        mRegistrationNumber = new PreferencesHelper(this).getRegistrationNumber();
    }

    public void onReadQrCodeClick(View view) {
        startActivityForResult(new Intent(this, QrCodeReaderActivity.class), REQUEST_QRCODE_READER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_QRCODE_READER:
                if (resultCode == RESULT_OK && checkConnection()) {
                    String eventMeetingId = data.getStringExtra(QrCodeReaderActivity.EXTRA_READER_RESULT);
                    new RegisterAttendanceTask().execute(eventMeetingId);
                }
                break;
        }
    }

    /**
     * Verifica se possui conexão com a internet para acessar web service, ou exibe aviso para usuário.
     */
    private boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, R.string.toast_error_no_network, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class RegisterAttendanceTask extends AsyncTask<String, Void, String> {

        private ProgressDialog mProgress;

        @Override
        protected String doInBackground(String... strings) {
            EventAttendanceService service = new ApiFactory<>(EventAttendanceService.class).getService();
            EventAttendanceObject obj = new EventAttendanceObject();
            obj.event_meeting_id = strings[0];
            obj.registration_number = mRegistrationNumber;
            obj.check_in  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            ApiResponse response;

            try {
                response = service.registerAttendance(obj);
            }
            catch (Exception e) {
                // Pega qualquer erro: rede, formato dos dados, decodificação da resposta, ...
                Log.e("MainActivity", "Erro no WS: " + e.getMessage());
                return e.getMessage();
            }

            return response.getMessage();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = ProgressDialog.show(MainActivity.this, "Aguarde", "Registrando sua presença no evento...", true, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgress.dismiss();

            if (s != null) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
