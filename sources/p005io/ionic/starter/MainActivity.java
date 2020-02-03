package p005io.ionic.starter;

import android.os.Bundle;
import org.apache.cordova.CordovaActivity;

/* renamed from: io.ionic.starter.MainActivity */
public class MainActivity extends CordovaActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
        loadUrl(this.launchUrl);
    }
}
