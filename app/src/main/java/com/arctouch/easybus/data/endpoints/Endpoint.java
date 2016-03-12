package com.arctouch.easybus.data.endpoints;

import android.content.Context;

import java.util.List;

/**
 * Simple contract for any REST endpoint within the app.
 *
 * @param <T> The type returned by the endpoint.
 * @param <P> The type of the parameter needed by the endpoint.
 */
public interface Endpoint<T, P> {

    List<T> call(Context context, P parameter);

}
