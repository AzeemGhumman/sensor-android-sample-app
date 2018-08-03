package sensor.vanity.com.sensorandroidsampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;

import java.util.Objects;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String contents = "{\"timestamp\" : " + GeneralUtilities.getCurrentTimestamp() + ", \"events\" : [\"sms_received\"]}";
            GeneralUtilities.reportToServer(context, contents);
        }
    }
}
