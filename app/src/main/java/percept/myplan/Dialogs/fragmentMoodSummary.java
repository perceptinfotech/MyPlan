package percept.myplan.Dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import percept.myplan.R;

/**
 * Created by percept on 16/7/16.
 */

public class fragmentMoodSummary extends DialogFragment {
    private TextView TV_MOODNOTE;


    public static fragmentMoodSummary newInstance(String note) {
        fragmentMoodSummary f = new fragmentMoodSummary();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("NOTE", note);
        f.setArguments(args);

        return f;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.lay_moodrating_summary, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TV_MOODNOTE = (TextView) _view.findViewById(R.id.tvMoodNote);

        TV_MOODNOTE.setText(getArguments().getString("NOTE"));

        return _view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}
