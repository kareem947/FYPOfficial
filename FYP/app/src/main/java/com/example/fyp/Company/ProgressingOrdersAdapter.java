package com.example.fyp.Company;

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

public class ProgressingOrdersAdapter extends RecyclerView.Adapter<ProgressingOrdersAdapter.viewHolder> {

    LayoutInflater layoutInflater;
    ArrayList<ProgressingOrdersModel> data;
    Context context;
    String requestId;

    public ProgressingOrdersAdapter(Context context,ArrayList<ProgressingOrdersModel> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgressingOrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.single_company_progressing_order,viewGroup,false);
        return (new viewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressingOrdersAdapter.viewHolder viewHolder, int i) {

        viewHolder.dName.setText(data.get(i).getDriverName());
        viewHolder.orderName.setText(data.get(i).getOrderName());
        viewHolder.customerName.setText(data.get(i).getCustomerName());
        requestId=data.get(i).getRequestId();



    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dName,customerName,orderName;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dName=itemView.findViewById(R.id.driverName);
            customerName=itemView.findViewById(R.id.customerName);
            orderName=itemView.findViewById(R.id.orderName);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "EveryThing is Goooood", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,ShowProgressingOrders.class);
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
