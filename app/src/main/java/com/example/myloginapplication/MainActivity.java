package com.example.myloginapplication;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myloginapplication.Model.Member;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    String appId = "application-0-joalz";
    static MongoDatabase mongoDatabase;
    static MongoClient mongoClient;
    static MongoCollection<Member> mongoCollection;
    static CodecRegistry pojoCodecRegistry;
    User user;
    static App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        app =new App(new AppConfiguration.Builder(appId).build());

        Credentials credentials = Credentials.emailPassword("kpr11@student.le.ac.uk","mobileapp");
        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    Log.v("User","Logged in as kpr11");
                }else{
                    Log.v("User","Failed to login");
                    Log.v("User",result.getError().toString());
                }
            }
        });

        user= app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("electricity");
        pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoCollection = mongoDatabase.getCollection("member",Member.class).withCodecRegistry(pojoCodecRegistry);

        Button signupbtn = findViewById(R.id.signupbtn);
        Button signinbtn = findViewById(R.id.signinbtn);

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
    }

    public void openregister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void opensignin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}