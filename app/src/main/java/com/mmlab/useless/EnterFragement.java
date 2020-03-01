package com.mmlab.useless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mmlab.zzjh_deh.MiniActivity;
import com.mmlab.zzjh_deh.R;
import com.mmlab.game.fragement.GroupChoiceFragement;


public class EnterFragement extends Fragment {

    private Button app_2;
    private Button app_1;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GroupChoiceFragement groupChoiceFragement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.app_enter_fragement, container, false);

        app_2 = (Button)rootView.findViewById(R.id.app_2);
        app_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragement change to group list
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                groupChoiceFragement = new GroupChoiceFragement();
                //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.main_fragment,groupChoiceFragement);
                fragmentTransaction.commit();
            }
        });

        app_1 = (Button)rootView.findViewById(R.id.app_1);
        app_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getActivity(),MiniActivity.class);
                startActivity(intent);
            }
        });



        return rootView;
    }
}
