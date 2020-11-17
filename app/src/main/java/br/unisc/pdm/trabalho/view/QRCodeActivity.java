package br.unisc.pdm.trabalho.view;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.Event;
import br.unisc.pdm.trabalho.database.model.EventMeeting;
import br.unisc.pdm.trabalho.helper.MailHelper;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.EventMeetingService;
import br.unisc.pdm.trabalho.retrofit.EventService;

public class QRCodeActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private Spinner mEvent;
    private Spinner mEventMeeting;
    private ImageView mImageView;
    private Bitmap mQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        mEvent        = (Spinner) findViewById(R.id.spinnerEvent);
        mEventMeeting = (Spinner) findViewById(R.id.spinnerEventMeeting);
        mImageView    = (ImageView) findViewById(R.id.imageView);

        EventService eventService = new ApiFactory<>(EventService.class).getService();
        List<Event> eventList = eventService.listAll();

        ArrayAdapter<Event> adapterEvents = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, eventList);
        adapterEvents.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEvent.setAdapter(adapterEvents);
        mEvent.setOnItemSelectedListener(this);

        mEventMeeting.setOnItemSelectedListener(this);
        mEventMeeting.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            if (mQRCode == null) {
                Toast.makeText(this, R.string.toast_no_qrcode, Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    File root = Environment.getExternalStorageDirectory();
                    if (root.canWrite()) {
                        File pic = new File(root, "qrcode.png");
                        FileOutputStream out = new FileOutputStream(pic);
                        mQRCode.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();

                        MailHelper.sendEmail(this, "", "QR Code", Uri.fromFile(pic));
                    }
                    else {
                        Toast.makeText(this, R.string.error_generate_report, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.e("BROKEN", "Could not write file " + e.getMessage());
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void generateQRCode(EventMeeting em) {
        // this is a small sample use of the QRCodeEncoder class from zxing
        try {
            String content = String.valueOf(em.getId());
            // generate a 150x150 QR code
            mQRCode = encodeAsBitmap(content, BarcodeFormat.QR_CODE, 200, 200);
            if (mQRCode != null) {
                mImageView.setImageBitmap(mQRCode);
                mImageView.setVisibility(View.VISIBLE);
            }
        } catch (WriterException e) { //eek
            e.printStackTrace();
        }
    }

    private Bitmap encodeAsBitmap(String contents,
                                 BarcodeFormat format,
                                 int desiredWidth,
                                 int desiredHeight) throws WriterException {

        Hashtable<EncodeHintType,Object> hints = null;
        hints = new Hashtable<EncodeHintType,Object>(2);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //FIXME modifiquei isso

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (mEvent == adapterView) {
            Event e = (Event) adapterView.getItemAtPosition(pos);
            List<EventMeeting> meetings = e.getEventMeeting();

            ArrayAdapter<EventMeeting> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, meetings);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventMeeting.setAdapter(adapter);
            mEventMeeting.setEnabled(true);

            mQRCode = null;
            mImageView.setVisibility(View.INVISIBLE);
        }
        else {
            EventMeeting em = (EventMeeting) adapterView.getItemAtPosition(pos);
            generateQRCode(em);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mQRCode = null;
        mImageView.setVisibility(View.INVISIBLE);

        if (mEvent == adapterView) {
            mEventMeeting.setEnabled(false);
        }
    }
}
