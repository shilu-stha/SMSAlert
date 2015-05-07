package adapter;

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

import common.Constants;
import common.Converter;
import data.Contract;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/4/15
 */

public class MessageDetailsListAdapter extends CursorAdapter {


    public MessageDetailsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.messages_details_listview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtBody = (TextView) view.findViewById(R.id.message_body);
        TextView txtDate = (TextView) view.findViewById(R.id.message_date);
        TextView txtTime = (TextView) view.findViewById(R.id.message_time);

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
