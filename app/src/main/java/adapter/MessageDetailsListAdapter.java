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

import common.Converter;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/4/15
 */

public class MessageDetailsListAdapter extends CursorAdapter {

    public MessageDetailsListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.messages_details_listview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = (TextView) view.findViewById(R.id.message_body);
        TextView tvDate = (TextView) view.findViewById(R.id.message_date);
        TextView tvtime = (TextView) view.findViewById(R.id.message_time);

        String body = cursor.getString(cursor.getColumnIndexOrThrow("message_body"));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("message_type"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date_time"));

        tvBody.setText(body);
        tvDate.setText(Converter.DateConverter(Long.parseLong(date)));
        tvtime.setText(Converter.TimeConverter(Long.parseLong(date)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);
        if(type.equals("SENT")){
            params.gravity = Gravity.RIGHT;
            tvBody.setLayoutParams(params);
            tvBody.setBackgroundResource(R.drawable.chat_bubble_right);
        }
        else{
            params.gravity = Gravity.LEFT;
            tvBody.setLayoutParams(params);
            tvBody.setBackgroundResource(R.drawable.chat_bubble_left);
        }
    }
}
