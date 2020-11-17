package br.unisc.pdm.trabalho.view.person;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.PeopleService;


/**
 * A list fragment representing a list of People. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link PersonDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PersonListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Texto pesquisado na lista.
     */
    private String mSearchText;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = dummyCallbacks;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(long id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks dummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonListFragment() {

    }

    /**
     * Atualiza os dados exibidos na lista, fazendo a consulta novamente no banco de dados.
     */
    public void refresh() {
        ApiFactory<PeopleService> apiFactory = new ApiFactory<>(PeopleService.class);
        PeopleService peopleService = apiFactory.getService();
        List<Person> list = new ArrayList<>();
        if(mSearchText == null || "".equals(mSearchText)) {
            list = peopleService.listPeople();
        } else {
            list = peopleService.select(mSearchText);
        };

        if (list.isEmpty()) {
            Toast.makeText(getActivity(), R.string.toast_no_person, Toast.LENGTH_LONG).show();
        }

        int itemLayout = android.R.layout.simple_list_item_1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            itemLayout = android.R.layout.simple_list_item_activated_1;
        }

        setListAdapter(new ArrayAdapter<>(getActivity(), itemLayout, android.R.id.text1, list));
    }

    public void search(String text) {
        mSearchText = text;
        refresh();
    }

    @Override
    public void onViewCreated(View view, Bundle args) {
        super.onViewCreated(view, args);
        //FIXME Medida paliativa que permite Network operations na thread principal
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Restore the previously serialized activated item position.
        if (args != null && args.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(args.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = dummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        @SuppressWarnings("unchecked")
        Person item = ((ArrayAdapter<Person>) getListAdapter()).getItem(position);
        mActivatedPosition = position;

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(item.getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activate) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activate ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public int getActivatedPosition() {
        return mActivatedPosition;
    }
}
