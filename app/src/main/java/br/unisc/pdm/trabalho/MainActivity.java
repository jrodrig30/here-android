package br.unisc.pdm.trabalho;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.unisc.pdm.trabalho.view.ExportPeopleActivity;
import br.unisc.pdm.trabalho.view.ImporterActivity;
import br.unisc.pdm.trabalho.view.QRCodeActivity;
import br.unisc.pdm.trabalho.view.RegisterEventAttendanceActivity;
import br.unisc.pdm.trabalho.view.ReportAttendanceActivity;
import br.unisc.pdm.trabalho.view.ReportNotAttendanceActivity;
import br.unisc.pdm.trabalho.view.event.EventListActivity;
import br.unisc.pdm.trabalho.view.person.PersonListActivity;
import retrofit.RestAdapter;

public class MainActivity extends ActionBarActivity {
    protected FrameLayout frameLayout;
    protected LinearLayout linearLayout;
    protected List<DrawerItem> dataList;
    protected ListView mDrawerList;
    protected static int position;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    CustomDrawerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FIXME Medida paliativa que permite Network operations na thread principal
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        dataList.add(new DrawerItem(true));
        dataList.add(new DrawerItem(getString(R.string.menu_event), R.drawable.ic_action_event));
        dataList.add(new DrawerItem(getString(R.string.menu_person), R.drawable.ic_action_person));
       // dataList.add(new DrawerItem(getString(R.string.menu_register_attendance), R.drawable.ic_action_register_attendance));
        dataList.add(new DrawerItem(getString(R.string.menu_report_attendance), R.drawable.ic_action_report_attendance));
        dataList.add(new DrawerItem(getString(R.string.menu_report_not_attendance), R.drawable.ic_action_report_not_attendance));
        dataList.add(new DrawerItem(getString(R.string.menu_qrcode), R.drawable.ic_action_qrcode));
        dataList.add(new DrawerItem("Importador", R.drawable.ic_action_download));
        dataList.add(new DrawerItem("Exportar Pessoas", R.drawable.ic_action_download));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {

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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (dataList.get(position).getTitle() == null) {
                SelectItem(position);
            }

        }
    }

    public void SelectItem(int position) {
        switch (position) {
            case 1:
                Intent intentListEvent = (Intent) new Intent(this, EventListActivity.class);
                startActivity(intentListEvent);
                break;
            case 2:
                Intent intentListPerson = (Intent) new Intent(this, PersonListActivity.class);
                startActivity(intentListPerson);
                break;
           /*
            case 3:
                Intent intentRegisterAttendance = (Intent) new Intent(this, RegisterEventAttendanceActivity.class);
                startActivity(intentRegisterAttendance);
                break;
                */
            case 3:
                Intent intentReportAttendance = (Intent) new Intent(this, ReportAttendanceActivity.class);
                startActivity(intentReportAttendance);
                break;
            case 4:
                Intent intentReportNotAttendance = (Intent) new Intent(this, ReportNotAttendanceActivity.class);
                startActivity(intentReportNotAttendance);
                break;
            case 5:
                Intent intentQrCodeGenerator = (Intent) new Intent(this, QRCodeActivity.class);
                startActivity(intentQrCodeGenerator);
                break;
            case 6:
                Intent intentImporter = (Intent) new Intent(this, ImporterActivity.class);
                startActivity(intentImporter);
                break;
            case 7:
                Intent intentExporter = (Intent) new Intent(this, ExportPeopleActivity.class);
                startActivity(intentExporter);
                break;
            default:
                break;
        }


        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
}
