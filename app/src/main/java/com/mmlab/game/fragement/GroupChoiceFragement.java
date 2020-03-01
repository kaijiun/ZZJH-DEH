package com.mmlab.game.fragement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmlab.zzjh_deh.model.DEHUser;
import com.mmlab.game.ApiService;
import com.mmlab.game.GameActivity;
import com.mmlab.game.callback.GroupFragmentCallback;
import com.mmlab.game.db.DbHandler;
import com.mmlab.game.module.Group;
import com.mmlab.game.module.Id;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.mmlab.zzjh_deh.R;
import com.mmlab.game.module.LoginForm;
import com.mmlab.game.module.User;
import com.mmlab.game.thread.ChestThread;

public class GroupChoiceFragement extends Fragment implements GroupFragmentCallback
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FragmentManager fragmentManager;

    private TextView score_url;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.group_choice, container, false);

        //build recycle view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.group_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        GameActivity.buildGroupFragmentCallback(this);
        GameActivity.apiGroup();

        ChestThread.setDoRun(false);

        score_url = (TextView) rootView.findViewById(R.id.score_url);
        score_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://deh.csie.ncku.edu.tw/extn"));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }


    @Override
    public void onGroupListReady() {
        fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new GroupChoiceAdapter(fragmentManager,getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }
}
