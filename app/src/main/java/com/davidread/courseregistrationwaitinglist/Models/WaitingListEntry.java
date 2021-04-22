package com.davidread.courseregistrationwaitinglist.Models;

/**
 * This model class represents a single entry in the course registration waiting list. It has
 * attributes for a unique identifier, first name, last name, course, and priority.
 */
public class WaitingListEntry {

    // SQLite constants.
    public static final String TABLE_NAME = "waiting_list_entries";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_PRIORITY = "priority";

    // Model variables.
    private long id;
    private String firstName;
    private String lastName;
    private String course;
    private String priority;

    /**
     * Constructs a waiting list entry with null attributes.
     */
    public WaitingListEntry() {
    }

    /**
     * Constructs a waiting list entry with the specified attributes.
     */
    public WaitingListEntry(long id, String firstName, String lastName, String course, String priority) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
