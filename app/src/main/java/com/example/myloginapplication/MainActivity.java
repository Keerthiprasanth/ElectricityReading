package com.example.myloginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    String appId = "application-0-joalz";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    User user;
    static App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        app =new App(new AppConfiguration.Builder(appId).build());

        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    Log.v("User","Logged in anonymously");
                }else{
                    Log.v("User","Failed to login");
                }
            }
        });

        user= app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("electricity");

        Button signupbtn = findViewById(R.id.signupbtn);
        Button signinbtn = findViewById(R.id.signinbtn);
        Button dashboard = findViewById(R.id.dashboardbtn);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openregister();
            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensignin();
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendashboard();
            }
        });
    }

    public void openregister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void opensignin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void opendashboard(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}