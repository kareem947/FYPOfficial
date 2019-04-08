package com.example.fyp.Driver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DriverRegister extends AppCompatActivity {


    private Button createAccount;
    private TextView alreadyHaveAccount;
    private EditText emailText, passText, nameText, phoneText, dlText, cnicText;
    private RadioButton smallPickp, largeTruck, mediumTruck;
    private String email = null, password = null, name = null, phone = null, dlNum = null, cnicNum = null;
    private int truckType = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser mcurrentuser;
    private DatabaseReference mdatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);



        createAccount = (Button) findViewById(R.id.dCreateAccount);
        alreadyHaveAccount = (TextView) findViewById(R.id.dAlreadyAccount);
        emailText = (EditText) findViewById(R.id.dRegisterEmail);
        passText = (EditText) findViewById(R.id.dRegisterPassword);
        nameText = (EditText) findViewById(R.id.dRegisterName);
        phoneText = (EditText) findViewById(R.id.dRegisterPhone);
        dlText = (EditText) findViewById(R.id.dRegisterLicense);
        cnicText = (EditText) findViewById(R.id.dRegisterCnic);

        smallPickp = (RadioButton) findViewById(R.id.dSmallPickup);
        largeTruck = (RadioButton) findViewById(R.id.dLargeTruck);
        mediumTruck = (RadioButton) findViewById(R.id.dMediumTruck);


        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DriverRegister.this, SignIn.class);
                startActivity(intent);
            }
        });



        createAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //pull all values from input
                email = emailText.getText().toString();
                password = passText.getText().toString();
                name = nameText.getText().toString();
                phone = phoneText.getText().toString();
                dlNum = dlText.getText().toString();
                cnicNum = cnicText.getText().toString();
                //check truck type
                if(smallPickp.isChecked())
                    truckType = 1;
                else if(mediumTruck.isChecked())
                    truckType = 2;
                else if(largeTruck.isChecked())
                    truckType = 3;


                //make sure there are no null values and that a truck has been selected
                if(email.equals("") || password.equals("") || name.equals("") || phone.equals("") || dlNum.equals("") || cnicNum.equals("") || truckType == 0){
                    Toast.makeText(DriverRegister.this, "Complete Registration Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    AddDriverAccount();
                }
            }
        });



    }

    public void AddDriverAccount(){

                            mdatabaseReference= FirebaseDatabase.getInstance().getReference().child("DriversRequests");
                            HashMap<String, String> user= new HashMap<>();
                            user.put("name", name);
                            user.put("license", dlNum);
                            user.put("cnic", cnicNum);
                            user.put("mobile", phone);
                            user.put("email", email);
                            user.put("password", password);
                            user.put("image","default");
                            user.put("truckType", Integer.toString(truckType));
                            mdatabaseReference.push().setValue(user);
                            Toast.makeText(DriverRegister.this, "You will be notified when you are registered it wont take much time", Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(DriverRegister.this,SignIn.class );
                            startActivity(intent);


    }
}
