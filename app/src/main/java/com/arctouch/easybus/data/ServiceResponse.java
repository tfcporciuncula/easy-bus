package com.arctouch.easybus.data;

import com.google.common.base.Objects;

import java.util.List;

/**
 * Generic and abstract class that represents a response from the AppGlu REST API.
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

    @Override
    public int hashCode() {
        return Objects.hashCode(rows, rowsAffected);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ServiceResponse) {
            ServiceResponse that = (ServiceResponse) o;
            return Objects.equal(this.rows, that.rows) &&
                    Objects.equal(this.rowsAffected, that.rowsAffected);
        }
        return false;
    }

}
