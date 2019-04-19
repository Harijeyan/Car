package com.catwalk.catwalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class dashboard extends AppCompatActivity {


    Button signout;
    TextView user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
        signout = (Button) findViewById(R.id.signout);
        user =(TextView) findViewById(R.id.hellouser);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser!=null){
            details(currentUser);
        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent it = new Intent(dashboard.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    private void details(FirebaseUser currentUser) {
        if (currentUser.getDisplayName()!=null) {
            user.setText("Hello " + currentUser.getDisplayName());
        }else{
            FirebaseDatabase fd = FirebaseDatabase.getInstance();
            DatabaseReference dr = fd.getReference().child("user").child(currentUser.getUid());
            dr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String first = dataSnapshot.child("firstname").getValue(String.class);
                    String last = dataSnapshot.child("lastname").getValue(String.class);
                    user.setText("Hello "+first+" "+last);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
