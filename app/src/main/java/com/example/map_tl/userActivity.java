package com.example.map_tl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userActivity extends AppCompatActivity {

    private EditText emailTextField;
    private Button mapBtn, mapUserLocBtn;
    private DatabaseReference fbdatabase, ref;
    private FirebaseAuth fb_Auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setupUIviews();

        // Map button initialization
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userActivity.this, MapActivity.class));
            }
        });

        // family location getting form
        mapUserLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_email = emailTextField.getText().toString().trim().split("[\\.@]+")[0];

                String u_email = emailTextField.getText().toString().trim();
                fb_Auth = FirebaseAuth.getInstance();
                fb_Auth.fetchSignInMethodsForEmail(u_email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean isUser = task.getResult().getSignInMethods().isEmpty();
                        if(isUser){
                            Toast.makeText(userActivity.this, "Not Exists ", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(userActivity.this, "Exists ", Toast.LENGTH_SHORT).show();
                            fbdatabase = FirebaseDatabase.getInstance().getReference();
                            ref = fbdatabase.child("users").child(user_email).child("location");


                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    double user_ln = (double) dataSnapshot.child("longitude").getValue();
                                    double user_lat = (double) dataSnapshot.child("latitude").getValue();

                                    //Sending parameter to another activity
                                    Intent usermap = new Intent(userActivity.this, MapActivity.class);

                                    Bundle locBundle = new Bundle();
                                    locBundle.putString("lat", String.valueOf(user_lat));
                                    locBundle.putString("ln", String.valueOf(user_ln));
                                    locBundle.putString("name",user_email);

                                    usermap.putExtras(locBundle);

                                    startActivity(usermap);

                                    Toast.makeText(userActivity.this, "user location : "+user_lat+ "..." +user_ln, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void setupUIviews(){
        // Initializing the UI elements
        mapBtn = (Button)findViewById(R.id.mapBtn);
        mapUserLocBtn = (Button)findViewById(R.id.mapUserLocBtn);
        emailTextField = (EditText)findViewById(R.id.emailTextField);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this,"Logging-out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(userActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
