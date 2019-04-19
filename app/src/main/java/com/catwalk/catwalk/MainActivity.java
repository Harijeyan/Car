package com.catwalk.catwalk;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    public static String username;
    public static String password;
    public static int STEP =0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        switch (STEP){
            case 0:
                FragmentManager fragmentManagerot = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionot = fragmentManagerot.beginTransaction();
                fragmentTransactionot.replace(R.id.freamview, new Login_email());
                fragmentTransactionot.commit();
                break;
            case 1:
                FragmentManager fragmentManagerx = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionx = fragmentManagerx.beginTransaction();
                fragmentTransactionx.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
                fragmentTransactionx.replace(R.id.freamview, new login_pass());
                fragmentTransactionx.addToBackStack(null);
                fragmentTransactionx.commit();
                break;
            case 2:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.freamview, new create_account());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
             default:
                 FragmentManager fragmentManagero = getSupportFragmentManager();
                 FragmentTransaction fragmentTransactiono = fragmentManagero.beginTransaction();
                 fragmentTransactiono.replace(R.id.freamview, new Login_email());
                 fragmentTransactiono.commit();
                 break;
        }



    }


}
