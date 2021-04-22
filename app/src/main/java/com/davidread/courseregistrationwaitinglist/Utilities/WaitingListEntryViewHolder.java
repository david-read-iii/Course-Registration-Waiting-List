package com.davidread.courseregistrationwaitinglist.Utilities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidread.courseregistrationwaitinglist.R;

/**
 * This class represents a holder for the views used in a single row of the recycler view.
 */
public class WaitingListEntryViewHolder extends RecyclerView.ViewHolder {

    // Class objects.
    private TextView textViewFullName;
    private TextView textViewCourse;
    private TextView textViewPriority;

    /**
     * Constructs a waiting list entry view holder.
     */
    public WaitingListEntryViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewFullName = itemView.findViewById(R.id.text_view_full_name);
        textViewCourse = itemView.findViewById(R.id.text_view_course);
        textViewPriority = itemView.findViewById(R.id.text_view_priority);
    }

    public TextView getTextViewFullName() {
        return textViewFullName;
    }

    public TextView getTextViewCourse() {
        return textViewCourse;
    }

    public TextView getTextViewPriority() {
        return textViewPriority;
    }
}
