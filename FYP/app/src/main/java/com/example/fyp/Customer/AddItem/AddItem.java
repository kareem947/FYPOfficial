package com.example.fyp.Customer.AddItem;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fyp.Customer.MakeOrder;
import com.example.fyp.R;

import java.io.Serializable;
import java.util.ArrayList;


public class AddItem extends AppCompatActivity implements Serializable {

    EditText itemName,quantity,desc;
    Button addItem,done;
    TextView addItemText;
    Uri imageUri;
    private static final int GALLERY_PICK = 1;
    ImageButton addItemPic;
    ArrayList<ItemModel> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addItemText=findViewById(R.id.addItempicText);
        addItemPic=findViewById(R.id.addItemPic);
        itemName=findViewById(R.id.addItemName);
        desc=findViewById(R.id.addItemDesc);
        quantity=findViewById(R.id.addItemQuantity);
        addItem =findViewById(R.id.addItem);
       // addMore =findViewById(R.id.moreItem);
        done =findViewById(R.id.doneItem);

        addItemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Choose Image"), GALLERY_PICK);             }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = itemName.getText().toString();
                String miqdar = quantity.getText().toString();
                String description = desc.getText().toString();
                if (name.equals("") || miqdar.equals("") || description.equals("") || imageUri.equals("")) {
                    Toast.makeText(AddItem.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    list.add(new ItemModel(name, miqdar, description));
                    Toast.makeText(AddItem.this, "Your Item has been added you can add more items if you want", Toast.LENGTH_LONG).show();
                    itemName.setText("");
                    quantity.setText("");
                    desc.setText("");
                    addItemText.setText("");
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(AddItem.this, MakeOrder.class);

                bundle.putSerializable("newPlaylist", list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Log.d("ProfilePic    ",data.getData().toString());
            imageUri = data.getData();

        }
    }
}



