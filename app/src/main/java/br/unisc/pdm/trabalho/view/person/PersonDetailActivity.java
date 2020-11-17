package br.unisc.pdm.trabalho.view.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.model.Person;

/**
 * An activity representing a single Person detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PersonListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link PersonDetailFragment}.
 */
public class PersonDetailActivity extends ActionBarActivity implements PersonDeleteDialogFragment.Callbacks {

    public static final int REQUEST_EDIT_PERSON = 1;

    private long mPersonId;

    private boolean mNeedRefresh = false;

    PersonDetailFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        mPersonId = getIntent().getLongExtra(PersonDetailFragment.ARG_PERSON_ID, 0);

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
            case REQUEST_EDIT_PERSON:
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
        arguments.putLong(PersonDetailFragment.ARG_PERSON_ID, mPersonId);

        mFragment = new PersonDetailFragment();
        mFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.person_detail_container, mFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_person_detail, menu);
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
                Intent intent = new Intent(this, PersonEditActivity.class);
                intent.putExtra(PersonEditActivity.ARG_PERSON_ID, mPersonId);
                startActivityForResult(intent, REQUEST_EDIT_PERSON);
                break;
            case R.id.action_delete:
                Bundle args = new Bundle();
                args.putLong(PersonDeleteDialogFragment.ARG_PERSON_ID, mPersonId);
                PersonDeleteDialogFragment fragment = new PersonDeleteDialogFragment();
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "confirm_delete");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onPersonDeleted(Person person) {
        finish();
    }
}
