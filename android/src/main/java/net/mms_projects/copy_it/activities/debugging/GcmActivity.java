package net.mms_projects.copy_it.activities.debugging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.android.utilities.AppUtility;
import net.mms_projects.copy_it.android.utilities.GcmUtility;
import net.mms_projects.copy_it.ui.android.MainActivity;

public class GcmActivity extends Activity {

    TextView registrationIdText;
    Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_debug_gcm);

        this.registrationIdText = (TextView) this.findViewById(R.id.registration_id_text);
        this.registrationIdText.setText(GcmUtility.getRegistrationId(this));

        this.registerButton = (Button) this.findViewById(R.id.register_button);

        this.reloadData();
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();

        this.reloadData();
    }


    public void registerWithGcm(View view) {
        GcmRegistrationTask task = new GcmRegistrationTask();
        task.execute();
    }

    public void resetGcm(View view) {
        GcmUtility.clear(this);

        this.reloadData();
    }

    public void copyDebugInformation(View view) {
        String content = null;

        switch (view.getId()) {
            case R.id.registration_id_copy:
                content = this.getTextViewText(R.id.registration_id_text);

                break;
            case R.id.sender_id_copy:
                content = this.getTextViewText(R.id.sender_id_text);

                break;
        }
        if (content != null) {
            Toast toast = Toast.makeText(this, content, Toast.LENGTH_LONG);
            toast.show();

            System.out.println("Content: " + content);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setType("text/plain");
            startActivity(intent);
        }
    }

    private String getTextViewText(int id) {
        return ((TextView) this.findViewById(id)).getText().toString();
    }

    private void reloadData() {
        TextView playServicesAvailableText = (TextView) this.findViewById(R.id.play_service_available);
        playServicesAvailableText.setText(
                AppUtility.checkPlayServices(this)
                        ? this.getResources().getText(R.string.debug_available)
                        : this.getResources().getText(R.string.debug_not_available)
        );

        String registrationId = GcmUtility.getRegistrationId(this);
        if (registrationId != null) {
            this.registrationIdText.setText(GcmUtility.getRegistrationId(this));
        } else {
            this.registrationIdText.setText(this.getResources().getText(R.string.debug_no_value));
        }
    }

    private class GcmRegistrationTask extends AsyncTask<Void, Void, String> {

        private final Context context = GcmActivity.this;

        @Override
        protected void onPreExecute() {
            registerButton.setEnabled(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return GcmUtility.register(this.context);
            } catch (GcmUtility.GmcRegistrationException e) {
                e.printStackTrace();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String registrationId) {
            super.onPostExecute(registrationId);

            registerButton.setEnabled(true);

            Toast toast;
            if (registrationId != null) {
                GcmUtility.storeRegistrationId(this.context, registrationId);

                toast = Toast.makeText(
                        this.context,
                        context.getResources().getText(R.string.gcm_debug_toast_registered),
                        Toast.LENGTH_LONG
                );
            } else {
                toast = Toast.makeText(
                        this.context,
                        context.getResources().getText(R.string.gcm_debug_toast_register_errored),
                        Toast.LENGTH_LONG
                );
            }
            toast.show();

            reloadData();
        }
    }
}
