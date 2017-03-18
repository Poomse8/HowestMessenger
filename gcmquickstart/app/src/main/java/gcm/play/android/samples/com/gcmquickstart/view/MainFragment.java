package gcm.play.android.samples.com.gcmquickstart.view;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import gcm.play.android.samples.com.gcmquickstart.R;
import gcm.play.android.samples.com.gcmquickstart.adapter.MyMessageRecyclerViewAdapter;
import gcm.play.android.samples.com.gcmquickstart.models.Message;
import gcm.play.android.samples.com.gcmquickstart.service.QuickstartPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.MainFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private RecyclerView rvMessages;
    private MyMessageRecyclerViewAdapter myMessageRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvNiets;
    private ArrayList<Message> messages;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    //private String mParam1;

    private MainFragmentListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //list met messages ophalen en in var steken
        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences(QuickstartPreferences.PREFERENCE_NAME, 0);

        String messagesJson = prefs.getString(QuickstartPreferences.MESSAGES_LIST_KEY, null);
        if(messagesJson == null) messages = new ArrayList<>();
        else messages = gson.fromJson(messagesJson, new TypeToken<ArrayList<Message>>(){}.getType());

        /// / Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        tvNiets = (TextView) v.findViewById(R.id.tvNiets);
        if(messages.size() == 0) tvNiets.setVisibility(View.VISIBLE);

        mLayoutManager = new LinearLayoutManager(getContext());

        rvMessages = (RecyclerView) v.findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(mLayoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());

        //myMessageRecyclerViewAdapter = new MyMessageRecyclerViewAdapter(v.getContext(), Message.Dummy(), mListener);
        myMessageRecyclerViewAdapter = new MyMessageRecyclerViewAdapter(v.getContext(), messages, mListener);
        rvMessages.setAdapter(myMessageRecyclerViewAdapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentListener) {
            mListener = (MainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MainFragmentListener {
        // TODO: Update argument type and name
        void onListItemSelected(Message message);
        void onMessageDelete(int index, Context context);
    }
}
