package com.shilu.leapfrog.smsalert.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shilu.leapfrog.smsalert.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.shilu.leapfrog.smsalert.components.Constants;
import com.shilu.leapfrog.smsalert.utils.Converter;
import com.shilu.leapfrog.smsalert.data.Contract;

/**
 * Adapter class to list details of the messages
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/4/15
 */

public class MessageDetailsListAdapter extends CursorAdapter {

    @InjectView(R.id.listview_detail_body)
    TextView txtBody;
    @InjectView(R.id.listview_detail_date)
    TextView txtDate;
    @InjectView(R.id.listview_detail_time)
    TextView txtTime;

    public MessageDetailsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.messages_details_listview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ButterKnife.inject(this,view);

        String mBody = cursor.getString(cursor.getColumnIndexOrThrow(Contract.MessageDetailEntry.MESSAGE_BODY));
        String mType = cursor.getString(cursor.getColumnIndexOrThrow(Contract.MessageDetailEntry.MESSAGE_TYPE));
        String mDate = cursor.getString(cursor.getColumnIndexOrThrow(Contract.MessageDetailEntry.DATE_TIME));

        txtBody.setText(mBody);
        txtDate.setText(Converter.DateConverter(Long.parseLong(mDate)));
        txtTime.setText(Converter.TimeConverter(Long.parseLong(mDate)));
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);

        if(mType.equals(Constants.SENT)){
            params.gravity = Gravity.RIGHT;
            // Alignment of the textView set to Right
            txtBody.setLayoutParams(params);
            txtBody.setBackgroundResource(R.drawable.chat_bubble_right);
        } else{
            params.gravity = Gravity.LEFT;
            // Alignment of the textView set to Left
            txtBody.setLayoutParams(params);
            txtBody.setBackgroundResource(R.drawable.chat_bubble_left);
        }
    }
}
