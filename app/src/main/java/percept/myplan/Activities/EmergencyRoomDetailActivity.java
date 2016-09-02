package percept.myplan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

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
        mTitle.setText(getResources().getString(R.string.title_activity_add_contact));

        ivRoomImage = (ImageView) findViewById(R.id.ivRoomImage);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistanceTime = (TextView) findViewById(R.id.tvDistanceTime);
        tvLandmarkName = (TextView) findViewById(R.id.tvLandmarkName);
        tvRoomTitle = (TextView) findViewById(R.id.tvRoomTitle);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);

        if (getIntent().hasExtra("EMERGENCY_ROOM_DETAIL")) {

            Picasso.with(EmergencyRoomDetailActivity.this).load(emergencyRoom.getImage()).into(ivRoomImage);
            emergencyRoom = (NearestEmergencyRoom) getIntent().getSerializableExtra("EMERGENCY_ROOM_DETAIL");
            tvRoomTitle.setText(emergencyRoom.getRoomName());
            tvDistance.setText(new DecimalFormat("0.00").format(Double.parseDouble(
                    emergencyRoom.getDistance())) + getString(R.string.km));
            tvAddress.setText(emergencyRoom.getAddress());
            tvPhoneNo.setText(emergencyRoom.getPhone());
            tvWebsite.setText(emergencyRoom.getWebsite());
            tvDistanceTime.setText(emergencyRoom.getDistance() + getString(R.string.km));
        }

    }
}
