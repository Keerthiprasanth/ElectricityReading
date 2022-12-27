package com.example.myloginapplication;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {
    Realm uiThreadRealm;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    User user;
    App app;
    String AppId = "application-0-joalz";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signupbtn = findViewById(R.id.signupbtn);
        Button signinbtn = findViewById(R.id.signinbtn);
        Button dashboard = findViewById(R.id.dashboardbtn);

        Realm.init(this);


//        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
//                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//        MongoClient mongoClient = MongoClients.create("mongodb+srv://breakup:breakup@cluster0.p3rjm4h.mongodb.net/splitdb?retryWrites=true&w=majority");
//        MongoDatabase database = mongoClient.getDatabase("splitdb");
//        database = database.withCodecRegistry(pojoCodecRegistry);

        MongoDatabase finalDatabase = database;
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app = new App(new AppConfiguration.Builder(AppId).build());

                if (app.currentUser() == null) {
                    Toast.makeText(MainActivity.this, "user is null", Toast.LENGTH_SHORT).show();
                    app.loginAsync(Credentials.anonymous(), new App.Callback<User>() {
                        @Override
                        public void onResult(App.Result<User> result) {
                            if (result.isSuccess()) {
                                Log.v("User", "Logged In Successfully");
                                initializeMongoDB();
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            } else {
                                Log.v("User", "Failed to Login");
                                Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "user is not null", Toast.LENGTH_SHORT).show();
                    initializeMongoDB();
                }
                openregister(finalDatabase);
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

    private void initializeMongoDB() {
        user = app.currentUser();
        mongoClient = (MongoClient) user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("electricity");
        mongoCollection = mongoDatabase.getCollection("member");
    }

    public void openregister(MongoDatabase mongoDatabase){
        mongoDatabase.createCollection("test");
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