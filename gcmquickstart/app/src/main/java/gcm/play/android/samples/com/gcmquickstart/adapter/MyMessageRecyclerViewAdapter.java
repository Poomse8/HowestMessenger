package gcm.play.android.samples.com.gcmquickstart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import gcm.play.android.samples.com.gcmquickstart.R;
import gcm.play.android.samples.com.gcmquickstart.models.Message;
import gcm.play.android.samples.com.gcmquickstart.view.DetailFragment;
import gcm.play.android.samples.com.gcmquickstart.view.MainFragment;

/**
 * Created by Nicol on 5/05/2016.
 */
public class MyMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageRecyclerViewAdapter.MessageViewHolder>{

    private MainFragment.MainFragmentListener mListener;
    private ArrayList<Message> messages;
    private Context context;

    public MyMessageRecyclerViewAdapter(Context context, ArrayList<Message> messages, MainFragment.MainFragmentListener mListener){
        this.messages = messages;
        this.context =  context;
        this.mListener = mListener;
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message huidigeMessage = messages.get(position);
        String dateFormat = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,Locale.getDefault());

        holder.tvTitle.setText(huidigeMessage.getMessageTitle());
        //holder.tvMessage.setText(huidigeMessage.getMessageText());
        holder.tvAuteur.setText(huidigeMessage.getMessageAuteur());
        //holder.tvTijd.setText(huidigeMessage.getTimeStamp().toString());
        holder.tvTijd.setText(sdf.format(huidigeMessage.getTimeStamp()));

        switch (huidigeMessage.getMessageVak()){
            case "Mobile App Developement":
                holder.ivIcon.setImageResource(R.drawable.message_icon_programming);
                break;
            case "Server Side Advanced":
                holder.ivIcon.setImageResource(R.drawable.message_icon_serverside);
                break;
            case "Project":
                holder.ivIcon.setImageResource(R.drawable.message_icon_des);
                break;
            case "Datacom": case "Datacenter Technology":
                holder.ivIcon.setImageResource(R.drawable.message_icon_infrastructure);
                break;
            case "New Media": case "Video en Audio":
                holder.ivIcon.setImageResource(R.drawable.message_icon_design);
                break;
            default:
                holder.ivIcon.setImageResource(R.drawable.message_icon);
                break;
        }


    }

    /*@Override
    public int getItemCount() {
        return Message.Dummy().size();
    }*/

    @Override
    public int getItemCount() { return messages.size(); }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        //public TextView tvMessage;
        public TextView tvAuteur;
        public TextView tvTijd;
        public ImageView ivIcon;
        public ImageButton ibDelete;

        public MessageViewHolder(View row) {
            super(row);

            tvTitle = (TextView) row.findViewById(R.id.tvTitle);
            //tvMessage = (TextView) row.findViewById(R.id.tvMessage);
            tvAuteur = (TextView) row.findViewById(R.id.tvAuteur);
            tvTijd = (TextView) row.findViewById(R.id.tvTijd);
            ivIcon = (ImageView) row.findViewById(R.id.ivIcon);
            ibDelete = (ImageButton) row.findViewById(R.id.ibDelete);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    mListener.onMessageDelete(index, context);
                }
            });

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    //Toast.makeText(context, messages.get(index).getMessageTitle(), Toast.LENGTH_SHORT).show();
                    mListener.onListItemSelected(messages.get(index));
                }
            });
        }
    }
}
