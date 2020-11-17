package br.unisc.pdm.trabalho.view.event;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.unisc.pdm.trabalho.R;
import br.unisc.pdm.trabalho.database.DatabaseHelper;
import br.unisc.pdm.trabalho.database.model.Event;
import br.unisc.pdm.trabalho.database.model.EventMeeting;
import br.unisc.pdm.trabalho.retrofit.ApiFactory;
import br.unisc.pdm.trabalho.retrofit.EventService;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_EVENT_ID = "event_id";

    /**
     * The content this fragment is presenting.
     */
    private Event mEvent;

    private List<EventMeeting> mEventMeetings = new ArrayList<>();
    private List<EventMeetingItem> eventMeetingItems = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_EVENT_ID)) {
            ApiFactory<EventService> apiFactory = new ApiFactory<>(EventService.class);
            EventService eventService = apiFactory.getService();
            mEvent = eventService.get(getArguments().getLong(ARG_EVENT_ID));

            mEventMeetings = mEvent.getEventMeeting();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mEvent != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            ((TextView) rootView.findViewById(R.id.viewName))       .setText(mEvent.getName());
            if(mEvent.getStart_date() != null) {
                ((TextView) rootView.findViewById(R.id.viewStartDate))  .setText(formatter.format(mEvent.getStart_date()));
            }

            if(mEvent.getEnd_date() != null) {
                ((TextView) rootView.findViewById(R.id.viewEndDate))    .setText(formatter.format(mEvent.getEnd_date()));
            }

            ((TextView) rootView.findViewById(R.id.viewDescription)).setText(mEvent.getDescription());

            if(mEventMeetings != null) {
                LinearLayout eventMeetings = (LinearLayout) rootView.findViewById(R.id.viewMeetings);

                for(EventMeeting eventMeeting : mEventMeetings) {
                    EventMeetingItem item = new EventMeetingItem(eventMeeting);
                    eventMeetings.addView(item.createViewItem(getActivity()));
                    eventMeetingItems.add(item);
                }
            }
        }

        return rootView;
    }
}
