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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import percept.myplan.POJO.Contact;
import percept.myplan.R;
import percept.myplan.adapters.ContactAdapter;
import percept.myplan.adapters.TestBaseAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AddContactFromPhoneActivity extends AppCompatActivity implements
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {
    //    private RecyclerView LST_CONTACT;
    private List<Contact> LIST_CONTACTS;
    //    private ContactAdapter ADAPTER;
    private TestBaseAdapter ADAPTER;
    private String FROM_PAGE = "";
    private boolean SINGLE_CHECK = false;
    private StickyListHeadersListView stickyList;
    private EditText EDT_SEARCHTEXT;

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

        EDT_SEARCHTEXT = (EditText) findViewById(R.id.edtSearchContact);
//        LST_CONTACT = (RecyclerView) findViewById(R.id.lstContact);
        LIST_CONTACTS = new ArrayList<>();
//        LIST_CONTACTS.add(new Contact("dadsa", false));
//        LIST_CONTACTS.add(new Contact("dsaz", false));
//        LIST_CONTACTS.add(new Contact("eq", true));
//        LIST_CONTACTS.add(new Contact("cxz", false));
//        LIST_CONTACTS.add(new Contact("jhgkj", false));
//        LIST_CONTACTS.add(new Contact("m,nm", true));
//        LIST_CONTACTS.add(new Contact("eqw", false));
//        LIST_CONTACTS.add(new Contact("op", true));


        stickyList = (StickyListHeadersListView) findViewById(R.id.list);
        stickyList.setOnStickyHeaderChangedListener(this);
        stickyList.setOnStickyHeaderOffsetChangedListener(this);
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        LST_CONTACT.setLayoutManager(mLayoutManager);
//        LST_CONTACT.setItemAnimator(new DefaultItemAnimator());
//        LST_CONTACT.setAdapter(ADAPTER);

        readContacts();
        EDT_SEARCHTEXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                ADAPTER.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
//        getContactIDFromNumber(LIST_CONTACTS.get(2).getContactID());
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
            for (int i = 0; i < LIST_CONTACTS.size(); i++) {
                if (LIST_CONTACTS.get(i).isSelected()) {
                    getContactInfoFromID(LIST_CONTACTS.get(i).getContactID());
                }
            }
            return true;
        }
        return false;
    }

    private void getContactInfoFromID(String _contactID) {
        ContentResolver cr = getContentResolver();

        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,};
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        //Get Email...
        Cursor emailCur = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{_contactID}, null);
        while (emailCur.moveToNext()) {
            // This would allow you get several email addresses
            // if the email addresses were stored in an array
            String email = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            String emailType = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

            System.out.println("Email " + email + " Email Type : " + emailType);
        }
        emailCur.close();

        // Get note.......
        String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] noteWhereParams = new String[]{_contactID,
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
        Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
        if (noteCur.moveToFirst()) {
            String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
            System.out.println("Note " + note);
        }
        noteCur.close();

        // Get firstname and all names
        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, _contactID};
        Cursor nameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        while (nameCur.moveToNext()) {
            String given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            String family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
            String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
            System.out.println("Given " + given);
            System.out.println("family " + family);
            System.out.println("Display " + display);
        }
        nameCur.close();

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

                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,};
                Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
                if (cur.moveToFirst()) {
                    do {
                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNo = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String _contactID = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String _EMAIL = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.DATA));
                        String _photouri = cur.getString(cur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));


                        Log.d(":::::::::::: ", _contactID);
                        LIST_CONTACTS.add(new Contact(name, phoneNo, _contactID, false));
                    } while (cur.moveToNext());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ADAPTER = new TestBaseAdapter(AddContactFromPhoneActivity.this, LIST_CONTACTS, SINGLE_CHECK);
                stickyList.setAdapter(ADAPTER);
            }
        }.execute();


    }


    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {

    }
}
