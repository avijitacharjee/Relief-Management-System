package com.avijit.rms.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avijit.rms.R;

import java.util.List;

public class SearchByNidRecyclerViewAdapter extends RecyclerView.Adapter<SearchByNidRecyclerViewAdapter.ViewHolder> {
    private List<String> names;
    private List<String> nids;
    private List<String> contacts;

    public SearchByNidRecyclerViewAdapter(List<String> names,List<String> nids,List<String> contacts)
    {
        this.names = names;
        this.nids = nids;
        this.contacts = contacts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_relief_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(names.get(position));
        holder.contactTextView.setText(contacts.get(position));
        holder.nidTextView.setText(nids.get(position));
        if(position%2==0)
        {
            holder.nameTextView.setBackgroundColor(Color.parseColor("#DFE6E9"));
            holder.contactTextView.setBackgroundColor(Color.parseColor("#DFE6E9"));
            holder.nidTextView.setBackgroundColor(Color.parseColor("#DFE6E9"));
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView,nidTextView,contactTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView=(TextView)itemView.findViewById(R.id.name_text_view);
            nidTextView=itemView.findViewById(R.id.nid_text_view);
            contactTextView = itemView.findViewById(R.id.contact_text_view);
        }
    }
}
