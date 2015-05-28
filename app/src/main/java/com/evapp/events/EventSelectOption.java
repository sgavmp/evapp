package com.evapp.events;

import com.evapp.ui.util.OpcionesEvaluacion;

/**
 * Created by Sergio on 03/02/2015.
 */
public class EventSelectOption {
    private OpcionesEvaluacion option;

    public EventSelectOption(OpcionesEvaluacion option) {
        this.option = option;
    }

    public OpcionesEvaluacion getOption() {
        return option;
    }
}
