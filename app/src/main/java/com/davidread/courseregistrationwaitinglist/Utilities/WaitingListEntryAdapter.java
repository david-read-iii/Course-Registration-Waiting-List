package com.davidread.courseregistrationwaitinglist.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidread.courseregistrationwaitinglist.Models.WaitingListEntry;
import com.davidread.courseregistrationwaitinglist.R;

import java.util.ArrayList;

/**
 * This class defines how waiting list entry objects from an array list should be adapted to be
 * displayed within a recycler view.
 */
public class WaitingListEntryAdapter extends RecyclerView.Adapter<WaitingListEntryViewHolder> {

    // Class objects.
    private Context context;
    private ArrayList<WaitingListEntry> waitingListEntries;

    /**
     * Constructs a waiting list entry adapter.
     */
    public WaitingListEntryAdapter(Context context, ArrayList<WaitingListEntry> waitingListEntries) {
        this.context = context;
        this.waitingListEntries = waitingListEntries;
    }

    /**
     * Called for each view holder to initialize them. It inflates the layout of a single row of the
     * recycler view.
     */
    @NonNull
    @Override
    public WaitingListEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_waiting_list_entry, parent, false);
        return new WaitingListEntryViewHolder(itemView);
    }

    /**
     * Called for each view holder when they are bound to the adapter. It passes data about the
     * corresponding waiting list entry object to the view holder.
     */
    @Override
    public void onBindViewHolder(@NonNull WaitingListEntryViewHolder holder, int position) {
        WaitingListEntry waitingListEntry = waitingListEntries.get(position);
        holder.getTextViewFullName().setText(context.getString(R.string.format_full_name, waitingListEntry.getFirstName(), waitingListEntry.getLastName()));
        holder.getTextViewCourse().setText(waitingListEntry.getCourse());
        holder.getTextViewPriority().setText(waitingListEntry.getPriority());
    }

    /**
     * Returns the size of the waiting list entries array list.
     */
    @Override
    public int getItemCount() {
        return waitingListEntries.size();
    }
}
