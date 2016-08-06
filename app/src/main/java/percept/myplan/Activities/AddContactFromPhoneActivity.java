package percept.myplan.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Contact;
import percept.myplan.R;
import percept.myplan.adapters.ContactFromPhoneAdapter;
import percept.myplan.fragments.fragmentContacts;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AddContactFromPhoneActivity extends AppCompatActivity implements
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {
    //    private RecyclerView LST_CONTACT;
    private List<Contact> LIST_CONTACTS;
    //    private ContactAdapter ADAPTER;
    private ContactFromPhoneAdapter ADAPTER;
    private String FROM_PAGE = "";
    private boolean SINGLE_CHECK = false;
    private StickyListHeadersListView LST_CONTACT;
    private EditText EDT_SEARCHTEXT;
    private int NO_COUNT = 0;
    private int SAVED_NO_COUNT = 0;
    private ProgressBar PB_SAVECONTACT;

    private String ADD_TO_HELP_LIST = "0";
    private Utils UTILS;
    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("FROM")) {
            FROM_PAGE = getIntent().getStringExtra("FROM");
        }

        if (FROM_PAGE.toLowerCase().trim().equals("emergency")) {
            SINGLE_CHECK = true;
        }
        if (getIntent().hasExtra("ADD_TO_HELP")) {
            ADD_TO_HELP_LIST = "1";
        }

        setContentView(R.layout.add_contact_from_phone);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(AddContactFromPhoneActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddContactFromPhoneActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(AddContactFromPhoneActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            readContacts();
        }

        UTILS = new Utils(AddContactFromPhoneActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_add_contact_from_phone));

        PB_SAVECONTACT = (ProgressBar) findViewById(R.id.pbSaveContact);
        PB_SAVECONTACT.setVisibility(View.GONE);
        EDT_SEARCHTEXT = (EditText) findViewById(R.id.edtSearchContact);
