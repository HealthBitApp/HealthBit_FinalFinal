package com.ariel.healthbit;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class newProducts extends AppCompatActivity {
    EditText price, kcal, des, nameProduct, stock;
    Button setP;
    FirebaseAuth auth;
    FirebaseDatabase root;
    DatabaseReference refUser, refStore;
    Map<String, Object> mapProduct = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_products);
        Toolbar toolbar = findViewById(R.id.upload);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        price = (EditText) findViewById(R.id.price);
        kcal = (EditText) findViewById(R.id.kcal);
        des = (EditText) findViewById(R.id.des);
        nameProduct = (EditText) findViewById(R.id.nameProduct);
        stock = (EditText) findViewById(R.id.stock);
        setP = (Button)findViewById(R.id.setP);




        root=FirebaseDatabase.getInstance();

        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference();






        setP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TextPrice = price.getText().toString().trim();

                if( TextPrice== null||  TextPrice.trim().isEmpty()||TextPrice.length()==0) //check the price llegal
                {
                    price.setError("enter integer");
                    return;
                }
                for (int i = 0; i < price.length(); i++)
                {
                    if(!Character.isDigit(TextPrice.charAt(i)))
                    {
                        if ((TextPrice.charAt(i)=='.'))
                        {
                            continue;

                        }
                        else
                        {
                            price.setError("enter integer");
                            return;
                        }

                    }
                }
                Double doublePrice=Double.parseDouble(TextPrice);

                String TextKcal = kcal.getText().toString().trim();
                if( TextKcal== null|| TextKcal.trim().isEmpty()||TextKcal.length()==0) //check the price llegal
                {
                    kcal.setError("enter integer");
                    return;
                }
                for (int i = 0; i < kcal.length(); i++)
                {
                    if(!Character.isDigit(TextKcal.charAt(i)))
                    {

                        {
                            kcal.setError("enter integer");
                            return;
                        }

                    }
                }
                Double doubleKcal=Double.parseDouble(TextKcal);

                String TextDes = des.getText().toString().trim();
                if( TextDes== null|| TextDes.trim().isEmpty()||TextDes.length()==0) //check the price llegal
                {
                    des.setError("enter Description");
                    return;
                }
                String TextName = nameProduct.getText().toString().trim();
                if( TextName== null|| TextName.trim().isEmpty()||TextName.length()==0) //check the price llegal
                {
                    nameProduct.setError("enter Name");
                    return;
                }
                String st = stock.getText().toString().trim();
                if( st== null|| st.trim().isEmpty()||st.length()==0) //check the stock llegal
                {
                    stock.setError("enter integer");
                    return;
                }
                for (int i = 0; i < st.length(); i++)
                {
                    if(!Character.isDigit(st.charAt(i)))
                    {
                        stock.setError("enter integer");
                        return;
                    }
                }
                int intStock = Integer.parseInt(st);

                storeProduct newP=new storeProduct(TextName,doubleKcal,doublePrice,TextDes,intStock);
                System.out.println();
                mapProduct.put("name",TextName);
                mapProduct.put("Kcal",doubleKcal);
                mapProduct.put("inStock",intStock);
                mapProduct.put("description",TextDes);
                mapProduct.put("price",doublePrice);


                productsRefernce.child("products").push().setValue(mapProduct);


            }

        });

    }
}









  /*      EditText p =(EditText)findViewById(R.id.price);
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setText("");
            }
        });

        EditText kcal =(EditText)findViewById(R.id.kcal);
        kcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kcal.getText().clear();
            }
        });
        EditText stock =(EditText)findViewById(R.id.stock);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock.getText().clear();
            }
        });

        EditText des =(EditText)findViewById(R.id.des);
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des.getText().clear();
            }
        });

        EditText nameProduct =(EditText)findViewById(R.id.nameProduct);
        nameProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameProduct.getText().clear();
            }
        });
*/



