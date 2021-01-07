package com.ariel.healthbit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class adminMenu1 extends AppCompatActivity {

    public void callPhoneNumber()
    {
        try
        {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                ActivityCompat.requestPermissions(adminMenu1.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                return;
            }


        }
        catch (Exception ex)
        {
            Toast.makeText(adminMenu1.this, "Permission not Granted",
                    Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu1);
        Toolbar toolbar = findViewById(R.id.upload);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button update = (Button) findViewById(R.id.updateP); //logout, and move to the login activity
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getApplicationContext(), updateProducts.class);
                startActivity(myIntent);
            }
        });


        Button newPro1 = (Button) findViewById(R.id.newPro2); //logout, and move to the login activity
        newPro1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getApplicationContext(), newProducts.class);
                startActivity(myIntent);
            }
        });
        callPhoneNumber();
        Button ordersButton = (Button) findViewById(R.id.orders); //logout, and move to the login activity
        ordersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {

                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getApplicationContext(), activityOrders.class);
                startActivity(myIntent);
            }
        });





    }

}