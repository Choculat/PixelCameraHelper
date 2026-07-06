package sh.margot.pixelcamerahelper;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** Receives the camera's REVIEW intent and forwards it to the user's chosen gallery app. */
public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "PCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        Log.i(TAG, "Received: action=" + in.getAction() + " data=" + in.getData()
                + " type=" + in.getType() + " extras=" + in.getExtras());

        String target = Prefs.targetPackage(this);
        if (target == null) {
            Toast.makeText(this, "Pick a gallery app in Pixel Camera Helper first", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        Intent out = new Intent(in);
        out.setComponent(null);
        out.setPackage(target);
        out.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_FORWARD_RESULT);

        try {
            startActivity(out);
        } catch (ActivityNotFoundException e) {
            // ponytail: target may not handle REVIEW — retry as plain VIEW
            out.setAction(Intent.ACTION_VIEW);
            try {
                startActivity(out);
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(this, "Selected app can't open this", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
}
