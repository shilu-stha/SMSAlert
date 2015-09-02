package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shilu.leapfrog.smsalert.R;
import com.shilu.leapfrog.smsalert.components.ClickListener;
import com.shilu.leapfrog.smsalert.components.Constants;
import com.shilu.leapfrog.smsalert.components.MessagesDetails;
import com.shilu.leapfrog.smsalert.utils.Converter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/11/15
 */
public class MessageDetailRecyclerAdapter extends RecyclerView.Adapter<MessageDetailRecyclerAdapter.ViewHolder> {

    List<MessagesDetails> mList;
    ClickListener mListener;

    public MessageDetailRecyclerAdapter(List<MessagesDetails> list) {
        this.mList = list;
    }

    public void setClickListener(ClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_details_listview, parent, false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessagesDetails messagesDetails = mList.get(position);
        String mBody = messagesDetails.MESSAGE_BODY;
        String mType = messagesDetails.MESSAGE_TYPE;
        String mDate = messagesDetails.DATE_TIME;

        holder.txtBody.setText(mBody);
        holder.txtTime.setText(Converter.DateTimeConverter(Long.parseLong(mDate)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (mType.equals(Constants.SENT)) {
            params.setMargins(150, 20, 50, 20);
            params.gravity = Gravity.RIGHT;
            // Alignment of the textView set to Right
            holder.rl.setLayoutParams(params);
            holder.rl.setBackgroundResource(R.drawable.chat_bubble_right);
        } else {
            params.setMargins(50, 20, 150, 20);
            params.gravity = Gravity.LEFT;
            // Alignment of the textView set to Left
            holder.rl.setLayoutParams(params);
            holder.rl.setBackgroundResource(R.drawable.chat_bubble_left);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.listview_detail_body)
        TextView txtBody;
        @InjectView(R.id.listview_detail_time)
        TextView txtTime;
        @InjectView(R.id.rl_details)
        RelativeLayout rl;

        public ViewHolder(View itemView) {
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
