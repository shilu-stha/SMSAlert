package adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.shilu.leapfrog.smsalert.R;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/22/15
 */

public class MessagesListAdapter extends CursorAdapter {

    public MessagesListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.messages_listview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtBody = (TextView) view.findViewById(R.id.messages_listview_text);
        TextView txtNumber = (TextView) view.findViewById(R.id.messages_listview_txtnum);

        String mBody = cursor.getString(cursor.getColumnIndexOrThrow("message_body"));
        String mName = cursor.getString(cursor.getColumnIndexOrThrow("contacts_name"));

        if (mName.equals(null)||mName.equals("")) {
            mName = cursor.getString(cursor.getColumnIndexOrThrow("contacts_number"));              //if contact name not present show number
        }

        txtBody.setText(mBody);
        txtNumber.setText(mName);
    }
}
