package percept.myplan.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.crosswall.photo.pick.PickConfig;
import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.R;

import static percept.myplan.Activities.HopeDetailsActivity.GET_HOPE_DETAILS;


public class AddStrategyImageActivity extends AppCompatActivity {

    private TextView TV_CHOOSEEXISTING, TV_TAKENEW;
    private static Uri IMG_URI;
    private static final int REQ_TAKE_PICTURE = 33;
    private String FROM = "";
    private String HOPE_TITLE = "";
    private String HOPE_ID = "";
    private boolean FROM_EDIT = false;
    private ProgressBar PB;

    private final static int MY_PERMISSIONS_REQUEST = 22;
    private boolean HAS_PERMISSION = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy_image);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addimage));

        if (getIntent().hasExtra("FROM_HOPE")) {
            FROM = getIntent().getExtras().getString("FROM_HOPE");
            HOPE_TITLE = getIntent().getExtras().getString("HOPE_TITLE");
            HOPE_ID = getIntent().getExtras().getString("HOPE_ID");
        }

        if (getIntent().hasExtra("FROM_EDIT")) {
            FROM_EDIT = true;
        }

        TV_CHOOSEEXISTING = (TextView) findViewById(R.id.tvChooseExisting);
        TV_TAKENEW = (TextView) findViewById(R.id.tvTakeNew);
        PB = (ProgressBar) findViewById(R.id.pbAddImage);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            insertDummyPermissionWrapper();
        }


        TV_CHOOSEEXISTING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HAS_PERMISSION) {
                    Toast.makeText(AddStrategyImageActivity.this, R.string.requiredpermissionn, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (FROM.equals("") || FROM_EDIT) {
                    new PickConfig.Builder(AddStrategyImageActivity.this)
                            .pickMode(PickConfig.MODE_MULTIP_PICK)
                            .maxPickSize(10)
                            .spanCount(3)
                            //.showGif(true)
                            .checkImage(false) //default false
                            .useCursorLoader(false) //default true
                            .toolbarColor(R.color.colorPrimary)
                            .build();
                } else {
                    new PickConfig.Builder(AddStrategyImageActivity.this)
                            .pickMode(PickConfig.MODE_SINGLE_PICK)
                            .spanCount(3)
                            //.showGif(true)
                            .checkImage(false) //default false
                            .useCursorLoader(false) //default true
                            .toolbarColor(R.color.colorPrimary)
                            .build();
                }
            }
        });

        TV_TAKENEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HAS_PERMISSION)
                    return;

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                IMG_URI = Uri.fromFile(Constant.getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, IMG_URI);
                // start the image capture Intent
                startActivityForResult(intent, REQ_TAKE_PICTURE);


            }
        });
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void insertDummyPermissionWrapper() {
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
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddStrategyImageActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddStrategyImageActivity.this.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    HAS_PERMISSION = true;
                } else {
                    HAS_PERMISSION = false;
                    // Permission Denied
                    Toast.makeText(AddStrategyImageActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == PickConfig.PICK_REQUEST_CODE) {
            if (FROM.equals("") || FROM_EDIT) {
                if (FROM_EDIT) {
                    StrategyEditActivity.LIST_IMG = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
                    AddStrategyImageActivity.this.finish();
                } else {
                    AddStrategyActivity.LIST_IMG = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
                    AddStrategyImageActivity.this.finish();
                }

            } else {
                List<String> _LIST_IMG = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
                if (_LIST_IMG.size() > 0) {
                    PB.setVisibility(View.VISIBLE);
                    new AddHopeBoxImageElement(HOPE_TITLE, HOPE_ID, _LIST_IMG.get(0), "image").execute();
                }
            }
        }
        if (requestCode == REQ_TAKE_PICTURE) {

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
                    file.delete();
                }
                String _Path = Constant.APP_MEDIA_PATH + File.separator + "IMAGES" + File.separator + name;

                Log.d("::::::: ", _Path);
                if (FROM.equals("") || FROM_EDIT) {
                    if (FROM_EDIT) {
                        StrategyEditActivity.LIST_IMG.add(_Path);
                        AddStrategyImageActivity.this.finish();
                    } else {
                        AddStrategyActivity.LIST_IMG.add(_Path);
                        AddStrategyImageActivity.this.finish();
                    }

                } else {
                    new AddHopeBoxImageElement(HOPE_TITLE, HOPE_ID, _Path, "image").execute();
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddHopeBoxImageElement extends AsyncTask<Void, Integer, String> {

        private String HOPE_TITLE, IMG_PATH, HOPE_ID, TYPE;

        public AddHopeBoxImageElement(String title, String hopeId, String imgpath, String type) {
            this.HOPE_TITLE = title;
            this.IMG_PATH = imgpath;
            this.HOPE_ID = hopeId;
            this.TYPE = type;

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
            HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".saveHopemedia");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (!IMG_PATH.equals("")) {
                    File sourceFile = new File(IMG_PATH);
                    entity.addPart("media", new FileBody(sourceFile));
                }
                try {

                    entity.addPart("sid", new StringBody(Constant.SID));
                    entity.addPart("sname", new StringBody(Constant.SNAME));
                    entity.addPart(Constant.ID, new StringBody(""));
                    entity.addPart(Constant.HOPE_ID, new StringBody(HOPE_ID));
                    entity.addPart(Constant.HOPE_TITLE, new StringBody(HOPE_TITLE));
                    entity.addPart(Constant.HOPE_TYPE, new StringBody(TYPE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                long totalLength = entity.getContentLength();
                System.out.println("TotalLength : " + totalLength);

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
            PB.setVisibility(View.GONE);
            super.onPostExecute(result);
            Log.d(":::::: ", result);
            if (getIntent().hasExtra("FROM_HOPE")) {
                GET_HOPE_DETAILS = true;
            }
            AddStrategyImageActivity.this.finish();
        }

    }


}
