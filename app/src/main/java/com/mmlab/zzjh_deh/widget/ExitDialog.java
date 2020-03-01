package com.mmlab.zzjh_deh.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mmlab.zzjh_deh.MiniActivity;
import com.mmlab.zzjh_deh.R;


public class ExitDialog extends DialogFragment {

    public interface GroupListener {
        void onGroupCreate(String group_name, String group_description);
    }

    public ExitDialog() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void onDetach() {
        super.onDetach();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new MaterialDialog.Builder(getActivity()).title(getResources().getString(R.string.leave_title))
                .content(getResources().getString(R.string.Are_you_sure))
                .positiveText(getResources().getString(R.string.confirm))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        ((MiniActivity)getActivity()).exitDEH();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                    }
                })
                .negativeText(getResources().getString(R.string.cancel)).build();
    }
}
