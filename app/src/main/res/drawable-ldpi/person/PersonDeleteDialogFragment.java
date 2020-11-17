package br.unisc.pdm.trabalho.view.person;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.dao.PersonDao;
import br.unisc.pdm.trabalho.database.model.Person;


public class PersonDeleteDialogFragment extends DialogFragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_PERSON_ID = "person_id";

    /**
     * A pessoa que deve ser excluída.
     */
    private Person mPerson;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onPersonDeleted(Person person);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PERSON_ID)) {
            SQLiteDatabase db = new DatabaseHelper(getActivity()).getReadableDatabase();
            mPerson = new PersonDao(db).get(getArguments().getLong(ARG_PERSON_ID));
            db.close();
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(mPerson.getName())
                .setMessage(R.string.dialog_confirm_delete_person)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
                        new PersonDao(db).delete(mPerson);
                        db.close();

                        // Notifica para a activity que a pessoa foi excluída
                        mCallbacks.onPersonDeleted(mPerson);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, nothing to do
                    }
                });

        // Create the AlertDialog object
        return builder.create();
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

        // Reset the active callbacks interface
        mCallbacks = null;
    }

}
