package com.example.debatetracker;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.debatetracker.ui.login.LoginActivity;
import com.example.debatetracker.ui.qrcode.QrScanner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final String CHANNEL_ID = "notification";
    public static final String CHANNEL_NAME = "notification";
    public static final String CHANNEL_DESC = "first notification";

    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey = "key=AAAAJzcdKaY:APA91bFj7byTGVMWLYp7NVC26FFLKlAtSjCuLvruHwuMqggbq6S1DKLkcu3kmGrXhIO5MbDbNyF3edvlG4pKIMDWv6tD5zVtoJFvQtWAz_FTBH-Si-DkMZlIiCocDhUZMx2S8ROwQUU2";
    private String contentType = "application/json";

    Button btnLogout;
    Button btnQrCode;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Initiate http facade
        HttpFacade.loadInstance(getApplicationContext());
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/topic_name");

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String topic = "/topics/topic_name";

                JSONObject notification = new JSONObject();
                JSONObject notificationBody = new JSONObject();

                try {
                    notificationBody.put("title", "Broadcast");
                    notificationBody.put("body", "Notification Broadcast");
                    notification.put("to", topic);
                    notification.put("notification", notificationBody);
                    Log.e("Send notif setup", "try");
                } catch (JSONException e) {
                    Log.e("Send notif setup", "onCreate:" + e.getMessage());
                }

                sendNotification(notification);

                Snackbar.make(view, "Notification broadcast ! :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            private void sendNotification(JSONObject notification) {
                Log.i("Sending notif", "sendNotification");

                HttpFacade httpFacade = HttpFacade.getInstance();
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        FCM_API,
                        notification,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Notif", "Broadcast succeeded");
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Notif", "Broadcast failed " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders(){
                        Map<String, String> params = new HashMap();
                        params.put("Authorization", serverKey);
                        params.put("Content-Type", contentType);
                        return params;
                    }
                    
                };

                httpFacade.addToRequestQueue(request);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("getInstanceId failed" + task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.i("MyActivity", token);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intToLogin);
            }
        });

        btnQrCode = findViewById(R.id.qrbutton);
        btnQrCode.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, QrScanner.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