//        LST_CONTACT = (RecyclerView) findViewById(R.id.lstContact);
        LIST_CONTACTS = new ArrayList<>();

        LST_CONTACT = (StickyListHeadersListView) findViewById(R.id.list);
        LST_CONTACT.setOnStickyHeaderChangedListener(this);
        LST_CONTACT.setOnStickyHeaderOffsetChangedListener(this);
        LST_CONTACT.setDrawingListUnderStickyHeader(true);
        LST_CONTACT.setAreHeadersSticky(true);


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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    readContacts();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
            if (SINGLE_CHECK) {
                for (Contact _obj : LIST_CONTACTS) {
                    if (_obj.isSelected()) {
                        UTILS.setPreference("EMERGENCY_CONTACT_NAME", _obj.getContactName());
                        UTILS.setPreference("EMERGENCY_CONTACT_NO", _obj.getPhoneNo());
                    }
                }
                AddContactFromPhoneActivity.this.finish();
                return true;
            }
            Toast.makeText(AddContactFromPhoneActivity.this, "Add Contact Pressed", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < LIST_CONTACTS.size(); i++) {
                if (LIST_CONTACTS.get(i).isSelected() && !LIST_CONTACTS.get(i).isOriginalSelection()) {
                    PB_SAVECONTACT.setVisibility(View.VISIBLE);
                    NO_COUNT = NO_COUNT + 1;
                    getContactInfoFromID(LIST_CONTACTS.get(i).getContactID(), LIST_CONTACTS.get(i).getPhoneNo());

                } else if (!LIST_CONTACTS.get(i).isSelected() && LIST_CONTACTS.get(i).isOriginalSelection()) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sid", Constant.SID);
                    params.put("sname", Constant.SNAME);
                    params.put(Constant.ID, LIST_CONTACTS.get(i).getWEB_ID());
                    PB_SAVECONTACT.setVisibility(View.VISIBLE);
                    NO_COUNT = NO_COUNT + 1;
                    try {
                        new General().getJSONContentFromInternetService(AddContactFromPhoneActivity.this,
                                General.PHPServices.DELETE_CONTACT, params, false, false, true, new VolleyResponseListener() {
                                    @Override
                                    public void onError(VolleyError message) {

                                    }

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("::::::::::::: ", response.toString());
                                        SAVED_NO_COUNT = SAVED_NO_COUNT + 1;
                                        if (NO_COUNT == SAVED_NO_COUNT) {
                                            PB_SAVECONTACT.setVisibility(View.GONE);
                                            Toast.makeText(AddContactFromPhoneActivity.this,
                                                    getResources().getString(R.string.contactsaved), Toast.LENGTH_SHORT).show();
                                            AddContactFromPhoneActivity.this.finish();
                                            fragmentContacts.GET_CONTACTS = true;
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void getContactInfoFromID(String _contactID, String _phoneno) {
        String _fname = "", _lname = "", _email = "", _note = "";
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
            _email = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            String emailType = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

            System.out.println("Email " + _email + " Email Type : " + emailType);
        }
        emailCur.close();

        // Get note.......
        String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] noteWhereParams = new String[]{_contactID,
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
        Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
        if (noteCur.moveToFirst()) {
            _note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
            System.out.println("Note " + _note);
        }
        noteCur.close();

        // Get firstname and all names
        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, _contactID};
        Cursor nameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        while (nameCur.moveToNext()) {
            _fname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            _lname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
            String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
            if (_lname == null || _lname.equals("null")) {
                _lname = "";
            }
            System.out.println("Given " + _fname);
            System.out.println("family " + _lname);
            System.out.println("Display " + display);
        }
        nameCur.close();

//        try {
//            Cursor curPhoto = getContentResolver().query(
//                    ContactsContract.Data.CONTENT_URI,
//                    null,
//                    ContactsContract.Data.CONTACT_ID + "=" + _contactID + " AND "
//                            + ContactsContract.Data.MIMETYPE + "='"
//                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
//                    null);
//            if (curPhoto != null) {
//                if (!curPhoto.moveToFirst()) {
//                    //return null; // no photo
//                }
//            } else {
//                //return null; // error in cursor process
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            //return null;
//        }
//        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
//                .parseLong(_contactID));
//        Uri _imageURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//
//        File _file = new File(_imageURI.getPath());

        Bitmap photo = null;
        File _file = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(_contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                _file = new File(getCacheDir(), "_temp.png");
                _file.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap = photo;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(_file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }

            if (inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        new UploadFileToServer(_fname, _lname, _email, _note, _phoneno, ADD_TO_HELP_LIST, _file).execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        private String FNAME, LNAME, EMAIL, NOTE, PHONENO, HELPLIST;
        private File IMG_FILE;

        public UploadFileToServer(String fname, String lname, String email, String note, String phoneno, String helplist,
                                  File image) {
            this.FNAME = fname;
            this.LNAME = lname;
            this.EMAIL = email;
            this.NOTE = note;
            this.PHONENO = phoneno;
            this.HELPLIST = helplist;
            this.IMG_FILE = image;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".saveContact");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

//                if (!FILE_PATH.equals("")) {
//                    File sourceFile = new File(FILE_PATH);

                // Adding file data to http body
                if (IMG_FILE != null)
                    entity.addPart(Constant.CON_IMAGE, new FileBody(IMG_FILE));
//                }
                // Extra parameters if you want to pass to server
                try {

                    entity.addPart("sid", new StringBody(Constant.SID));
                    entity.addPart("sname", new StringBody(Constant.SNAME));
                    entity.addPart(Constant.ID, new StringBody(""));
                    entity.addPart(Constant.FIRST_NAME, new StringBody(this.FNAME));
                    entity.addPart(Constant.LAST_NAME, new StringBody(this.LNAME));
                    entity.addPart(Constant.PHONE, new StringBody(this.PHONENO));
                    entity.addPart(Constant.SKYPE, new StringBody(""));
                    entity.addPart(Constant.EMAIL, new StringBody(this.EMAIL));
                    entity.addPart(Constant.HELPLIST, new StringBody(this.HELPLIST));
                    entity.addPart(Constant.NOTE, new StringBody(this.NOTE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            try {
                Log.d(":::::: ", result);
                JSONObject _object = new JSONObject(result);
                JSONObject _ObjData = _object.getJSONObject(Constant.DATA);
                SAVED_NO_COUNT = SAVED_NO_COUNT + 1;
                if (NO_COUNT == SAVED_NO_COUNT) {
                    PB_SAVECONTACT.setVisibility(View.GONE);
                    Toast.makeText(AddContactFromPhoneActivity.this,
                            getResources().getString(R.string.contactsaved), Toast.LENGTH_SHORT).show();
                    AddContactFromPhoneActivity.this.finish();
                    fragmentContacts.GET_CONTACTS = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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

                        Log.d(":::::::::::: ", _contactID);
                        Boolean _hasContact = false;
                        String _WEB_ID = "";
                        // Get firstname and all names
                        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, _contactID};
                        Cursor nameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                        while (nameCur.moveToNext()) {
                            String _fname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));

                            if (!SINGLE_CHECK) {
                                if (ADD_TO_HELP_LIST.equals("1")) {
                                    ArrayList<String> lst = new ArrayList<String>(fragmentContacts.HELP_CONTACT_NAME.values());
                                    if (lst.contains(_fname)) {
                                        _hasContact = true;
                                        _WEB_ID = getKey(fragmentContacts.HELP_CONTACT_NAME, _fname);
                                    }
                                } else {
                                    ArrayList<String> lst = new ArrayList<String>(fragmentContacts.CONTACT_NAME.values());
                                    if (lst.contains(_fname)) {
                                        _hasContact = true;
                                        _WEB_ID = getKey(fragmentContacts.CONTACT_NAME, _fname);
                                    }
                                }
                            }
                        }
                        nameCur.close();

                        LIST_CONTACTS.add(new Contact(name, phoneNo, _contactID, _hasContact, _hasContact, _WEB_ID));
                    } while (cur.moveToNext());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ADAPTER = new ContactFromPhoneAdapter(AddContactFromPhoneActivity.this, LIST_CONTACTS, SINGLE_CHECK);
                LST_CONTACT.setAdapter(ADAPTER);
            }
        }.execute();


    }

    String getKey(HashMap<String, String> map, String value) {
        String key = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if ((value == null && entry.getValue() == null) || (value != null && value.equals(entry.getValue()))) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }


    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {

    }
}
