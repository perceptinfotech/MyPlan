package percept.myplan.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import percept.myplan.Activities.StrategyDetailsOtherActivity;
import percept.myplan.R;


/**
 * Created by percept on 2/5/15.
 */
public class dialogAddStrategy extends Dialog implements View.OnClickListener {
    private Context CONTEXT;
    private TextView TV_YES, TV_NO;

    public dialogAddStrategy(Context context) {

        super(context, R.style.DialogTheme);
        this.CONTEXT = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_strategy);

        TV_YES = (TextView) findViewById(R.id.tvYes);
        TV_YES.setOnClickListener(this);
        TV_NO = (TextView) findViewById(R.id.tvNo);
        TV_NO.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvYes:
                StrategyDetailsOtherActivity.IS_YES = true;
                dismiss();
                break;
            case R.id.tvNo:
                StrategyDetailsOtherActivity.IS_YES = false;
                dismiss();
                break;


        }
    }

    @Override
    public void onBackPressed() {
        StrategyDetailsOtherActivity.IS_YES = false;
        super.onBackPressed();
    }
}
