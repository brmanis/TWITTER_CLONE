package com.example.twitter_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login_Activity extends AppCompatActivity {

    private EditText edtLoginUname,edtLoginPwd;
    private Button btnLogin,BtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        setTitle("LOGIN");
        edtLoginUname=findViewById(R.id.edtLoginUsername);
        edtLoginPwd=findViewById(R.id.edtLoginPwd);
        btnLogin=findViewById(R.id.btnLogInTo);
        BtnSignUp=findViewById(R.id.btnBackToSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(edtLoginUname.getText().toString(),
                        edtLoginPwd.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user !=null && e==null){
                                    FancyToast.makeText(Login_Activity.this,user.get("username")+" is logged in successfully",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                    Intent intent = new Intent(Login_Activity.this, Twitter_Users.class);
                                    startActivity(intent);
                                    transitionToSocialMediaActivity();
                                }else{
                                    FancyToast.makeText(Login_Activity.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();


                                }
                            }


                        });
            }
        });
        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(Login_Activity.this,Twitter_Users.class);
        startActivity(intent);
        finish();
    }
    }
