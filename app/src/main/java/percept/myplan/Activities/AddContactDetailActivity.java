package percept.myplan.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import org.apache.http.util.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import percept.myplan.AppController;
import percept.myplan.Dialogs.dialogSelectPic;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.MultiPartParsing;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.AsyncTaskCompletedListener;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.customviews.RoundedImageView;
import percept.myplan.fragments.fragmentContacts;

/**
 * Created by percept on 22/8/16.
 */

public class AddContactDetailActivity extends AppCompatActivity {

    private final int REQUEST_CODE_RINGTONE = 12;
    private final int REQUEST_CODE_PRIORITY = 13;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static final int REQ_TAKE_PICTURE = 33;
    private static final int TAKE_PICTURE_GALLERY = 34;

    private TextView tvAssignPriority, tvContactChar, tvAddRingTone;
    private EditText edtFirstName, edtLastName, edtCompany, edtAddAddress, edtAddUrl, edtAddEmail, edtAddPhoneNo;
    RoundedImageView imgContact;
    private ContactDisplay _contactDisplay;
    private static Uri IMG_URI;
    private String FILE_PATH = "";
    private CoordinatorLayout REL_COORDINATE;
    private String ADD_TO_HELP_LIST = "0";
    private int contact_priority = 0;
    private Utils UTILS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        if (getIntent().getBooleanExtra("IS_FROM_STRATEGY", false)) {
            _contactDisplay = (ContactDisplay) getIntent().getSerializableExtra("DATA");
            ADD_TO_HELP_LIST = _contactDisplay.getHelplist();
        } else if (getIntent().hasExtra("ADD_TO_HELP")) {
            ADD_TO_HELP_LIST = "1";
        }
        initializeComponent();

    }

    private void initializeComponent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.addcontact));
        UTILS = new Utils(AddContactDetailActivity.this);
        imgContact = (RoundedImageView) findViewById(R.id.imgContact);

        tvAssignPriority = (TextView) findViewById(R.id.tvAssignPriority);
        tvContactChar = (TextView) findViewById(R.id.tvContactChar);
        tvAddRingTone = (TextView) findViewById(R.id.tvAddRingTone);

        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtCompany = (EditText) findViewById(R.id.edtCompany);
        edtAddPhoneNo = (EditText) findViewById(R.id.edtAddPhoneNo);
        edtAddEmail = (EditText) findViewById(R.id.edtAddEmail);
        edtAddUrl = (EditText) findViewById(R.id.edtAddUrl);
        edtAddAddress = (EditText) findViewById(R.id.edtAddAddress);
        imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermission();
            }
        });

        tvAssignPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContactDetailActivity.this, AssignPriorityActivity.class);
                intent.putExtra("ADD_TO_HELP", ADD_TO_HELP_LIST);
                startActivityForResult(intent, REQUEST_CODE_PRIORITY);
            }
        });

        tvAddRingTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                startActivityForResult(ringtone, REQUEST_CODE_RINGTONE);
            }
        });

        if (_contactDisplay != null) {
            edtFirstName.setText(_contactDisplay.getFirst_name());
            edtLastName.setText(_contactDisplay.getLast_name());
            edtAddPhoneNo.setText(_contactDisplay.getPhone());
            edtAddEmail.setText(_contactDisplay.getEmail());
            if (_contactDisplay.getCon_image().equals("")) {
                imgContact.setImageResource(R.drawable.contact_noimage);
                tvContactChar.setTextColor(getResources().getColor(android.R.color.white));
                if (!TextUtils.isEmpty(_contactDisplay.getFirst_name())) {
                    tvContactChar.setText(_contactDisplay.getFirst_name().substring(0, 2));
                } else if (!TextUtils.isEmpty(_contactDisplay.getLast_name())) {
                    tvContactChar.setText(_contactDisplay.getLast_name().substring(0, 2));
                }


//            holder.IMG_CONTACT.setBackgroundColor(Color.rgb(169, 169, 169));
            } else {
                tvContactChar.setVisibility(View.GONE);
                AppController.getInstance().getImageLoader().get(_contactDisplay.getCon_image(),
                        new ImageLoader.ImageListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }

                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                if (response.getBitmap() != null) {
                                    // load image into imageview
                                    imgContact.setImageBitmap(response.getBitmap());
                                }
                            }
                        });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_saveNote) {
            saveContact();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RINGTONE:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    final Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                    // Get your title here `ringtone.getTitle(this)
                    String Str = ringtone.getTitle(this);

                    tvAddRingTone.setText(Str);
                    Log.d("::::: ", Str);
                }
                break;
            case TAKE_PICTURE_GALLERY:
                if (data == null)
                    return;
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                FILE_PATH = c.getString(columnIndex);
                c.close();
