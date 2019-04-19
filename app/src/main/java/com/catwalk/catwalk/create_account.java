package com.catwalk.catwalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.santalu.maskedittext.MaskEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class create_account extends Fragment {

    FloatingActionButton back;

    TextInputLayout f,l,e,p,pc;
    TextInputEditText first,last,emai,pass,cpass;
    MaskEditText dob;
    String firstname,lastname,email,password,conpassword,birth;
    int Sex;

    CardView done;

    RadioGroup gender;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rview = inflater.inflate(R.layout.login_create_ac,container,false);
        back = rview.findViewById(R.id.create_ac_back);

        f = rview.findViewById(R.id.lay_create_first_name);
        l = rview.findViewById(R.id.lay_create_last_name);
        firebaseAuth = FirebaseAuth.getInstance();

        first = rview.findViewById(R.id.create_first_name);
        last = rview.findViewById(R.id.create_last_name);
        dob = rview.findViewById(R.id.create_dob);

        e = rview.findViewById(R.id.lay_create_email);
        p = rview.findViewById(R.id.lay_create_pass);
        pc = rview.findViewById(R.id.lay_create_cpass);

        emai = rview.findViewById(R.id.create_email);
        pass = rview.findViewById(R.id.create_pass);
        cpass = rview.findViewById(R.id.create_cpass);

        gender = rview.findViewById(R.id.create_gender);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if (getActivity()!=null)
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        done = rview.findViewById(R.id.createaccount);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    request();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });





        return rview;
    }

    private void request() {
        firstname = first.getEditableText().toString();
        lastname = last.getEditableText().toString();
        email = emai.getEditableText().toString();
        birth = dob.getEditableText().toString();
        password = pass.getEditableText().toString();
        conpassword = cpass.getEditableText().toString();
        Sex = 0;
        switch (gender.getCheckedRadioButtonId()){
            case R.id.create_gender_male:
                Sex = 0;
                break;
            case R.id.create_gender_female:
                Sex=1;
                break;
            default:
                Sex = 0;
                break;

        }
        if (validate()){
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait..");
            pd.setCancelable(false);
            if (!pd.isShowing())pd.show();
            firebaseAuth.createUserWithEmailAndPassword(email,conpassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if (task.isSuccessful()){
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        updateUI(user);
                    }else{
                        Toast.makeText(getActivity(), "Something wrong. Try again!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateUI(FirebaseUser user) {
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference dr = fd.getReference().child("user");
        dr.child(user.getUid()).child("firstname").setValue(firstname);
        dr.child(user.getUid()).child("lastname").setValue(lastname);
        dr.child(user.getUid()).child("gender").setValue(Sex);
        dr.child(user.getUid()).child("birthday").setValue(birth);
        Intent it = new Intent(getActivity(),dashboard.class);
        startActivity(it);
        getActivity().finish();

    }

    private boolean validate() {
        if (firstname.isEmpty()){
            f.setError("* Required");
            return false;
        }else {
            f.setError(null);
        }
        if (lastname.isEmpty()){
            l.setError("* Required");
            return false;
        }else {
            l.setError(null);
        }
        if (email.isEmpty()){
            e.setError("* Required");
            return false;
        }else if (!Email(email)){
            e.setError("Invalid email address");
            return false;
        }else {
            e.setError(null);
        }
        if (birth.isEmpty()){
            dob.setError("* Required");
            return false;
        }
        if (password.isEmpty()) {
            p.setError("* Required");
            return false;
        }else if(!password.equals(conpassword)){
            p.setError("* Password not match");
            return false;
        }else {
            p.setError(null);
        }
        if (conpassword.isEmpty()){
            pc.setError("* Required");
            return false;
        }else {
            pc.setError(null);
        }
        return true;
    }


    public boolean Email(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}
