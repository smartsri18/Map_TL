package com.example.map_tl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class userRegister extends AppCompatActivity {

    private EditText u_name, u_pwd, u_email;
    private Button regbtn;
    private TextView u_login;
    private FirebaseAuth fb_Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        setupUIviews();
        fb_Auth = FirebaseAuth.getInstance();

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_validate()){
                    //database
                    String user_email = u_email.getText().toString().trim();
                    String user_pwd = u_pwd.getText().toString().trim();

                    fb_Auth.createUserWithEmailAndPassword(user_email,user_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){
                              Toast.makeText(userRegister.this,"Successfully Registered..", Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(userRegister.this, MainActivity.class));
                          }
                          else{
                              Toast.makeText(userRegister.this,"Registeration Failed", Toast.LENGTH_SHORT).show();
                          }
                        }
                    });
                }
            }
        });

        u_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userRegister.this,MainActivity.class));
            }
        });
    }

    private void setupUIviews(){
        u_name = (EditText)findViewById(R.id.etUserName);
        u_email = (EditText)findViewById(R.id.etUserEmail);
        u_pwd = (EditText)findViewById(R.id.etUserPassword);
        regbtn = (Button)findViewById(R.id.btnRegister);
        u_login = (TextView)findViewById(R.id.tvUserLogin);
    }

    private Boolean reg_validate(){
        Boolean result = false;

        String name = u_name.getText().toString();
        String pwd = u_pwd.getText().toString();
        String email = u_email.getText().toString();

        if(name.isEmpty() || pwd.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Please enter all the details", Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;
    }
}
