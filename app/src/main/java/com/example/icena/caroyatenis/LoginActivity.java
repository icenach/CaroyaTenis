package com.example.icena.caroyatenis;

/**
 * Created by icena on 21/8/2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity  {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    private String profilePicture;


    //private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //progressBar = (ProgressBar) findViewById(R.id.progressBar);



        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.loginButton);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

        //////////////////////////////////////////////////////////////////////////////////////////

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("response: ", response + "");



                        try {
                            String userId = object.getString("id").toString();
                            profilePicture ="https://graph.facebook.com/" + userId + "/picture?width=500&height=500";



                        } catch (JSONException e) {
                            Log.e("error:", e + "");
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender , location");
                request.setParameters(parameters);
                request.executeAsync();
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        };

        loginButton.registerCallback(callbackManager, callback);

        /////////////////////////////////////////////////////////////////////////////////////////


        /*loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                //////////////////////////////////////////////////////////////////////////////////
                *//*GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.e("response: ", response + "");
                                try {
                                    String id = object.getString("id").toString();
                                    String email = object.getString("email").toString();
                                    String name = object.getString("name").toString();
                                    String profilePicUrl = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large";
                                    Log.d("imageFB", profilePicUrl);
                                    Log.d("FB_ID:", id);


                                    checkFBLogin(profilePicUrl);



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                //finish();
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,picture");
                request.setParameters(parameters);
                request.executeAsync();*//*

                /////////////////////////////////////////////////////////////////////////////////
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });*/

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();

                }
            }
        };
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        //progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                //progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkFBLogin (String uriFoto){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("parametro", uriFoto);
        startActivity(intent);

    }


    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("imageUrl",profilePicture);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}