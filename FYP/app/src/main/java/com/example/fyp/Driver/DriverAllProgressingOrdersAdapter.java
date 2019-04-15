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

import com.example.fyp.Driver.DriverRequests.requestadapter;
import com.example.fyp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverAllProgressingOrdersAdapter extends RecyclerView.Adapter<DriverAllProgressingOrdersAdapter.viewHolder> {

    ArrayList<DriverAllProgressingOrdersModel> data;
    private LayoutInflater mlayoutinflater;
    Context context;
    private requestadapter.ItemClickListener mylistener;
    String requestId;
    String customerId;

    public DriverAllProgressingOrdersAdapter(Context context,ArrayList<DriverAllProgressingOrdersModel> data){
        this.context=context;
        this.data=data;
        this.mlayoutinflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DriverAllProgressingOrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=mlayoutinflater.inflate(R.layout.driver_single_progressing_order,viewGroup,false);
        return (new viewHolder(view));     }

    @Override
    public void onBindViewHolder(@NonNull final DriverAllProgressingOrdersAdapter.viewHolder viewHolder, int i) {

        viewHolder.name.setText(data.get(i).getDriverName());
        viewHolder.mobile.setText(data.get(i).getDriverMobile());
        viewHolder.pickup.setText(data.get(i).getPickup());
        viewHolder.dropoff.setText(data.get(i).getDropoff());

        requestId= data.get(i).getRequestId();
        customerId=data.get(i).getCustomerId();


        String image=data.get(i).getImageView();
        if (!image.equals("default")) {
            Picasso.get().load(image).into(viewHolder.imageView);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewHolder.getAdapterPosition();
                Intent intent=new Intent(context, MapsActivity.class);
                intent.putExtra("requestId",data.get(pos).getRequestId());
                intent.putExtra("customerId",data.get(pos).getCustomerId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{




        TextView name;
        TextView mobile;
        CircleImageView imageView;
        TextView pickup;
        TextView dropoff;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.customerName);
            mobile=itemView.findViewById(R.id.customerMobile);
            imageView=itemView.findViewById(R.id.customerpic);
            pickup=itemView.findViewById(R.id.pickup);
            dropoff=itemView.findViewById(R.id.dropoff);
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
