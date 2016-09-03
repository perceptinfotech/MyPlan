package percept.myplan.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import percept.myplan.Dialogs.dialogYesNoOption;
import percept.myplan.POJO.NearestEmergencyRoom;
import percept.myplan.R;

public class EmergencyRoomDetailActivity extends AppCompatActivity {

    private NearestEmergencyRoom emergencyRoom;
    private TextView tvPhoneNo;
    private TextView tvDistance;
    private TextView tvRoomTitle;
    private TextView tvLandmarkName;
    private TextView tvDistanceTime;
    private TextView tvAddress;
    private TextView tvWebsite;
    private ImageView ivRoomImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_room_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);


        ivRoomImage = (ImageView) findViewById(R.id.ivRoomImage);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistanceTime = (TextView) findViewById(R.id.tvDistanceTime);
        tvLandmarkName = (TextView) findViewById(R.id.tvLandmarkName);
        tvRoomTitle = (TextView) findViewById(R.id.tvRoomTitle);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);

        if (getIntent().hasExtra("EMERGENCY_ROOM_DETAIL")) {

            emergencyRoom = (NearestEmergencyRoom) getIntent().getSerializableExtra("EMERGENCY_ROOM_DETAIL");
            mTitle.setText(emergencyRoom.getRoomName());
            if (!TextUtils.isEmpty(emergencyRoom.getImage()))
                Picasso.with(EmergencyRoomDetailActivity.this).load(emergencyRoom.getImage()).into(ivRoomImage);
            tvRoomTitle.setText(emergencyRoom.getRoomName());
            tvDistance.setText(new DecimalFormat("0.00").format(Double.parseDouble(
                    emergencyRoom.getDistance())) + getString(R.string.km));
            tvAddress.setText(emergencyRoom.getAddress());
            tvPhoneNo.setText(emergencyRoom.getPhone());
            tvWebsite.setText(emergencyRoom.getWebsite());
            tvDistanceTime.setText(emergencyRoom.getDistance() + getString(R.string.km));
        }

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new dialogYesNoOption(EmergencyRoomDetailActivity.this, getString(R.string.open_google_maps)) {

                    @Override
                    public void onClickYes() {
                        dismiss();
                        String url = "http://maps.google.com/maps?f=d&daddr=" +emergencyRoom.getAddress() + "&dirflg=d&layer=t";
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EmergencyRoomDetailActivity.this.finish();
                return true;
        }
        return false;
    }
}
