package com.evapp.events;

import com.evapp.ui.util.OpcionesComo;

/**
 * Created by Sergio on 04/02/2015.
 */
public class EventEvaluar {

    private OpcionesComo opcion;

    public EventEvaluar(OpcionesComo opcion){
        this.opcion = opcion;
    }

    public OpcionesComo getOpcion() {
        return opcion;
    }
}
