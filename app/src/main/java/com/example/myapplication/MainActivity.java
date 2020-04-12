package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText prodName,prodSize,prodQuan;
        Button btn= (Button)findViewById(R.id.Submit);

        //2nd APP
        final TextView txtView=(TextView)findViewById(R.id.showData);
        Button fetch=(Button)findViewById(R.id.getData);
        final ArrayList<String> myArray = new ArrayList<>();
        ListView myList= (ListView) findViewById(R.id.myList);
        final ArrayAdapter<String> myAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,myArray);
        myList.setAdapter(myAdapter);
        //1st App start

        prodName=(EditText)findViewById(R.id.productName);
        prodSize=(EditText)findViewById(R.id.productSize);
        prodQuan=(EditText)findViewById(R.id.productQuantity);

//        btn.setOnClickListener();
        final Data data;
        data = new Data();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v)
            {

                int quantity=Integer.parseInt(prodQuan.getText().toString().trim());
                int size=Integer.parseInt(prodSize.getText().toString().trim());
//                Log.i("STRING",prodName.toString().trim() +"");
                data.setProductName(prodName.getText().toString().trim());
                data.setProductQuantity(quantity);
                data.setProductSize(size);
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                myRef.child("Customer").push().setValue(data);
                Toast.makeText(MainActivity.this,"Submitted Successfully",Toast.LENGTH_LONG).show();

            }

        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mydb = FirebaseDatabase.getInstance().getReference().child("Test");
                mydb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String getData= dataSnapshot.getValue().toString();
                        txtView.setText(getData);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
//                mydb.push().setValue("New Value");
                Toast.makeText(MainActivity.this,"Fetched Data",Toast.LENGTH_LONG).show();


                DatabaseReference mydb1=FirebaseDatabase.getInstance().getReference().child("Customer");
                mydb1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String myChild=dataSnapshot.child("productQuantity").getValue().toString();
//                        String myChild1=dataSnapshot.getKey();
                        myArray.add(myChild);
                        myAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(MainActivity.this,"Fetched",Toast.LENGTH_LONG).show();

            }
        });



    }
}
