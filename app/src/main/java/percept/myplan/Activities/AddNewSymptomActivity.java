package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import percept.myplan.R;

public class AddNewSymptomActivity extends AppCompatActivity {

    private TextView TV_ADDSTRATEGY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_symptom);

        TV_ADDSTRATEGY= (TextView) findViewById(R.id.tvAddStrategy);
        TV_ADDSTRATEGY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
