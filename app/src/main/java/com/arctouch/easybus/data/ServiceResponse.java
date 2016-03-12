package com.arctouch.easybus.data;

import java.util.List;

/**
 * Abstract class that represents a response from the AppGlu REST API.
 *
 * @param <T> The type of the model returned by the response.
 */
public abstract class ServiceResponse<T> {

    private List<T> rows;
    private int rowsAffected;

    public List<T> getResults() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    public void setRowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

}
