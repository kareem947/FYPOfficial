package com.example.fyp.Customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CustomerRegister extends AppCompatActivity {


    private EditText newEmail;
    private EditText newPassword;
    private EditText newName;
    private EditText newPhone;
    private EditText newCnic;
    private Button createAccount;
    private String email = null, password = null, name = null, phone = null, cnic=null;
    private TextView alreadyHaveAccount;

    private FirebaseAuth mAuth;
    private FirebaseUser mcurrentuser;
    private DatabaseReference mdatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);


        alreadyHaveAccount = (TextView) findViewById(R.id.cAlreadyAccount);

        createAccount = (Button) findViewById(R.id.cCreateAccount);
        newEmail = (EditText) findViewById(R.id.cRegisterEmail);
        newPassword = (EditText) findViewById(R.id.cRegisterPassword);
        newName = (EditText) findViewById(R.id.cRegisterName);
        newCnic = (EditText) findViewById(R.id.cRegisterCnic);
        newPhone = (EditText) findViewById(R.id.cRegisterPhone);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CustomerRegister.this, SignIn.class);
                startActivity(intent);
            }
        });


        createAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //pull all values from input
                email = newEmail.getText().toString();
                password = newPassword.getText().toString();
                name = newName.getText().toString();
                phone = newPhone.getText().toString();
                cnic = newCnic.getText().toString();




                //make sure there are no null values and that a truck has been selected
                if(email.equals("") || password.equals("") || name.equals("") || phone.equals("") || cnic.equals("")){
                    Toast.makeText(CustomerRegister.this, "Complete Registration Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    AddCustomerAccount();
                }
            }
        });
    }

    public void AddCustomerAccount(){


        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            mcurrentuser=FirebaseAuth.getInstance().getCurrentUser();
                            String uid= mcurrentuser.getUid();
                            mdatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid);
                            HashMap<String, String> user= new HashMap<>();
                            user.put("name", name);
                            user.put("mobile", phone);
                            user.put("email", email);
                            user.put("cnic",cnic);
                            user.put("image","default");
                            user.put("password", password);

                            mdatabaseReference.setValue(user);
                            Toast.makeText(CustomerRegister.this, "Success", Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(CustomerRegister.this,SignIn.class );
                            startActivity(intent);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CustomerRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });



    }

}
