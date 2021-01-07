package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class store extends AppCompatActivity {

    Button gotoCart;
    ArrayList<String> productUID=new ArrayList<String>();
    ArrayList<Double> prices=new ArrayList<Double>();
    ArrayList<TextView> tvIndexes=new ArrayList<>();
    OrderUpdate currOrderUpdate =new OrderUpdate("1");
    DatabaseReference refUser,refOrders;
    FirebaseAuth fb;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView inStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);

        //add all products from "Store" on Firebase to an array list
        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("products");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String,storeProduct> td= (Map <String,storeProduct>) dataSnapshot.getValue();
                ArrayList<storeProduct>data=new ArrayList<storeProduct>();
                storeProduct product;
                for (Entry <String, storeProduct> entry : td.entrySet()) {
                    HashMap nana=entry.getValue();
                    Long LunitsInStock= (Long) nana.get("inStock");
                    int nitsInStock = LunitsInStock != null ? LunitsInStock.intValue() : -1;
                    String Lkcal = (String) nana.get("Kcal").toString();
                    Double kcal = Double.parseDouble(Lkcal);
                    Long Lprice=(Long) nana.get("price");
                    double price= Lprice != null ? Lprice.doubleValue() : -1;
                    String name= (String) nana.get("name");
                    String subType= (String) nana.get("description");
                    productUID.add((String) entry.getKey());
                    prices.add(price);
                    product=new storeProduct(name,kcal,price,subType,nitsInStock);
                    data.add(product);

                }
                int productsAmount=data.size();
                final LinearLayout lm = (LinearLayout) findViewById(R.id.linearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 50);

                ArrayList<Button> buttons = new ArrayList<Button>();
                for(int i=0;i<productsAmount;i++)
                {
                    LinearLayout ll = new LinearLayout(getBaseContext());
                    ll.setOrientation(LinearLayout.VERTICAL);

                    TextView productName = new TextView(getBaseContext());
                    productName.setText("Product name: "+data.get(i).getName());
                    ll.addView(productName);

                    TextView kcal = new TextView(getBaseContext());
                    kcal.setText("kCal: "+data.get(i).getKcal());
                    ll.addView(kcal);

                    TextView price = new TextView(getBaseContext());
                    price.setText("Price is: "+data.get(i).getPrice() + " ILS");
                    ll.addView(price);

                    TextView subType = new TextView(getBaseContext());
                    subType.setText("Description: "+data.get(i).getSubType());
                    ll.addView(subType);

                    inStock=new TextView(getBaseContext());
                    inStock.setText("Currently In Stock: "+data.get(i).UnitsInStock);
                    ll.addView(inStock,params);
                    tvIndexes.add(inStock);
                    // Create Button
                    final Button btn = new Button(getBaseContext());
                    // Give button an ID
                    btn.setId(i);
                    btn.setText("Add To Cart");
                    // set the layoutParams on the button

                    btn.setLayoutParams(params);
                    //Add button to LinearLayout
                    ll.addView(btn);
                    //Add button to LinearLayout defined in XML

                    lm.addView(ll);
                    buttons.add(btn);
                    if(data.get(i).UnitsInStock==0)
                    {
                        buttons.get(i).setEnabled(false);
                    }
                    buttons.get(i).setOnClickListener(handleOnClick(buttons.get(i),data, currOrderUpdate,tvIndexes));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        productsRefernce.addValueEventListener(postListener);
        gotoCart=(Button) findViewById(R.id.btnCart);
        setOnClick(gotoCart, currOrderUpdate);

    }
    View.OnClickListener handleOnClick(final Button currBtn, ArrayList<storeProduct>data, OrderUpdate currOrderUpdate, ArrayList<TextView> tvIndexes) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String productName=productUID.get(currBtn.getId());
                double price=prices.get(currBtn.getId());
                int stockQuantity=data.get(currBtn.getId()).UnitsInStock;
                    if(stockQuantity==1)
                    {
                        Toast.makeText(getBaseContext(),"Last unit in Stock",Toast.LENGTH_LONG).show();
                        currBtn.setEnabled(false);
                        currOrderUpdate.FillProductInArrayList(productName,price);
                        int OrdersNumber= currOrderUpdate.getItemQuantity(productName);
                        currBtn.setText("Add To Cart"+ "(" +OrdersNumber+ ")");
                        data.get(currBtn.getId()).setUnitsInStock(stockQuantity-1);
                        tvIndexes.get(currBtn.getId()).setText("Currently In Stock: "+data.get(currBtn.getId()).UnitsInStock);
                    }
                    if(stockQuantity<=0)
                    {
                        currBtn.setEnabled(false);
                    }
                    else if(stockQuantity>1)
                    {
                        currOrderUpdate.FillProductInArrayList(productName,price);
                        int OrdersNumber= currOrderUpdate.getItemQuantity(productName);
                        currBtn.setText("Add To Cart"+ "(" +OrdersNumber+ ")");
                        data.get(currBtn.getId()).setUnitsInStock(stockQuantity-1);
                        tvIndexes.get(currBtn.getId()).setText("Currently In Stock: "+data.get(currBtn.getId()).UnitsInStock);
                    }
            }
        };
    }

    public void setOnClick(final Button btn, final OrderUpdate currOrderUpdate){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currOrderUpdate.getItemQuantity().size()>=1)
                {
                    refUser= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid()).child("Orders").push();
                    String OrderUID=refUser.getKey();
                    refUser.setValue("true");
                    String orderUID = refUser.getKey();
                    Intent goToCart=new Intent(store.this,cart_activity.class);
                    goToCart.putExtra("Unique Order ID",orderUID);
                    refOrders=FirebaseDatabase.getInstance().getReference("Orders").child(orderUID);
                    currOrderUpdate.setUserUID(currentFirebaseUser.getUid());
                    currOrderUpdate.setTotalPrice(0);
                    refOrders.setValue(currOrderUpdate);
                    startActivity(goToCart);
                }
                else{
                    Toast.makeText(getBaseContext(),"No Orders were made. \nPlease add items to the cart",Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}