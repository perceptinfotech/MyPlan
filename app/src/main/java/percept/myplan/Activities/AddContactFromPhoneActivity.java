package percept.myplan.Activities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import percept.myplan.POJO.Contact;
import percept.myplan.R;
import percept.myplan.adapters.ContactAdapter;

public class AddContactFromPhoneActivity extends AppCompatActivity {
    private RecyclerView LST_CONTACT;
    private List<Contact> LIST_CONTACTS;
    private ContactAdapter ADAPTER;
    private String FROM_PAGE = "";
    private boolean SINGLE_CHECK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("FROM")) {
            FROM_PAGE = getIntent().getStringExtra("FROM");
        }

        if (FROM_PAGE.toLowerCase().trim().equals("emergency")) {
            SINGLE_CHECK = true;
        }

        setContentView(R.layout.add_contact_from_phone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_add_contact_from_phone));

        LST_CONTACT = (RecyclerView) findViewById(R.id.lstContact);
        LIST_CONTACTS = new ArrayList<>();
//        LIST_CONTACTS.add(new Contact("dadsa", false));
//        LIST_CONTACTS.add(new Contact("dsaz", false));
//        LIST_CONTACTS.add(new Contact("eq", true));
//        LIST_CONTACTS.add(new Contact("cxz", false));
//        LIST_CONTACTS.add(new Contact("jhgkj", false));
//        LIST_CONTACTS.add(new Contact("m,nm", true));
//        LIST_CONTACTS.add(new Contact("eqw", false));
//        LIST_CONTACTS.add(new Contact("op", true));
        ADAPTER = new ContactAdapter(LIST_CONTACTS, SINGLE_CHECK);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        LST_CONTACT.setLayoutManager(mLayoutManager);
        LST_CONTACT.setItemAnimator(new DefaultItemAnimator());
        LST_CONTACT.setAdapter(ADAPTER);

        readContacts();

        getContactIDFromNumber(LIST_CONTACTS.get(2).getContactID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddContactFromPhoneActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_InsertContact) {
            Toast.makeText(AddContactFromPhoneActivity.this, "Add Contact Pressed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public ArrayList<String> getContactIDFromNumber(String _id) {
        ContentResolver cr = getContentResolver();
//        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
//                ContactsContract.CommonDataKinds.Phone._ID};
//        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);

        ArrayList<String> phones = new ArrayList<String>();

        Cursor cursor = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{_id}, null);

        while (cursor.moveToNext()) {
            phones.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
        }

        cursor.close();
        return (phones);
    }

    private void readContacts() {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ContentResolver cr = getContentResolver();

                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
                Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
                if (cur.moveToFirst()) {
                    do {
                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNo = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String _contactID = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                        Log.d(":::::::::::: ", _contactID);
                        LIST_CONTACTS.add(new Contact(name, phoneNo, _contactID, false));
                    } while (cur.moveToNext());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ADAPTER.notifyDataSetChanged();
            }
        }.execute();


    }
}
