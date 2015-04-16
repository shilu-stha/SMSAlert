package common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.shilulf.smsalert.smsalert.MainActivity;
import com.shilulf.smsalert.smsalert.TextToSpeechService;

/**
 * Created by shilushrestha on 4/15/15.
 */
public class SMSReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage message[] = null;
            String smsFrom = null;
            String msgBody = null;
            if(bundle!=null){
                try{
                    Object[] pdusObject = (Object[]) bundle.get("pdus");
                    message = new SmsMessage[pdusObject.length];
                    for(int i=0; i<message.length; i++){
                        message[i] = SmsMessage.createFromPdu((byte[])pdusObject[i]);
                        smsFrom = message[i].getOriginatingAddress();
                        msgBody = message[i].getMessageBody();
                    }
//                    Intent intentService = new Intent(context, TextToSpeechService.class);
//                    intentService.putExtra("SMSAlert_Message",msgBody);
//                    context.startService(intentService);
                    Intent intentMain = new Intent(context, MainActivity.class);
                    intentMain.putExtra("SMSAlert_Message",msgBody);
                    context.startActivity(intentMain);
                    Log.d("RECEIVER","RECEIVED SMS FROM: "+smsFrom+" Message: "+msgBody);

                }catch(Exception e){
                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
