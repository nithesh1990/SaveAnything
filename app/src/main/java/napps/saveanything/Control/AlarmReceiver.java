package napps.saveanything.Control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import napps.saveanything.Utilities.Utils;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        Utils.checkAndStartService(context, SaveClipService.class);
    }
}
