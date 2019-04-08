package com.example.fyp.Customer.AddItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {

    ArrayList<ItemModel> mdata;
    private LayoutInflater mlayoutinflater;
    Context context;
    private ItemClickListener mylistener;
    int x;


    public ItemAdapter(Context context, ArrayList<ItemModel> data) {
        mlayoutinflater = LayoutInflater.from(context);
        this.mdata = data;
        this.context = context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mlayoutinflater.inflate(R.layout.sigle_item, viewGroup, false);
        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ItemAdapter.viewHolder viewHolder, int i) {
        viewHolder.text1.setText("Name:   " + mdata.get(i).getItemName());

        int x=i;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
              /*  Intent intent= new Intent(context, ShowFullRequest.class);
                context.startActivity(intent);*/
                Toast.makeText(context, "i am clicked"+pos, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

  /*  String getItem(int id) {
        return null;
    }*/

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text1;

        public viewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.itemName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mylistener.onItemClick(getAdapterPosition());
           // Toast.makeText(context, "how r u", Toast.LENGTH_SHORT).show();


        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mylistener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

}