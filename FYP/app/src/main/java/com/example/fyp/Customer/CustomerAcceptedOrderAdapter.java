package com.example.fyp.Customer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Driver.DriverRequests.requestadapter;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAcceptedOrderAdapter extends RecyclerView.Adapter<CustomerAcceptedOrderAdapter.viewHolder> {

    ArrayList<CustomerAcceptedOrderModel> data;
    private LayoutInflater mlayoutinflater;
    Context context;
    private requestadapter.ItemClickListener mylistener;
    String requestId,driverId;
    int x;

    public CustomerAcceptedOrderAdapter(Context context,ArrayList<CustomerAcceptedOrderModel> data){
        this.context=context;
        this.data=data;
        this.mlayoutinflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=mlayoutinflater.inflate(R.layout.customer_single_accepted_order,viewGroup,false);
        return (new viewHolder(view));    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, int i) {

        viewHolder.name.setText(data.get(i).getDriverName());
        viewHolder.mobile.setText(data.get(i).getDriverMobile());

        requestId= data.get(i).getRequestId();
        driverId=data.get(i).getDriverId();

        String image=data.get(i).getImageView();
        if (!image.equals("default")) {
            Picasso.get().load(image).into(viewHolder.imageView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewHolder.getAdapterPosition();
                Intent intent=new Intent(context, CustomerMaps.class);
                intent.putExtra("requestId",data.get(pos).getRequestId());
                intent.putExtra("driverId",data.get(pos).getDriverId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView mobile;
        TextView orderName;
        CircleImageView imageView;
        Button cancel;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.driverName);
            mobile=itemView.findViewById(R.id.driverMobile);
            imageView=itemView.findViewById(R.id.driverpic);
            orderName=itemView.findViewById(R.id.orderName);
            cancel=itemView.findViewById(R.id.cancelOrder);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference cancelOrder=FirebaseDatabase.getInstance().getReference("WorkingRequests").child(requestId);
                    cancelOrder.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            notifyDataSetChanged();
                        }
                    });
                }
            });

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            mylistener.onItemClick(getAdapterPosition());


        }


    }


    void setClickListener(requestadapter.ItemClickListener itemClickListener) {
        this.mylistener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

}
