package com.example.fyp.Customer;

import android.content.Context;
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

public class CustomerAcceptedOrderAdapter extends RecyclerView.Adapter<CustomerAcceptedOrderAdapter.viewHolder> {

    ArrayList<CustomerAcceptedOrderModel> data;
    private LayoutInflater mlayoutinflater;
    Context context;
    private requestadapter.ItemClickListener mylistener;
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
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        viewHolder.name.setText(data.get(i).getDriverName());
        viewHolder.mobile.setText(data.get(i).getDriverMobile());

        String image=data.get(i).getImageView();
        if (!image.equals("default")) {
            Picasso.get().load(image).into(viewHolder.imageView);
        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView mobile;
        CircleImageView imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.driverName);
            mobile=itemView.findViewById(R.id.driverMobile);
            imageView=itemView.findViewById(R.id.driverpic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "EveryThing is Good", Toast.LENGTH_SHORT).show();
                }
            });


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
