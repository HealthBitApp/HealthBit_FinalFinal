package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class activityOrders extends AppCompatActivity {

    storeProduct product1,product2,product3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);





        //add all products from "Store" on Firebase to an array list
        DatabaseReference userRfe= FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference proRfe= FirebaseDatabase.getInstance().getReference("products");
        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("Orders");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Order> td = (Map<String, Order>) dataSnapshot.getValue();
                ArrayList<Order> data = new ArrayList<Order>();
                ArrayList<String> uid = new ArrayList<String>();
                Order product;
                int j = 0;
                String curr = "" + j;
                for (Entry<String, Order> entry : td.entrySet()) {
                    String getPath = entry.getKey().toString();
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2@@@@@@@@@" + getPath);
                    FirebaseDatabase.getInstance().getReference("Orders/" + getPath);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2@@@@@@@@@" + FirebaseDatabase.getInstance().getReference("Orders/" + getPath).toString());
                    ArrayList<productOrder> itemQuantity = new ArrayList<>();
                    HashMap nana = entry.getValue();
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2@@@@@@@@@" + FirebaseDatabase.getInstance().getReference("Orders/" + getPath + "/itemQuantity" + "/" + curr).toString());





                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2@@@@@@@@@" + dataSnapshot.child(getPath).child("itemQuantity").child(curr));
                    while(dataSnapshot.child(getPath).child("itemQuantity").child(curr).exists()) {
                        String name = dataSnapshot.child(getPath).child("itemQuantity").child(curr).child("item").getValue().toString();
                        long amount = (long) dataSnapshot.child(getPath).child("itemQuantity").child(curr).child("amount").getValue();
                        long price1 = (long) dataSnapshot.child(getPath).child("itemQuantity").child(curr).child("price").getValue();
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2@@@@@@@@@"+name);
                        itemQuantity.add(j,new productOrder(name,(int)amount,price1));
                        j++;
                        curr=""+j;
                    }
                    j=0;
                    curr=""+j;








                    long price = (long) nana.get("totalPrice");

                    Double priceD = price -(0.0);


                    String userUid = (String) nana.get("userUID");
                    product = new Order(userUid, itemQuantity, priceD);
                    data.add(product);


                }

//                    for(int i=0;i<data.size();i++)
//                        uid.add()

                int productsAmount=data.size();
                final LinearLayout lm = (LinearLayout) findViewById(R.id.linearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 50);

                for(int i=0;i<productsAmount;i++)
                {
                    LinearLayout ll = new LinearLayout(getBaseContext());
                    ll.setOrientation(LinearLayout.VERTICAL);
                    final  Button btn = new Button(getBaseContext());
                    btn.setId(i);
                    btn.setText("Click to call");

                    ll.addView(btn);
                    int finalI = i;


                    for (int w=0;w<data.get(i).getItemQuantity().size();w++)
                    {


                        int finalW = w;
                        proRfe.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                TextView amount=new TextView(getBaseContext());
                                amount.setText("the amount of product"+data.get(finalI).getItemQuantity().get(finalW).amount);
                                ll.addView(amount);
                                TextView name = new TextView(getBaseContext());
                                name.setText(( dataSnapshot.child(data.get(finalI).getItemQuantity().get(finalW).Item).child("name").getValue().toString()));//name of product
                                ll.addView(name);
                                TextView priceProduct=new TextView(getBaseContext());
                                priceProduct.setText("sum of price for this product"+data.get(finalI).getItemQuantity().get(finalW).price);
                                ll.addView(priceProduct);




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                    }





                    TextView totalp=new TextView(getBaseContext());
                    totalp.setText(""+data.get(i).getTotalPrice());

                    ll.addView(totalp);

                    userRfe.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TextView name = new TextView(getBaseContext());
                            name.setText((CharSequence) dataSnapshot.child(data.get(finalI).userUID).child("name").getValue().toString());
                            ll.addView(name);
                            TextView lastname = new TextView(getBaseContext());
                            lastname.setText((CharSequence) dataSnapshot.child(data.get(finalI).userUID).child("lname").getValue().toString());
                            ll.addView(lastname);
                            TextView phone=new TextView(getBaseContext());
                            phone.setText((CharSequence) dataSnapshot.child(data.get(finalI).userUID).child("phone").getValue().toString());
                            ll.addView(phone);
                            String bPhone= (String) dataSnapshot.child(data.get(finalI).userUID).child("phone").getValue().toString();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent callIntent =new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:"+bPhone));

                                    startActivity(callIntent);
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    EditText productName = new EditText(getBaseContext());
                    productName.setHint("product name");




//
//                    EditText kcal = new EditText(getBaseContext());
//                    kcal.setHint("Kcal");
//                    kcal.setText(""+data.get(i).getKcal());
//                    ll.addView(kcal);
//
//
//
//                    EditText priceEditT = new EditText(getBaseContext());
//                    priceEditT.setHint("price");
//                    priceEditT.setText(""+data.get(i).getPrice());
//                    ll.addView(priceEditT);
//
//
//
//
//
//                    EditText subType = new EditText(getBaseContext());
//                    subType.setHint("description");
//                    subType.setText(data.get(i).getSubType());
//                    ll.addView(subType);
//                    String TextDes =data.get(i).getSubType();
//
//
//
//                    EditText st = new EditText(getBaseContext());
//                    st.setHint("Stock");
//                    st.setText(""+data.get(i).getUnitsInStock());
//                    ll.addView(st,params);


                    // Create Button

                    // Give button an ID

                    //   btn.setOnClickListener(new View.OnClickListener() {
                    //     @Override
                    //   public void onClick(View v) {





                    //   });
                    // set the layoutParams on the button

                    //   btn.setLayoutParams(params);
                    //Add button to LinearLayout

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