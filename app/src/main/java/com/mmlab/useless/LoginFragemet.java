package com.mmlab.useless;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mmlab.zzjh_deh.R;
import com.mmlab.zzjh_deh.model.DEHUser;
import com.mmlab.game.ApiService;
import com.mmlab.game.db.DbHandler;
import com.mmlab.game.module.LoginForm;
import com.mmlab.game.module.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragemet extends Fragment {

    private Button login;
    private EditText pw_txt;
    private EditText us_txt;
    private TextView pw_tw;
    private TextView us_tw;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private EnterFragement enterFragement;

    private DbHandler dbHandler;
    private SQLiteDatabase db;

    private String pw;
    private String un;

    private String BASE_URL = "http://140.116.82.130:8080/";
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiService api = retrofit.create(ApiService.class);
    private Call<User> call;

    private Realm realm;
    private RealmResults<DEHUser> userResult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.login, container, false);

        //db handler
        dbHandler = new DbHandler(getContext(), "database.db", null, 1);
        db = dbHandler.getWritableDatabase();

        //set login button
        login = (Button) rootView.findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(us_txt.getText().toString()!=null && pw_txt.getText().toString()!=null) {
                    un = us_txt.getText().toString();
                    pw = pw_txt.getText().toString();

                    //
                    realm = Realm.getInstance(getContext());
                    userResult = realm.where(DEHUser.class).findAll();
                    for (DEHUser user : userResult) {
                        un = user.getId();
                        pw = user.getPw();
                    }

                    //user login api
                    Call<User> call = api.getUser(new LoginForm(un,md5(pw),"extn"));
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            //login sucess
                            if (response.body().getNickname()!= null) {
                                //write user data to db
                                db.delete("user_data", null, null);
                                ContentValues cv = new ContentValues();
                                cv.put("username",un);
                                cv.put("passward",pw);
                                cv.put("user_id",response.body().getUser_id());
                                db.insert("user_data", null, cv);

                                //test realm
//                                Realm realm = Realm.getInstance(getContext());
//                                realm.beginTransaction();
//                                User user = realm.createObject(User.class);
//                                user = response.body();
//                                user.setUser_id(response.body().getUser_id());
//                                realm.commitTransaction();
//                                RealmResults<User> result2 = realm.where(User.class).findAll();
//                                Log.d("msg", ""+ response.body().getUser_id()+
//                                        " == "+result2.last().getUser_id()+
//                                        " == "+result2.first().getUser_id()
//                                );

                                //fragement switch
                                fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                enterFragement = new EnterFragement();
                                fragmentTransaction.replace(R.id.main_fragment,enterFragement);
                                fragmentTransaction.commit();
                            }

                            //login false
                            else {
                                final Dialog dialog = new Dialog(getActivity());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.alert_dialog_1btn);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                                //dialog title
                                TextView title = (TextView) dialog.findViewById((R.id.title_text));
                                title.setText("Wrong password");

                                //dialog button
                                Button rightButton = (Button) dialog.findViewById(R.id.bottom_btn);
                                rightButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });


                }
            }
        });

        //set EditText and give initail value
        us_txt = (EditText) rootView.findViewById(R.id.username_txt);
        us_txt.getBackground().mutate().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        pw_txt = (EditText) rootView.findViewById(R.id.password_txt);
        pw_txt.getBackground().mutate().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        String[] query1 = new String[] {"username","passward"};
        Cursor cursor = db.query("user_data", query1, null, null, null, null, "_id");
        if(cursor.getCount() != 0){
            cursor.moveToLast();
            us_txt.setText(cursor.getString(0));
            pw_txt.setText(cursor.getString(1));
        }




        return rootView;
    }


    private String md5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
