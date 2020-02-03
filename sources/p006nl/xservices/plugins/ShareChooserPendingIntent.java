package p006nl.xservices.plugins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* renamed from: nl.xservices.plugins.ShareChooserPendingIntent */
public class ShareChooserPendingIntent extends BroadcastReceiver {
    public static String chosenComponent = null;

    public void onReceive(Context context, Intent intent) {
        chosenComponent = intent.getExtras().get("android.intent.extra.CHOSEN_COMPONENT").toString();
    }
}
