package percept.myplan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import percept.myplan.R;
import percept.myplan.customviews.PinEntryEditText;

public class LoginActivity_1 extends AppCompatActivity {

    private TextView TV_FORGETPWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);

        TV_FORGETPWD= (TextView) findViewById(R.id.tvForgotPwd);

        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.toString().equals("1234")) {
                        Toast.makeText(LoginActivity_1.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(LoginActivity_1.this, HomeActivity.class));
                        //LoginActivity_1.this.finish();
                    } else {
                        pinEntry.setError(true);
                        Toast.makeText(LoginActivity_1.this, "FAIL", Toast.LENGTH_SHORT).show();
                        pinEntry.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pinEntry.setText(null);
                            }
                        }, 1000);
                    }
                }
            });
        }

//        startActivity(new Intent(LoginActivity_1.this, HomeActivity.class));
//        LoginActivity_1.this.finish();


        TV_FORGETPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity_1.this,ForgetPwdActivity.class));
                LoginActivity_1.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity_1.this, LoginActivity.class));
    }
}
