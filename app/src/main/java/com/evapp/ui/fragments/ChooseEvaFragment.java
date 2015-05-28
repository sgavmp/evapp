package com.evapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.evapp.R;
import com.evapp.events.EventSelectOption;
import com.evapp.ui.util.OpcionesEvaluacion;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class ChooseEvaFragment extends Fragment {

    @InjectView(R.id.numberPicker) NumberPicker aspectos;
    @InjectView(R.id.textView3) TextView cabecera;
    @InjectView(R.id.textView6) TextView descripcion;
    private String[] opciones;
    private OpcionesEvaluacion opcion;

    public ChooseEvaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chose_eva, container, false);
        ButterKnife.inject(this, view);
        //language = (NumberPicker) fragment.findViewById(R.id.numberPicker);
        opciones = getResources().getStringArray(R.array.aspect);
        aspectos.setMinValue(0);
        aspectos.setMaxValue(opciones.length - 1);
        aspectos.setDisplayedValues(opciones);
        aspectos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        aspectos.setOnValueChangedListener(new ChangeAspecto());
        opcion = OpcionesEvaluacion.visita;
        cabecera.setText(Html.fromHtml(getResources().getString(R.string.chooseEvaText)));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.play)
    public void startSelectOption(){
        EventBus.getDefault().post(new EventSelectOption(opcion));
    }

    private class ChangeAspecto implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            switch(newVal){
                case 1:
                    opcion = OpcionesEvaluacion.obras;
                    descripcion.setText(getResources().getText(R.string.obras_descripcion));
                    break;
                case 2:
                    opcion = OpcionesEvaluacion.diseno;
                    descripcion.setText(getResources().getText(R.string.diseno_descripcion));
                    break;
                case 3:
                    opcion = OpcionesEvaluacion.otros;
                    descripcion.setText(getResources().getText(R.string.otros_descripcion));
                    break;
                case 5:
                    opcion = OpcionesEvaluacion.obras;
                    descripcion.setText(getResources().getText(R.string.obras_descripcion));
                    break;
                case 6:
                    opcion = OpcionesEvaluacion.diseno;
                    descripcion.setText(getResources().getText(R.string.diseno_descripcion));
                    break;
                case 7:
                    opcion = OpcionesEvaluacion.otros;
                    descripcion.setText(getResources().getText(R.string.otros_descripcion));
                    break;
                default:
                    opcion = OpcionesEvaluacion.visita;
                    descripcion.setText(getResources().getText(R.string.visita_descripcion));
                    break;
            }

        }
    }

}
