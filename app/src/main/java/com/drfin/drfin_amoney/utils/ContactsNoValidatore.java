package com.drfin.drfin_amoney.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.models.Contacts;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Pulkit Kumar
 */
public class ContactsNoValidatore {

    private final ArrayList<Contacts> selectUsers = new ArrayList<>();
    private Cursor phones;
    private PrefrenceHandler pref;
    private EditText mobileEt;

    public void checkExistanceofNumber(Context context, String mobileNumber, EditText mobileEt) {
        this.mobileEt=mobileEt;
        pref = new PrefrenceHandler(context);
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(mobileNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        phones = Objects.requireNonNull(context).getContentResolver().query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        LoadContact loadContact = new LoadContact();
        loadContact.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("count", "" + phones.getCount());
            pref.setRelativesName("");
            if (phones.getCount() != 0) {
                while (phones.moveToNext()) {
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Contacts selectUser = new Contacts();
                    selectUser.setName(name);
                    selectUsers.add(selectUser);
                }
                if (selectUsers.size() <= 0) {
                    Log.d("TAG", "No contact details access");
                    pref.setRelativesName("");
                } else {
                    Log.d("TAG", "contact data new: " + selectUsers.get(0).getName());
                    pref.setRelativesName(selectUsers.get(0).getName());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