//                IMG_USER.setImageURI(selectedImage);
                Picasso.with(AddContactDetailActivity.this).load(selectedImage).into(imgContact);
                tvContactChar.setVisibility(View.INVISIBLE);
                break;
            case REQ_TAKE_PICTURE:
                try {

                    Calendar cal = Calendar.getInstance();
                    int seconds = cal.get(Calendar.SECOND);
                    int hour = cal.get(Calendar.HOUR);
                    int min = cal.get(Calendar.MINUTE);
                    String currentDateTimeString = new SimpleDateFormat("ddMMMyyyy").format(new Date());

                    String name = "IMG_" + currentDateTimeString + seconds + hour + min + ".jpeg";

                    String _imgPath = IMG_URI.getPath();

                    File mediaStorageDir = new File(Constant.APP_MEDIA_PATH + File.separator + "IMAGES");

                    // Create the storage directory if it does not exist
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {

                        }
                    }

                    Constant.copyFile(_imgPath, Constant.APP_MEDIA_PATH + File.separator + "IMAGES", name);

                    File file = new File(_imgPath);
                    if (file.exists()) {
//                        file.delete();
                    }
                    FILE_PATH = Constant.APP_MEDIA_PATH + File.separator + "IMAGES" + File.separator + name;

                    Picasso.with(AddContactDetailActivity.this).load(IMG_URI).into(imgContact);
                    tvContactChar.setVisibility(View.INVISIBLE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_PRIORITY:
                // 0: Default 1:Help  2:Emergency
                if (resultCode == Activity.RESULT_OK) {
                    if (data.hasExtra("FROM_PRIORITY")) {
                        contact_priority = data.getIntExtra("FROM_PRIORITY", 0);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void saveContact() {
        if (!UTILS.isNetConnected()) {
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveContact();
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }
        switch (contact_priority) {
            case 0:
            case 1:
                ADD_TO_HELP_LIST = String.valueOf(contact_priority);
                HashMap<String, String> params = new HashMap<>();
                // Adding file data to http body
                if (FILE_PATH != null)
                    params.put(Constant.CON_IMAGE, FILE_PATH);
                params.put("sid", Constant.SID);
                params.put("sname", Constant.SNAME);
                if (_contactDisplay != null)
                    params.put(Constant.ID, _contactDisplay.getId());
                else
                    params.put(Constant.ID, "");
                params.put(Constant.FIRST_NAME, edtFirstName.getText().toString().trim());
                params.put(Constant.LAST_NAME, edtLastName.getText().toString().trim());
                params.put(Constant.PHONE, edtAddPhoneNo.getText().toString().trim());
                params.put(Constant.SKYPE, "");
                params.put(Constant.EMAIL, edtAddEmail.getText().toString().trim());
                params.put(Constant.HELPLIST, ADD_TO_HELP_LIST);
                params.put(Constant.WEB_ADDRESS, edtAddUrl.getText().toString().trim());
                params.put(Constant.COMPANY_NAME, edtCompany.getText().toString().trim());
                params.put(Constant.ADDRESS, edtAddAddress.getText().toString().trim());
                params.put(Constant.RINGTONE, tvAddRingTone.getText().toString());
                new MultiPartParsing(AddContactDetailActivity.this, params, General.PHPServices.SAVE_CONTACT, new AsyncTaskCompletedListener() {
                    @Override
                    public void onTaskCompleted(String response) {
                        fragmentContacts.GET_CONTACTS = true;
                        AddContactDetailActivity.this.finish();
                    }
                });
                break;
            case 2:
                UTILS.setPreference("EMERGENCY_CONTACT_NAME", edtFirstName.getText().toString().trim() + " " + edtLastName.getText().toString().trim());
                UTILS.setPreference("EMERGENCY_CONTACT_NO", edtAddPhoneNo.getText().toString().trim());
                AddContactDetailActivity.this.finish();
                break;

        }


    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<String>();

            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write Storage");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Read Storage");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                return;
            }
        } else {
            OpenDialog();
        }
    }

    public void OpenDialog() {
        dialogSelectPic _dialogDate = new dialogSelectPic(AddContactDetailActivity.this) {
            @Override
            public void fromGallery() {
                dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, TAKE_PICTURE_GALLERY);
            }

            @Override
            public void fromCamera() {
                dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                IMG_URI = Uri.fromFile(Constant.getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, IMG_URI);
                // start the image capture Intent
                startActivityForResult(intent, REQ_TAKE_PICTURE);
            }
        };
        _dialogDate.setCanceledOnTouchOutside(false);
        _dialogDate.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddContactDetailActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }

}
