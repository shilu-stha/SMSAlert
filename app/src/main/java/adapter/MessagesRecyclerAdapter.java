package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shilu.leapfrog.smsalert.R;
import com.shilu.leapfrog.smsalert.components.ClickListener;
import com.shilu.leapfrog.smsalert.components.Messages;
import com.shilu.leapfrog.smsalert.utils.Converter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/8/15
 */
public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.MessagesViewHolder> {

    List<Messages> list;
    static Context context;
    static ClickListener mListener;

    public MessagesRecyclerAdapter(List<Messages> list) {
        this.list = list;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_card_view, parent, false);

        return new MessagesViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        Messages messageEntry = list.get(position);
        String mBody = messageEntry.MESSAGE_BODY;
        String mName = messageEntry.CONTACTS_NAME;
        String mDate = messageEntry.DATE_TIME;

        if (TextUtils.isEmpty(mName)) {
            //if contact name not present show number
            mName = messageEntry.CONTACTS_NUMBER;
        }

        holder.txtMessage.setText(mBody);
        holder.txtNum.setText(mName);
        holder.txtDate.setText(Converter.DateConverter(Long.parseLong(mDate)));
    }

    public void setClickListener(ClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.card_view_messages_date)
        protected TextView txtDate;
        @InjectView(R.id.card_view_messages_num)
        protected TextView txtNum;
        @InjectView(R.id.card_view_messages_text)
        protected TextView txtMessage;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

}

