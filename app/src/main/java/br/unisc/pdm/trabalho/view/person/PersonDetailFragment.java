package br.unisc.pdm.trabalho.view.person;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.PeopleService;
import br.unisc.pdm.trabalho.view.helper.BitmapLoader;


/**
 * A fragment representing a single Person detail screen.
 * This fragment is either contained in a {@link PersonListActivity}
 * in two-pane mode (on tablets) or a {@link PersonDetailActivity}
 * on handsets.
 */
public class PersonDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_PERSON_ID = "person_id";

    /**
     * The content this fragment is presenting.
     */
    private Person mPerson;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FIXME Medida paliativa que permite Network operations na thread principal
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (getArguments().containsKey(ARG_PERSON_ID)) {
            ApiFactory<PeopleService> apiFactory = new ApiFactory<>(PeopleService.class);
            PeopleService peopleService = apiFactory.getService();
            mPerson = peopleService.get(getArguments().getLong(ARG_PERSON_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_person_detail, container, false);

        if (mPerson != null) {
            ImageView mViewPhoto = ((ImageView) rootView.findViewById(R.id.viewPhoto));

            String photoUrl = String.format("http://jose.oneweb.com.br/app/webroot/photos/%d/%s.jpg", mPerson.getId(), mPerson.getRegistration_number());
            Picasso
                    .with(getActivity())
                    .load(photoUrl)
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(mViewPhoto);

            ((TextView) rootView.findViewById(R.id.viewName)).setText(mPerson.getName());
            ((TextView) rootView.findViewById(R.id.viewRegistrationNumber)).setText(mPerson.getRegistration_number());
            ((TextView) rootView.findViewById(R.id.viewEmail)).setText(mPerson.getEmail());
        }

        return rootView;
    }

}
