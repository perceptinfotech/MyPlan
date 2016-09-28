package percept.myplan.Activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import percept.myplan.Dialogs.dialogYesNoOption;
import percept.myplan.Global.Constant;
import percept.myplan.POJO.NearestEmergencyRoom;
import percept.myplan.R;

public class EmergencyRoomDetailActivity extends AppCompatActivity {

    private static final int REQ_CODE_EDIT_ROOM = 410;
    final private int REQUEST_CODE_CALL_PERMISSIONS = 123;
    private NearestEmergencyRoom emergencyRoom;
    private TextView tvPhoneNo;
    private TextView tvDistance;
    private TextView tvAddress;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_room_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);


        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        if (getIntent().hasExtra("EMERGENCY_ROOM_DETAIL")) {

            emergencyRoom = (NearestEmergencyRoom) getIntent().getSerializableExtra("EMERGENCY_ROOM_DETAIL");
            mTitle.setText(emergencyRoom.getRoomName());
            tvDistance.setText(new DecimalFormat("0.00").format(Double.parseDouble(
                    emergencyRoom.getDistance())) + getString(R.string.km));
            tvAddress.setText(emergencyRoom.getAddress());
            tvPhoneNo.setText(emergencyRoom.getPhone());
        }

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new dialogYesNoOption(EmergencyRoomDetailActivity.this, getString(R.string.open_google_maps)) {

                    @Override
                    public void onClickYes() {
                        dismiss();
                        String url = "http://maps.google.com/maps?f=d&daddr=" + emergencyRoom.getAddress() + "&dirflg=d&layer=t";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
//                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    }

                    @Override
                    public void onClickNo() {
                        dismiss();
                    }
                }.show();
            }
        });
        tvDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new dialogYesNoOption(EmergencyRoomDetailActivity.this, getString(R.string.open_google_maps)) {

                    @Override
                    public void onClickYes() {
                        dismiss();
                        String url = "http://maps.google.com/maps?f=d&daddr=" + emergencyRoom.getAddress() + "&dirflg=d&layer=t";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
//                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    }

                    @Override
                    public void onClickNo() {
                        dismiss();
                    }
                }.show();
            }
        });
        tvPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emergencyRoom != null && !TextUtils.isEmpty(emergencyRoom.getPhone()))
                    onCall(emergencyRoom.getPhone());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_strategy, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EmergencyRoomDetailActivity.this.finish();
                return true;
            case R.id.action_editStrategy:
                Intent intent = new Intent(EmergencyRoomDetailActivity.this, AddEmergencyRoomActivity.class);
                intent.putExtra("FROM_EDIT", "true");
                intent.putExtra(Constant.DATA, emergencyRoom);
                startActivityForResult(intent, REQ_CODE_EDIT_ROOM);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_EDIT_ROOM:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        emergencyRoom = (NearestEmergencyRoom) data.getSerializableExtra("EMERGENCY_ROOM_DETAIL");
                        mTitle.setText(emergencyRoom.getRoomName());
                        tvDistance.setText(new DecimalFormat("0.00").format(Double.parseDouble(
                                emergencyRoom.getDistance())) + getString(R.string.km));
                        tvAddress.setText(emergencyRoom.getAddress());
                        tvPhoneNo.setText(emergencyRoom.getPhone());
                    }
                }
                break;
        }
    }

    public void onCall(final String _phoneNo) {
        int permissionCheck = ContextCompat.checkSelfPermission(EmergencyRoomDetailActivity.this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EmergencyRoomDetailActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CODE_CALL_PERMISSIONS);
        } else {


            new dialogYesNoOption(EmergencyRoomDetailActivity.this, getString(R.string.confimation_call) + "\n" + _phoneNo) {
                @Override
                public void onClickYes() {
                    dismiss();
                    try {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + _phoneNo));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            phoneIntent.setPackage("com.android.server.telecom");
                        else
                            phoneIntent.setPackage("com.android.phone");
                        if (ActivityCompat.checkSelfPermission(EmergencyRoomDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(phoneIntent);

                    } catch (ActivityNotFoundException e) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _phoneNo));
                        startActivity(phoneIntent);
                    }
                }

                @Override
                public void onClickNo() {
                    dismiss();
                }
            }.show();


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CALL_PERMISSIONS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    onCall(emergencyRoom.getPhone());
                break;
        }
    }

}
