package com.example.fyp.Customer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;

import java.util.ArrayList;

public class CustomerPendingOrderAdapter extends RecyclerView.Adapter<CustomerPendingOrderAdapter.viewHolder> {


    Context context;
    ArrayList<CustomerPendingOrderModel> data;
    LayoutInflater layoutInflater;
    public CustomerPendingOrderAdapter(Context context, ArrayList<CustomerPendingOrderModel> data) {
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomerPendingOrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.single_pending_order,viewGroup,false);
        return (new viewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerPendingOrderAdapter.viewHolder viewHolder, int i) {

        viewHolder.mobile.setText(data.get(i).getMobile());
        viewHolder.name.setText(data.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView mobile;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.customerName);
            mobile=itemView.findViewById(R.id.customerMobile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Everything is Good", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
