package percept.myplan.Activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import percept.myplan.R;

public class ForgetPwdActivity extends AppCompatActivity {


    private Button BTN_SEND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        BTN_SEND = (Button) findViewById(R.id.btnSend);

        BTN_SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ForgetPwdActivity.this, LoginActivity_1.class);
                startActivity(homeIntent);
                ForgetPwdActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, LoginActivity_1.class);
                startActivity(homeIntent);
//                Toast.makeText(ForgetPwdActivity.this, "Hahahaha You are not going back", Toast.LENGTH_SHORT).show();
                ForgetPwdActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
