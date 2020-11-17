package br.unisc.pdm.trabalho.view.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.model.Event;


/**
 * An activity representing a single Event detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link EventListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link EventDetailFragment}.
 */
public class EventDetailActivity extends ActionBarActivity implements EventDeleteDialogFragment.Callbacks  {

    public static final int REQUEST_EDIT_EVENT = 1;

    private long mEventId;

    private boolean mNeedRefresh = false;

    EventDetailFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mEventId = getIntent().getLongExtra(EventDetailFragment.ARG_EVENT_ID, 0);

        // savedInstanceState is non-null when there is fragment state saved from previous
        // configurations of this activity (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added to its container so we don't
        // need to manually add it.

        if (savedInstanceState == null) {
            refreshDetails();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_EVENT:
                mNeedRefresh = true;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNeedRefresh) {
            refreshDetails();
            mNeedRefresh = false;
        }
    }

    private void refreshDetails() {
        // Create the detail fragment and add it to the activity using a fragment transaction.
        Bundle arguments = new Bundle();
        arguments.putLong(EventDetailFragment.ARG_EVENT_ID, mEventId);

        mFragment = new EventDetailFragment();
        mFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_detail_container, mFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EventEditActivity.class);
                intent.putExtra(EventEditActivity.ARG_EVENT_ID, mEventId);
                startActivityForResult(intent, REQUEST_EDIT_EVENT);
                break;
            case R.id.action_delete:
                Bundle args = new Bundle();
                args.putLong(EventDeleteDialogFragment.ARG_EVENT_ID, mEventId);
                EventDeleteDialogFragment fragment = new EventDeleteDialogFragment();
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "confirm_delete");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onEventDeleted(Event event) {
        finish();
    }
}
