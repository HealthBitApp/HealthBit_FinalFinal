package com.ariel.healthbit;

import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.common.util.NumberUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class updateProducts extends AppCompatActivity {

    storeProduct product1,product2,product3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);





        //add all products from "Store" on Firebase to an array list
        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("products");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String,storeProduct> td= (Map <String,storeProduct>) dataSnapshot.getValue();
                ArrayList<storeProduct> data=new ArrayList<storeProduct>();
                ArrayList<String> uid=new ArrayList<String>();
                storeProduct product;
                int j=0;
                for (Entry <String, storeProduct> entry : td.entrySet()) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    uid.add(j,entry.getKey());
                    HashMap nana = entry.getValue();


                    String LunitsInStock = (String)nana.get("inStock").toString();
                    int nitsInStock = Integer.parseInt(LunitsInStock);
                    String Lkcal = (String) nana.get("Kcal").toString();
                    Double kcal = Double.parseDouble(Lkcal);
                    String Lprice = (String) nana.get("price").toString();
                    double price = Double.parseDouble(Lprice);
                    String name = (String) nana.get("name");
                    String subType = (String) nana.get("description").toString();
                    product = new storeProduct(name, kcal, price, subType,nitsInStock);
                    data.add(product);

                    j++;
                }
                j=0;
//                    for(int i=0;i<data.size();i++)
//                        uid.add()

                int productsAmount=data.size();
                final LinearLayout lm = (LinearLayout) findViewById(R.id.linearLayout);
                lm.removeAllViews();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 50);

                for(int i=0;i<productsAmount;i++)
                {

                    LinearLayout ll = new LinearLayout(getBaseContext());
                    ll.setOrientation(LinearLayout.VERTICAL);


                    EditText productName = new EditText(getBaseContext());
                    productName.setHint("product name");
                    productName.setText(data.get(i).getName());
                    ll.addView(productName,params);



                    EditText kcal = new EditText(getBaseContext());
                    kcal.setHint("Kcal");
                    kcal.setText(""+data.get(i).getKcal());
                    ll.addView(kcal);



                    EditText priceEditT = new EditText(getBaseContext());
                    priceEditT.setHint("price");
                    priceEditT.setText(""+data.get(i).getPrice());
                    ll.addView(priceEditT);





                    EditText subType = new EditText(getBaseContext());
                    subType.setHint("description");
                    subType.setText(data.get(i).getSubType());
                    ll.addView(subType);
                    String TextDes =data.get(i).getSubType();



                    EditText st = new EditText(getBaseContext());
                    st.setHint("Stock");
                    st.setText(""+data.get(i).getUnitsInStock());
                    ll.addView(st);


                    // Create Button
                    final Button btn = new Button(getBaseContext());
                    // Give button an ID
                    btn.setId(i);
                    btn.setText("Click to update");
                    int finalI = i;
                    btn.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Map<String, Object> mapProduct = new HashMap<>();
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
                            String st1 = st.getText().toString().trim();
                            boolean boolstock=true;
                            int stock;
                            if( st1== null || st1.trim().isEmpty()) //check stock
                            {
                                st.setError("enter integer");
                                boolstock=false;

                            }
                            for (int i = 0; i < st1.length(); i++)
                            {
                                if(!Character.isDigit(st1.charAt(i)))
                                {
                                    boolstock=false;
                                    st.setError("enter integer");

                                }
                            }
                            if(boolstock)
                            {
                                stock = Integer.parseInt(st1);
                            }
                            else
                                stock=data.get(finalI).getUnitsInStock();




                            String subT=subType.getText().toString();
                            String TextPrice = priceEditT.getText().toString();
                            boolean price=true;
                            if( TextPrice== null || TextPrice.trim().isEmpty()||TextPrice.length()==0) //check the price llegal
                            {
                                priceEditT.setError("enter integer");
                                price=false;
                            }
                            for (int i = 0; i < TextPrice.length(); i++)
                            {
                                if(!Character.isDigit(TextPrice.charAt(i)))
                                {
                                    if(TextPrice.charAt(i)=='.')
                                    {

                                    }
                                    else
                                    {
                                        price=false;
                                        priceEditT.setError("enter integer");

                                    }
                                }
                            }
                            Double Dprice;
                            if(price)
                            {
                                Dprice = Double.parseDouble(TextPrice);
                            }
                            else
                                Dprice=data.get(finalI).getPrice();



                            String Kcal = kcal.getText().toString();
                            boolean cal =true;
                            if( Kcal== null || Kcal.trim().isEmpty()||Kcal.length()==0) //check kcal
                            {
                                kcal.setError("enter integer");
                                cal=false;
                            }

                            for (int i = 0; i < Kcal.length(); i++)
                            {
                                if(!Character.isDigit(Kcal.charAt(i)))
                                {
                                    if(Kcal.charAt(i)=='.')
                                    {

                                    }
                                    else
                                    {
                                        cal=false;
                                        kcal.setError("enter integer");

                                    }


                                }
                            }
                            Double KcalInt;
                            if(cal)
                            {
                                KcalInt = Double.parseDouble(Kcal);
                            }
                            else
                                KcalInt=data.get(finalI).getKcal();
                            String TextName=productName.getText().toString().trim();

                            storeProduct update=new storeProduct(TextName,KcalInt,Dprice,subT,(int)stock);
                            mapProduct.put("name",TextName);
                            mapProduct.put("Kcal",KcalInt);
                            mapProduct.put("inStock",stock);
                            mapProduct.put("description",subT);
                            mapProduct.put("price",Dprice);
                            if (TextName.length()==1||TextName.length()==0)//check name
                            {
                                productName.setError("invalid name");
                                TextName=data.get(finalI).name;
                            }





                            if( subT== null || subT.trim().isEmpty()||subT.length()==0) //check llegal description
                            {
                                subType.setError("enter Description");
                                return;
                            }





                            productsRefernce.child(uid.get(btn.getId())).updateChildren(mapProduct);

                        }


                    });
                    // set the layoutParams on the button

                    //   btn.setLayoutParams(params);
                    //Add button to LinearLayout
                    ll.addView(btn);
                    //Add button to LinearLayout defined in XML

                    lm.addView(ll);

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }

        };

        productsRefernce.addValueEventListener(postListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}