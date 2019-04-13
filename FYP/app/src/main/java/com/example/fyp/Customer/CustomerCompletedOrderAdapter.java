package com.example.fyp.Customer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerCompletedOrderAdapter extends RecyclerView.Adapter<CustomerCompletedOrderAdapter.viewHolder> {

    LayoutInflater layoutInflater;
    ArrayList<CustomerCompletedOrderModel> data;
    Context context;
    String requestId;

    public CustomerCompletedOrderAdapter(Context context,ArrayList<CustomerCompletedOrderModel> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerCompletedOrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.customer_single_completed_orders,viewGroup,false);
        return (new viewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerCompletedOrderAdapter.viewHolder viewHolder, int i) {

        viewHolder.dName.setText(data.get(i).getDriverName());
        viewHolder.orderName.setText(data.get(i).getOrderName());
        viewHolder.time.setText(data.get(i).getTime());
        String image = data.get(i).getImageView();
        requestId=data.get(i).getRequestId();

        if (!image.equals("default")){
            Picasso.get().load(image).into(viewHolder.imageView);

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dName,orderName,time;
        CircleImageView imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dName=itemView.findViewById(R.id.driverName);
            orderName=itemView.findViewById(R.id.orderName);
            time=itemView.findViewById(R.id.timeStamp);
            imageView=itemView.findViewById(R.id.driverpic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "EveryThing is Goooood", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,ShowCustomerCompletedOrder.class);
                    intent.putExtra("requestId",requestId);
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
