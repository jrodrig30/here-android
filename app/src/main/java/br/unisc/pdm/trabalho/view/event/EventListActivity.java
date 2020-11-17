package br.unisc.pdm.trabalho.view.event;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import br.unisc.pdm.trabalho.MainActivity;
import br.unisc.pdm.trabalho.R;


/**
 * An activity representing a list of Events. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EventDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link EventListFragment} and the item details
 * (if present) is a {@link EventDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link EventListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class EventListActivity extends ActionBarActivity implements EventListFragment.Callbacks {

    public static final int REQUEST_EDIT_EVENT    = 1;
    public static final int REQUEST_DETAILS_EVENT = 2;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private EventListFragment mListFragment;
    private EventDetailFragment mDetailFragment;

    private boolean mNeedRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListFragment = ((EventListFragment) getSupportFragmentManager().findFragmentById(R.id.event_list));

        if (findViewById(R.id.event_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/values-large and res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the 'activated' state when touched.
            mListFragment.setActivateOnItemClick(true);
        }

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getString(R.string.title_search_results, query));
            mListFragment.search(query);
        }
        else {
            // Se não houver pesquisa, traz a lista completa
            mListFragment.refresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListFragment == null) {
            return;
        }

        if (mNeedRefresh) {
            // Atualiza a lista exibida (se criou uma nova pessoa ou editou o nome de uma já existente,
            // já exibe imediatamente as modificações).
            mListFragment.refresh();

            // Quando exibido com dois paineis, readiciona o Fragment dos detalhes para atualizar
            // os dados da pessoa (quando volta da edição, por exemplo)
            if (mTwoPane && mDetailFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(mDetailFragment)
                        .commit();

                // Recria o menu (oculta opções editar e excluir)
                supportInvalidateOptionsMenu();
            }

            mNeedRefresh = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_list, menu);

        // Se existe um item selecionado, exibe as ações para ele. Senão, oculta.
        if (mTwoPane) {
            boolean selection = mListFragment.getActivatedPosition() != ListView.INVALID_POSITION;
            menu.findItem(R.id.action_edit).setVisible(selection);
            menu.findItem(R.id.action_delete).setVisible(selection);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create:
                startActivityForResult(new Intent(this, EventEditActivity.class), REQUEST_EDIT_EVENT);
                break;
            case R.id.action_search:
                onSearchRequested();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, EventEditActivity.class);
                intent.putExtra(EventEditActivity.ARG_EVENT_ID, mListFragment.getActivatedPosition());
                startActivityForResult(intent, REQUEST_EDIT_EVENT);
                break;
            case R.id.action_delete:
                Bundle args = new Bundle();
                args.putLong(EventDeleteDialogFragment.ARG_EVENT_ID, mListFragment.getActivatedPosition());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_EVENT:
            case REQUEST_DETAILS_EVENT:
                mNeedRefresh = true;
                break;
        }
    }

    /**
     * Callback method from {@link EventListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(EventDetailFragment.ARG_EVENT_ID, id);

            mDetailFragment = new EventDetailFragment();
            mDetailFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.event_detail_container, mDetailFragment)
                    .commit();

            // Recria o menu (exibe opções editar e excluir)
            supportInvalidateOptionsMenu();
        }
        else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EventDetailActivity.class);
            detailIntent.putExtra(EventDetailFragment.ARG_EVENT_ID, id);
            startActivityForResult(detailIntent, REQUEST_DETAILS_EVENT);
        }
    }
}
