package com.carepulse.util;

// Helpers used by JSPs to render sortable column headers.
// All methods are pure and safe to call from <%= ... %>.
public class TableSort {

    // The character to render next to the active sort column.
    // Returns "" when this column is not the active one.
    public static String indicator(String columnKey, String currentSort, String currentDir) {
        if (columnKey == null || !columnKey.equals(currentSort)) {
            return "";
        }
        return "desc".equalsIgnoreCase(currentDir) ? " ↓" : " ↑";
    }

    // The "dir" value to put in the next sort link for this column header.
    // Clicking the active column toggles direction; clicking any other column
    // starts at ASC.
    public static String nextDir(String columnKey, String currentSort, String currentDir) {
        if (columnKey != null && columnKey.equals(currentSort)
                && "asc".equalsIgnoreCase(currentDir)) {
            return "desc";
        }
        return "asc";
    }

    // CSS class for column header anchor - "active" when this column is the
    // currently sorted one. Lets JSPs style the active header subtly.
    public static String headerClass(String columnKey, String currentSort) {
        return columnKey != null && columnKey.equals(currentSort)
                ? "sort-link sort-active"
                : "sort-link";
    }
}
