package percept.myplan.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import percept.myplan.Global.Constant;
import percept.myplan.Global.Utils;
import percept.myplan.R;

public class LoginActivity extends AppCompatActivity {

    private Button BTN_LOGIN, BTN_SIGNUP, BTN_INFO, BTN_SHOWINFOINSIDE;
    private TextView TV_FORGOTPWD;
    private RelativeLayout LAY_INFO, REL_MAIN;
    private Utils UTILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UTILS = new Utils(LoginActivity.this);
        if (UTILS.getPreference(Constant.PREF_LOGGEDIN).equals("true")) {
            Constant.SID = UTILS.getPreference(Constant.PREF_SID);
            Constant.SNAME = UTILS.getPreference(Constant.PREF_SNAME);
            Constant.PROFILE_IMG_LINK = UTILS.getPreference(Constant.PREF_PROFILE_IMG_LINK);
            Constant.PROFILE_EMAIL = UTILS.getPreference(Constant.PREF_PROFILE_EMAIL);
            Constant.PROFILE_USER_NAME = UTILS.getPreference(Constant.PREF_PROFILE_USER_NAME);
            Constant.PROFILE_NAME = UTILS.getPreference(Constant.PREF_PROFILE_NAME);

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        }

        LAY_INFO = (RelativeLayout) findViewById(R.id.layInfo);
        REL_MAIN = (RelativeLayout) findViewById(R.id.relMainLogin);

        LAY_INFO.setVisibility(View.GONE);

        TV_FORGOTPWD = (TextView) findViewById(R.id.tvForgotPwd);

        BTN_LOGIN = (Button) findViewById(R.id.btnLogin);
        BTN_INFO = (Button) findViewById(R.id.btnShowInfo);
        BTN_SHOWINFOINSIDE = (Button) findViewById(R.id.btnShowInfoInside);
        //android:background="#55000000"
        BTN_INFO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTN_INFO.setVisibility(View.INVISIBLE);
                if (LAY_INFO.getVisibility() == View.GONE) {
                    REL_MAIN.setBackgroundColor(getResources().getColor(R.color.shadowback));
                    LAY_INFO.requestLayout();
                    LAY_INFO.setVisibility(View.VISIBLE);
                    LAY_INFO.animate()
                            .translationX(0)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    LAY_INFO.setVisibility(View.VISIBLE);
                                }
                            });

                } else {
//                    LAY_INFO.setVisibility(View.GONE);
                    LAY_INFO.animate()
                            .translationX(LAY_INFO.getWidth())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    LAY_INFO.setVisibility(View.GONE);

                                }
                            });
                }
            }
        });

        BTN_SHOWINFOINSIDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LAY_INFO.animate()
                        .translationX(LAY_INFO.getWidth())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                LAY_INFO.setVisibility(View.GONE);
                                BTN_INFO.setVisibility(View.VISIBLE);
                                REL_MAIN.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            }
                        });
            }
        });


        BTN_SIGNUP = (Button) findViewById(R.id.btnSignUp);
        BTN_LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginActivity_1.class));
                LoginActivity.this.finish();
            }
        });

        BTN_SIGNUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                LoginActivity.this.finish();
            }
        });

        TV_FORGOTPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                LoginActivity.this.finish();
            }
        });

    }
}
