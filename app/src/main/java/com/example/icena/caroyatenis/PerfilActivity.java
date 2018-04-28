package com.example.icena.caroyatenis;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PerfilActivity extends AppCompatActivity {

    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            //View headerView = navigationView.getHeaderView(0);

            TextView txtNombre = (TextView) findViewById(R.id.txtPerfinNombre);
            String nombre = user.getDisplayName();
            txtNombre.setText(nombre);

            ImageView photoImageView = (ImageView) findViewById(R.id.imagViewPerfil);
            Uri photoUrl = user.getPhotoUrl();
            Glide.with(getApplicationContext())
                    .load(photoUrl)
                    .into(photoImageView);


        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}