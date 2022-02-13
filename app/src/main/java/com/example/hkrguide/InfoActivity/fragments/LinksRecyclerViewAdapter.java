package com.example.hkrguide.InfoActivity.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hkrguide.InfoActivity.util.MenuEntry;
import com.example.hkrguide.R;

import java.util.List;

public class LinksRecyclerViewAdapter extends RecyclerView.Adapter<LinksRecyclerViewAdapter.ViewHolder> {

    private List<MenuEntry.Link> data;               // Data to display
    private LayoutInflater inflater;            // Layout Inflater
    private ItemClickListener clickListener;    // Click Listener

    // Constructor
    LinksRecyclerViewAdapter(Context context, List<MenuEntry.Link> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflates the row layout from XML when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row_info, parent, false);
        return new ViewHolder(view);
    }

    // Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuEntry.Link link = data.get(position);
        holder.textView.setText(link.DESCRIPTION);
    }

    // Get total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        // Set Click Listener
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_entry);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onLinkClick(view, getAdapterPosition());
        }
    }

    // Getting data at clicked row
    MenuEntry.Link getItem(int id) {
        return data.get(id);
    }

    // Set Click Listener
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // Parent Activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onLinkClick(View view, int position);
    }
}
