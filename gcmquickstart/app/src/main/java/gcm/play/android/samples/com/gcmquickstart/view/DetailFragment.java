package gcm.play.android.samples.com.gcmquickstart.view;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;

import gcm.play.android.samples.com.gcmquickstart.R;
import gcm.play.android.samples.com.gcmquickstart.adapter.MyMessageRecyclerViewAdapter;
import gcm.play.android.samples.com.gcmquickstart.models.Message;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment{

    private TextView tvTitle;
    private TextView tvVak;
    private TextView tvMessage;
    private TextView tvAuteur;
    private TextView tvTimestamp;
    private RelativeLayout header;
    private RelativeLayout footer;
    private LinearLayout extraFooter;


    private static final String MESSAGE_KEY = "message";
    private Message message;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Message message) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MESSAGE_KEY, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        tvVak = (TextView) v.findViewById(R.id.tvVak);
        tvMessage = (TextView) v.findViewById(R.id.tvMessage);
        tvAuteur = (TextView) v.findViewById(R.id.tvAuteur);
        tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);
        header = (RelativeLayout) v.findViewById(R.id.header);
        footer = (RelativeLayout) v.findViewById(R.id.footer);
        extraFooter = (LinearLayout) v.findViewById(R.id.extraFooter);

        message = getArguments().getParcelable(MESSAGE_KEY);

        if(message.getMessageVak() == null){
            header.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            footer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            extraFooter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        }
        else{
            switch (message.getMessageVak()){
                case "Mobile App Developement":
                    header.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Apps));
                    footer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Apps));
                    extraFooter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Apps));
                    break;
                case "Server Side Advanced":
                    header.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Backend));
                    footer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Backend));
                    extraFooter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Backend));
                    break;
                case "Project":
                    header.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.DES));
                    footer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.DES));
                    extraFooter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.DES));
                    break;
                case "Datacom": case "Datacenter Technology":
                    header.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Infrastructure));
                    footer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Infrastructure));
                    extraFooter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Infrastructure));
                    break;
                case "New Media": case "Video en Audio":
                    header.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Design));
                    footer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Design));
                    extraFooter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Design));
                    break;
                case "Algemeen": default:
                    header.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    footer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    extraFooter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    break;
            }
        }


        tvTitle.setText(message.getMessageTitle());
        tvVak.setText(message.getMessageVak());
        tvMessage.setText(message.getMessageText());
        tvAuteur.setText(message.getMessageAuteur());


        String dateFormat = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        tvTimestamp.setText(sdf.format(message.getTimeStamp()));

        return v;
    }

}
