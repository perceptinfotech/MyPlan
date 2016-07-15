package percept.myplan.Activities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.Classes.Contact;
import percept.myplan.R;
import percept.myplan.adapters.ContactAdapter;

public class AddContactFromPhoneActivity extends AppCompatActivity {
    private RecyclerView LST_CONTACT;
    private List<Contact> LIST_CONTACTS;
    private ContactAdapter ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_from_phone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        ADAPTER = new ContactAdapter(LIST_CONTACTS);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        LST_CONTACT.setLayoutManager(mLayoutManager);
        LST_CONTACT.setItemAnimator(new DefaultItemAnimator());
        LST_CONTACT.setAdapter(ADAPTER);

        readContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
                Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
                if (cur.moveToFirst()) {
                    do {
                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNo = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        LIST_CONTACTS.add(new Contact(name, phoneNo, false));
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
