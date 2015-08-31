package com.shilu.leapfrog.smsalert.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.shilu.leapfrog.smsalert.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.shilu.leapfrog.smsalert.data.Contract;

/**
 * Adapter to list the messages
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/22/15
 */

public class MessagesListAdapter extends CursorAdapter {

    @InjectView(R.id.listview_messages_text)
    TextView txtBody;
    @InjectView(R.id.listview_messages_num)
    TextView txtNumber;

    public MessagesListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.messages_listview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ButterKnife.inject(this,view);

        String mBody = cursor.getString(cursor.getColumnIndexOrThrow(Contract.MessageEntry.MESSAGE_BODY));
        String mName = cursor.getString(cursor.getColumnIndexOrThrow(Contract.MessageEntry.CONTACTS_NAME));

        if (TextUtils.isEmpty(mName)) {
            //if contact name not present show number
            mName = cursor.getString(cursor.getColumnIndexOrThrow(Contract.MessageEntry.CONTACTS_NUMBER));
        }

        txtBody.setText(mBody);
        txtNumber.setText(mName);
    }
}
