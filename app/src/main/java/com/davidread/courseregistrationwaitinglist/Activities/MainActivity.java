package com.davidread.courseregistrationwaitinglist.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.davidread.courseregistrationwaitinglist.Models.WaitingListEntry;
import com.davidread.courseregistrationwaitinglist.R;
import com.davidread.courseregistrationwaitinglist.Utilities.DatabaseHelper;
import com.davidread.courseregistrationwaitinglist.Utilities.RecyclerTouchListener;
import com.davidread.courseregistrationwaitinglist.Utilities.WaitingListEntryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * This activity class represents a list of waiting list entry objects. A recycler view is used to
 * show the objects in a list. Two alert dialogs provide user interfaces for adding or updating
 * an object. Tapping on an object in the recycler view shows the update dialog. A floating action
 * button allows access to the add dialog.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerTouchListener.ClickListener {

    private DatabaseHelper db;
    private ArrayList<WaitingListEntry> waitingListEntries;
    private WaitingListEntryAdapter adapter;
    private TextView textViewEmpty;
    private long selectedWaitingListEntryId;
    private int selectedWaitingListEntryPosition;
    private TextInputEditText editTextFirstName, editTextLastName, editTextCourse;
    private AutoCompleteTextView autoCompleteTextViewPriority;
    private AlertDialog alertDialogAdd, alertDialogUpdate;

    /**
     * Called when the activity is initially created. It initializes several global objects, sets up
     * the custom action bar, sets up the recycler view, sets up the empty text view for the
     * recycler view, and sets up a click listener for the add floating action button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar toolbar;
        RecyclerView recyclerViewWaitingListEntries;
        FloatingActionButton floatingActionButtonAdd;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize objects.
        db = new DatabaseHelper(this);
        waitingListEntries = db.getAllWaitingListEntries();
        adapter = new WaitingListEntryAdapter(this, waitingListEntries);
        textViewEmpty = findViewById(R.id.text_view_empty);
        toolbar = findViewById(R.id.toolbar);
        recyclerViewWaitingListEntries = findViewById(R.id.recycler_view_waiting_list_entries);
        floatingActionButtonAdd = findViewById(R.id.button_add);

        // Setup custom action bar.
        setSupportActionBar(toolbar);

        // Setup recycler view.
        recyclerViewWaitingListEntries.setAdapter(adapter);
        recyclerViewWaitingListEntries.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewWaitingListEntries, this));
        recyclerViewWaitingListEntries.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerViewWaitingListEntries.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWaitingListEntries.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Change the visibility of the empty text view depending on whether there are results in the array list.
        if (waitingListEntries.size() > 0)
            textViewEmpty.setVisibility(View.GONE);
        else
            textViewEmpty.setVisibility(View.VISIBLE);

        // Set click listener for add button.
        floatingActionButtonAdd.setOnClickListener(this);
    }

    /**
     * Handles clicks for add floating action button and dialog buttons.
     */
    @Override
    public void onClick(View v) {

        int id = v.getId();

        // For add floating action button, show add entry alert dialog.
        if (id == R.id.button_add)
            showAddEntryDialog();

            // For add button in add entry alert dialog, insert a waiting list entry.
        else if (alertDialogAdd != null && alertDialogAdd.isShowing() && id == alertDialogAdd.getButton(AlertDialog.BUTTON_POSITIVE).getId())
            insertWaitingListEntry();

            // For update button in update entry alert dialog, update a waiting list entry.
        else if (alertDialogUpdate != null && alertDialogUpdate.isShowing() && id == alertDialogUpdate.getButton(AlertDialog.BUTTON_POSITIVE).getId())
            updateWaitingListEntry();

            // For delete button in update entry alert dialog, delete a waiting list entry.
        else if (alertDialogUpdate != null && alertDialogUpdate.isShowing() && id == alertDialogUpdate.getButton(AlertDialog.BUTTON_NEGATIVE).getId())
            deleteWaitingListEntry();
    }

    /**
     * Handles recycler view clicks. It shows an update entry alert dialog corresponding to the
     * selected waiting list entry. It also updates the global position of the selected waiting list
     * entry.
     */
    @Override
    public void onClick(View view, int position) {
        selectedWaitingListEntryPosition = position;
        showUpdateEntryDialog();
    }

    /**
     * Returns true if the text in the global edit texts are valid to be attributes for a new
     * waiting list entry object. If invalid text is detected, an error is set on the edit text from
     * where the error originated.
     */
    private boolean areEditTextsValid() {

        boolean isTextValid = true;
        String firstName, lastName, course;

        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        course = editTextCourse.getText().toString();

        if (firstName.isEmpty()) {
            editTextFirstName.setError(getString(R.string.toast_error_empty));
            isTextValid = false;
        }

        if (lastName.isEmpty()) {
            editTextLastName.setError(getString(R.string.toast_error_empty));
            isTextValid = false;
        }

        if (course.isEmpty()) {
            editTextCourse.setError(getString(R.string.toast_error_empty));
            isTextValid = false;
        }

        return isTextValid;
    }

    /**
     * Builds and shows an alert dialog for adding an entry.
     */
    private void showAddEntryDialog() {

        AlertDialog.Builder builder;
        View dialogView;
        ArrayAdapter<String> arrayAdapterPriority;

        // Initialize helper objects.
        builder = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_waiting_list_entry, null);
        arrayAdapterPriority = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.auto_complete_text_view_priority_selections));

        // Update edit texts and auto complete text view global references.
        editTextFirstName = dialogView.findViewById(R.id.edit_text_first_name);
        editTextLastName = dialogView.findViewById(R.id.edit_text_last_name);
        editTextCourse = dialogView.findViewById(R.id.edit_text_course);
        autoCompleteTextViewPriority = dialogView.findViewById(R.id.auto_complete_text_view_priority);

        // Setup auto complete text view with default selection and set selections.
        autoCompleteTextViewPriority.setText(getResources().getStringArray(R.array.auto_complete_text_view_priority_selections)[0]);
        autoCompleteTextViewPriority.setAdapter(arrayAdapterPriority);

        // Setup the add entry alert dialog.
        builder.setTitle(getString(R.string.dialog_add_entry_title));
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.dialog_add_entry_positive_button), null);
        builder.setNeutralButton(R.string.dialog_neutral_button, null);
        alertDialogAdd = builder.create();

        // Show the add entry alert dialog.
        alertDialogAdd.show();

        // Setup click listener for dialog button.
        alertDialogAdd.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
    }

    /**
     * Builds and shows an alert dialog for updating an entry.
     */
    private void showUpdateEntryDialog() {

        AlertDialog.Builder builder;
        View dialogView;
        WaitingListEntry selectedWaitingListEntry;
        ArrayAdapter<String> arrayAdapterPriority;

        // Initialize helper objects.
        builder = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_waiting_list_entry, null);
        selectedWaitingListEntry = waitingListEntries.get(selectedWaitingListEntryPosition);
        arrayAdapterPriority = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.auto_complete_text_view_priority_selections));

        // Update global id of selected waiting list entry.
        selectedWaitingListEntryId = selectedWaitingListEntry.getId();

        // Update edit texts and auto complete text view global references.
        editTextFirstName = dialogView.findViewById(R.id.edit_text_first_name);
        editTextLastName = dialogView.findViewById(R.id.edit_text_last_name);
        editTextCourse = dialogView.findViewById(R.id.edit_text_course);
        autoCompleteTextViewPriority = dialogView.findViewById(R.id.auto_complete_text_view_priority);

        // Populate edit texts and auto complete text view with the attributes of the selected object.
        editTextFirstName.setText(selectedWaitingListEntry.getFirstName());
        editTextLastName.setText(selectedWaitingListEntry.getLastName());
        editTextCourse.setText(selectedWaitingListEntry.getCourse());
        autoCompleteTextViewPriority.setText(selectedWaitingListEntry.getPriority());

        // Setup auto complete text view selections.
        autoCompleteTextViewPriority.setAdapter(arrayAdapterPriority);

        // Setup the update entry alert dialog.
        builder.setTitle(getString(R.string.dialog_update_entry_title));
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.dialog_update_entry_positive_button), null);
        builder.setNeutralButton(R.string.dialog_neutral_button, null);
        builder.setNegativeButton(R.string.dialog_update_entry_negative_button, null);
        alertDialogUpdate = builder.create();

        // Show the update entry alert dialog.
        alertDialogUpdate.show();

        // Setup click listeners for dialog buttons.
        alertDialogUpdate.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        alertDialogUpdate.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(this);
    }

    /**
     * Inserts a new waiting list entry object into the database. The attributes are specified in
     * globally referenced edit texts. Then, the recycler view is updated appropriately.
     */
    private void insertWaitingListEntry() {

        if (areEditTextsValid()) {

            long newId;
            WaitingListEntry insertedWaitingListEntry;

            // Hide add entry alert dialog.
            alertDialogAdd.hide();

            // Insert object into database.
            newId = db.insertWaitingListEntry(
                    editTextFirstName.getText().toString(),
                    editTextLastName.getText().toString(),
                    editTextCourse.getText().toString(),
                    autoCompleteTextViewPriority.getText().toString()
            );

            // Update recycler view.
            insertedWaitingListEntry = db.getWaitingListEntry(newId);
            waitingListEntries.add(0, insertedWaitingListEntry);
            adapter.notifyItemInserted(0);
            textViewEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * Updates a waiting list entry object in the database. The attributes are specified in globally
     * referenced edit texts. Then, the recycler view is updated appropriately.
     */
    private void updateWaitingListEntry() {

        if (areEditTextsValid()) {

            WaitingListEntry updatedWaitingListEntry;

            // Hide update entry alert dialog.
            alertDialogUpdate.hide();

            // Update object in database.
            db.updateWaitingListEntry(
                    selectedWaitingListEntryId,
                    editTextFirstName.getText().toString(),
                    editTextLastName.getText().toString(),
                    editTextCourse.getText().toString(),
                    autoCompleteTextViewPriority.getText().toString()
            );

            // Update recycler view.
            updatedWaitingListEntry = db.getWaitingListEntry(selectedWaitingListEntryId);
            waitingListEntries.set(selectedWaitingListEntryPosition, updatedWaitingListEntry);
            adapter.notifyItemChanged(selectedWaitingListEntryPosition);
        }
    }

    /**
     * Deletes a waiting list entry object from the database. The attributes are specified in
     * globally referenced edit texts. Then, the recycler view is updated appropriately.
     */
    private void deleteWaitingListEntry() {

        // Hide update entry alert dialog.
        alertDialogUpdate.hide();

        // Remove object in database.
        db.deleteWaitingListEntry(selectedWaitingListEntryId);

        // Update recycler view.
        waitingListEntries.remove(selectedWaitingListEntryPosition);
        adapter.notifyItemRemoved(selectedWaitingListEntryPosition);
        if (waitingListEntries.size() <= 0)
            textViewEmpty.setVisibility(View.VISIBLE);
    }
}