package net.mms_projects.copy_it.android.tasks;

import java.util.Date;

import net.mms_projects.copy_it.AndroidClipboardUtils;
import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.api.CopyItProvider;
import net.mms_projects.copy_it.api.ServerApi;
import net.mms_projects.copy_it.api.endpoints.ClipboardContentEndpoint;
import net.mms_projects.copy_it.clipboard_services.ClipboardServiceInterface;
import net.mms_projects.copy_it.databases.HistoryItemsDbHelper;
import net.mms_projects.copy_it.models.HistoryContract;
import net.mms_projects.copy_it.models.HistoryItem.Change;
import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.v1.Clipboard;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class PasteItTask extends ServerApiUiTask<Void, Void, String> {
	private SQLiteDatabase database;

	public PasteItTask(Context context, ServerApi api) {
		super(context, api);

		this.setProgressDialigMessage(context.getResources().getString(
				R.string.text_content_pulling));
	}

	@Override
	protected String doInBackgroundWithException(Void... params)
			throws Exception {
		HistoryItemsDbHelper dbHelper = new HistoryItemsDbHelper(context);
		this.database = dbHelper.getWritableDatabase();

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.context);

        if (!preferences.getBoolean("api.use_dev_server", false)) {
            return new ClipboardContentEndpoint(api).get();
        } else {
            OAuthService service = new ServiceBuilder()
                    .provider(CopyItProvider.class)
                    .apiKey(this.context.getString(R.string.copyit_oauth_key))
                    .apiSecret(this.context.getString(R.string.copyit_oauth_secret))
                    .callback("http://example.com/")
                    .debug()
                    .build();

            String token = preferences.getString("oauth_public_key", null);
            String secret = preferences.getString("oauth_secret_key", null);

            if ((token == null) || (secret == null)) {
                return null;
            }

            Token accessToken = new Token(token, secret);

            Clipboard clipboard = new Clipboard(accessToken, service, "http://api.copyit.mmsdev.org/1/clipboard/");

            Clipboard.Responses.Get responseGet = clipboard.get();

            return responseGet.getContent();
        }
	}

	@Override
	protected void onPostExecute(String content) {
		try {
			this.doExceptionCheck();

			ClipboardServiceInterface clipboard = new AndroidClipboardUtils(this.context);

			Toast.makeText(
					this.context,
					this.context.getResources().getString(
							R.string.text_content_pulled, content),
					Toast.LENGTH_LONG).show();
			clipboard.setContent(content);

			ContentValues values = new ContentValues();
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_CONTENT, content);
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_DATE,
					new Date().getTime());
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_CHANGE,
					Change.PULLED.toString());

			SharedPreferences prefences = PreferenceManager
					.getDefaultSharedPreferences(this.context);
			if (prefences.getBoolean("history.tracking_state", true)) {
				this.database.insert(HistoryContract.ItemEntry.TABLE_NAME,
						null, values);
			}
			this.database.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Toast.makeText(
					this.context,
					this.context.getResources().getString(
							R.string.error_general, e.getLocalizedMessage()),
					Toast.LENGTH_LONG).show();
		}

		super.onPostExecute(content);
	}
}