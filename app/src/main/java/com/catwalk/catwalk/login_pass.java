package com.catwalk.catwalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.catwalk.catwalk.MainActivity.STEP;
import static com.catwalk.catwalk.MainActivity.password;
import static com.catwalk.catwalk.MainActivity.username;

public class login_pass extends Fragment {

    FloatingActionButton back;
    TextInputEditText pass;
    TextInputLayout lpass;
    FloatingActionButton lognow;
    Context context;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View rview = inflater.inflate(R.layout.login_password,container,false);
        back = rview.findViewById(R.id.login_password_back);
        pass = rview.findViewById(R.id.login_inputpass);
        lpass = rview.findViewById(R.id.login_inputpass_lay);
        lognow = rview.findViewById(R.id.main_login);
        STEP = 1;
        context = getActivity();
        firebaseAuth = FirebaseAuth.getInstance();


        if (savedInstanceState!=null){
            pass.setText(savedInstanceState.getString("user"));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });

        lognow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = pass.getEditableText().toString();
                if (!password.isEmpty()){
                    if (getActivity()!=null){
                        loginnow();
                    }

                }else{
                    lpass.setError("Please enter your password");
                }
            }
        });


        return rview;
    }

    private void loginnow() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait..");
        pd.setCancelable(false);
        if (!pd.isShowing())pd.show();
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pd.dismiss();
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        Intent it = new Intent(getActivity(),dashboard.class);
        startActivity(it);
        getActivity().finish();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user",pass.getEditableText().toString());
    }

}
