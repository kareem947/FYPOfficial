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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewHolder> {

    LayoutInflater layoutInflater;
    ArrayList<HistoryModel> data;
    Context context;
    String requestId;

    public HistoryAdapter(Context context,ArrayList<HistoryModel> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.singlr_history,viewGroup,false);
        return (new viewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.viewHolder viewHolder, int i) {

        viewHolder.dName.setText(data.get(i).getDriverName());
        viewHolder.time.setText(data.get(i).getTime());
        viewHolder.customerName.setText(data.get(i).getCustomerName());
        requestId=data.get(i).getRequestId();



    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dName,customerName,time;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dName=itemView.findViewById(R.id.driverName);
            customerName=itemView.findViewById(R.id.customerName);
            time=itemView.findViewById(R.id.timeStamp);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "EveryThing is Goooood", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,ShowHistory.class);
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
