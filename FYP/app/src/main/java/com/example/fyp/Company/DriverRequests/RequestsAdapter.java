package com.example.fyp.Company.DriverRequests;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.fyp.Company.ShowDriverDetails;
import com.example.fyp.R;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.viewHolder> {

    ArrayList<RequestModel> mdata;
    private LayoutInflater mlayoutinflater;
    Context context;
    private ItemClickListener mylistener;
    int x;

    public RequestsAdapter(Context context,ArrayList<RequestModel> mdata){
        mlayoutinflater=LayoutInflater.from(context);
        this.mdata=mdata;
        this.context=context;
    }
    @NonNull
    @Override
    public RequestsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mlayoutinflater.inflate(R.layout.company_single_driver, viewGroup, false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final RequestsAdapter.viewHolder viewHolder, int i) {

        viewHolder.text1.setText(mdata.get(i).getName());
        viewHolder.text2.setText(mdata.get(i).getMobile());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewHolder.getAdapterPosition();
                Intent intent=new Intent(context, ShowDriverDetails.class);
                intent.putExtra("pushId",mdata.get(pos).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView text1,text2;

        public viewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.companydriverName);
            text2 = itemView.findViewById(R.id.companyDrivermobile);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mylistener.onItemClick(getAdapterPosition());
        }
    }

    void setClickListener(RequestsAdapter.ItemClickListener itemClickListener) {
        this.mylistener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
