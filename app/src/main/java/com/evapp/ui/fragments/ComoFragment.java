package com.evapp.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.evapp.R;
import com.evapp.events.EventEvaluar;
import com.evapp.events.EventSelectOption;
import com.evapp.ui.util.OpcionesComo;
import com.evapp.ui.util.OpcionesEvaluacion;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComoFragment extends Fragment {

    @InjectView(R.id.numberPicker)
    NumberPicker opcionesComo;
    private String[] opciones;
    private OpcionesComo opcion;

    public ComoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_como, container, false);
        ButterKnife.inject(this, view);
        opciones = getResources().getStringArray(R.array.opcionesComo);
        opcionesComo.setMinValue(0);
        opcionesComo.setMaxValue(opciones.length - 1);
        opcionesComo.setDisplayedValues(opciones);
        opcionesComo.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        opcionesComo.setOnValueChangedListener(new ChangeAspecto());
        opcion = OpcionesComo.muecas;
        return view;
    }

    @OnClick(R.id.play)
    public void startSelectOption(){
        EventBus.getDefault().post(new EventEvaluar(opcion));
    }

    private class ChangeAspecto implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            switch(newVal){
                case 1:
                    opcion = OpcionesComo.aplausos;
                    break;
                case 2:
                    opcion = OpcionesComo.gritos;
                    break;
                case 3:
                    opcion = OpcionesComo.saltos;
                    break;
                case 4:
                    opcion = OpcionesComo.sonrisas;
                    break;
                case 5:
                    opcion = OpcionesComo.besos;
                    break;
                default:
                    opcion = OpcionesComo.muecas;
                    break;
            }

        }
    }
}
