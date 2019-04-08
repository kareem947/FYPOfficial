package com.example.fyp.Driver.DriverRequests;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Driver.ShowFullRequest;
import com.example.fyp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class requestadapter extends RecyclerView.Adapter<requestadapter.viewHolder> {

    ArrayList<Requestmodel> mdata;
    private LayoutInflater mlayoutinflater;
    Context context;
    private ItemClickListener mylistener;
    String customerId;
    String requestId;
    int x;


    public requestadapter(Context context, ArrayList<Requestmodel> data){
        mlayoutinflater = LayoutInflater.from(context);
        this.mdata=data;
        this.context=context;
    }



    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=mlayoutinflater.inflate(R.layout.driver_request_layout,viewGroup,false);
        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(final requestadapter.viewHolder viewHolder, int i) {
        viewHolder.name.setText(""+mdata.get(i).getName());
        viewHolder.pick.setText(""+mdata.get(i).getPick());
        viewHolder.drop.setText(""+mdata.get(i).getDrop());
        viewHolder.paymentMethod.setText("Payment : "+mdata.get(i).getPayment());
        customerId=mdata.get(i).getId();
        requestId=mdata.get(i).getRequest();
        String image=mdata.get(i).getImageView();
            if (!image.equals("default")) {
                Picasso.get().load(image).into(viewHolder.imageView);
            }


        int x=i;

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    String getItem(int id) {
        return null;
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView pick;
        TextView drop;
        TextView paymentMethod;
        ImageView imageView;

        public viewHolder( View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.requestName);
            pick=itemView.findViewById(R.id.requestPick);
            drop=itemView.findViewById(R.id.requestDrop);
            paymentMethod=itemView.findViewById(R.id.payment);
            imageView=itemView.findViewById(R.id.requestImage);
            //itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ShowFullRequest.class);
                    intent.putExtra("customerId",customerId);
                    intent.putExtra("requestId",requestId);
                    context.startActivity(intent);
                        }
            });
        }


        @Override
        public void onClick(View v) {
            mylistener.onItemClick(getAdapterPosition());


        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mylistener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
