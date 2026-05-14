package com.carepulse.util;

// Helper methods used by JSPs to render sortable column headers.
public class TableSort {

    // Returns an arrow character if this column is the one currently sorted,
    // otherwise returns an empty string.
    public static String indicator(String columnKey, String currentSort, String currentDir) {
        if (columnKey == null || !columnKey.equals(currentSort)) {
            return "";
        }
        return "desc".equalsIgnoreCase(currentDir) ? " ↓" : " ↑";
    }

    // Returns the direction value that the column header link should send next.
    // Clicking the active column flips the direction. Any other column starts at ASC.
    public static String nextDir(String columnKey, String currentSort, String currentDir) {
        if (columnKey != null && columnKey.equals(currentSort)
                && "asc".equalsIgnoreCase(currentDir)) {
            return "desc";
        }
        return "asc";
    }

    // Returns the CSS class for a column header link. Adds the "sort-active"
    // class when this column is the one currently being sorted.
    public static String headerClass(String columnKey, String currentSort) {
        return columnKey != null && columnKey.equals(currentSort)
                ? "sort-link sort-active"
                : "sort-link";
    }
}
