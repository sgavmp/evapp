package com.evapp.events;

import android.net.Uri;

/**
 * Created by Sergio on 04/02/2015.
 */
public class EventEnviar {
    Uri uri;
    String text;

    public EventEnviar(String text) {
        this.text = text;
    }

    public EventEnviar(Uri uri,String text) {
        this.uri = uri;
        this.text = text;
    }

    public Uri getUri() {
        return uri;
    }

    public String getText() {
        return text;
    }
}
