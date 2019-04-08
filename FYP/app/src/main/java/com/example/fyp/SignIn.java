package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Company.CompanyMainPage;
import com.example.fyp.Customer.CustomerHomeActivity;
import com.example.fyp.Customer.CustomerRegister;
import com.example.fyp.Driver.DriverHomeActivity;
import com.example.fyp.Driver.DriverRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private Button loginButton;
    private TextView createAccount, comapny;
    private Switch mySwitch;
    private EditText userText, passText;
    private String user = null, pass = null, userId, driverId;
    private String userType;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabaseReference;
    FirebaseUser mcurrentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginButton = (Button) findViewById(R.id.addItem);
        mySwitch = (Switch) findViewById(R.id.switch2);
        userText = (EditText) findViewById(R.id.userName);
        passText = (EditText) findViewById(R.id.password);
        comapny = findViewById(R.id.admin);
        createAccount = (TextView) findViewById(R.id.gotoRegister);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mySwitch.isChecked()){
                    Intent intent = new Intent(getApplicationContext(), DriverRegister.class);
                    startActivity(intent);
                }
                else {
                    //Actual code///////////////////////////////////////////////////////////////////
                    Intent intent = new Intent(getApplicationContext(), CustomerRegister.class);
                    startActivity(intent);
                }

            }
        });




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pull all values from input
                user = userText.getText().toString();
                pass = passText.getText().toString();

                if(mySwitch.isChecked()){
                    //Make sure there are no blank entries
                    if(user.equals("") || pass.equals("")){
                        Toast.makeText(SignIn.this, "Complete Registration Fields", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        userType="Drivers";
                        signInUser();
                    }
                }
                else{
                    //Make sure there are no blank entries
                    if(user.equals("") || pass.equals("")){
                        Toast.makeText(SignIn.this, "Complete Registration Fields", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        userType="Customers";
                        signInUser();
                    }
                }
            }
        });
    }

    public void signInUser(){


        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                         // /*  dialog.dismiss();
                            mcurrentuser= FirebaseAuth.getInstance().getCurrentUser();
                            String uid= mcurrentuser.getUid();
                            mdatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userType).child(uid);
                            Log.d("IdUser",uid);
                            mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        Log.d("IdUser1",dataSnapshot.child("name").getValue().toString());
                                        Toast.makeText(SignIn.this, "You are logged in as "+userType, Toast.LENGTH_SHORT).show();

                                        if (userType=="Drivers"){
                                            Intent intent= new Intent(SignIn.this, DriverHomeActivity.class );
                                            startActivity(intent);
                                        }
                                        else {
                                            Intent intent= new Intent(SignIn.this, CustomerHomeActivity.class );
                                            startActivity(intent);
                                        }
                                    }
                                    else {
                                        mAuth.signOut();
                                        Toast.makeText(SignIn.this, "Signed Out", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Toast.makeText(SignIn.this, "Sorry Kuch Ghalat Hy", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }
    public void goForCompany(View view){
        Intent intent=new Intent(SignIn.this, CompanyMainPage.class);
        startActivity(intent);
    }
}
