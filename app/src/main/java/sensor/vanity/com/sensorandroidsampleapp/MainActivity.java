package sensor.vanity.com.sensorandroidsampleapp;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    final int SMS_PERMISSION_CODE = 123;
    ToggleButton toggleRecordingButton;

    private ChargeBroadcastReceiver chargeBroadcastReceiver;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chargeBroadcastReceiver = new ChargeBroadcastReceiver();
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        toggleRecordingButton = (ToggleButton) findViewById(R.id.toggleRecordingButton);

        // Request permission to receive SMS
        if (!isSmsPermissionGranted()) {
            requestReadSmsPermission();
        }

        toggleRecordingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    startBackgroundServices();
                }
                else {
                    cancelBackgroundServices();
                }
            }
        });
    }

    public void startBackgroundServices() {
        Context context = this.getApplicationContext();
        if(chargeBroadcastReceiver != null){
            chargeBroadcastReceiver.setAlarm(context);
            //Register SMS receiver
            context.registerReceiver(smsBroadcastReceiver, mIntentFilter);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelBackgroundServices(){
        Context context = this.getApplicationContext();
        if(chargeBroadcastReceiver != null){
            chargeBroadcastReceiver.cancelAlarm(context);
            // Unregister the SMS receiver
            context.unregisterReceiver(smsBroadcastReceiver);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}