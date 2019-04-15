package com.example.fyp.Driver;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Customer.CustomerCompletedOrderAdapter;
import com.example.fyp.Customer.CustomerCompletedOrderModel;
import com.example.fyp.Customer.ShowCustomerCompletedOrder;
import com.example.fyp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverCompletedOrderAdapter  extends RecyclerView.Adapter<DriverCompletedOrderAdapter.viewHolder>{




    LayoutInflater layoutInflater;
    ArrayList<DriverCompletedOrderModel> data;
    Context context;
    String requestId;

    public DriverCompletedOrderAdapter(Context context,ArrayList<DriverCompletedOrderModel> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public DriverCompletedOrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.single_driver_comppleted_order,viewGroup,false);
        return (new DriverCompletedOrderAdapter.viewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final DriverCompletedOrderAdapter.viewHolder viewHolder, int i) {

        viewHolder.cName.setText(data.get(i).getCustomerName());
        viewHolder.cMobile.setText(data.get(i).getCustomerMobile());
        viewHolder.time.setText(data.get(i).getTime());
        String image = data.get(i).getImageView();
        requestId=data.get(i).getRequestId();

        if (!image.equals("default")){
            Picasso.get().load(image).into(viewHolder.imageView);

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                Toast.makeText(context, "EveryThing is Goooood", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, ShowDriverCompletedOrder.class);
                intent.putExtra("requestId",data.get(pos).getRequestId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cName,cMobile,time;
        CircleImageView imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cName=itemView.findViewById(R.id.customerName);
            cMobile=itemView.findViewById(R.id.customerMobile);
            time=itemView.findViewById(R.id.timeStamp);
            imageView=itemView.findViewById(R.id.customerPic);

         itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }








}
