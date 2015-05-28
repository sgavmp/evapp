package com.evapp.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.evapp.R;
import com.evapp.events.EventStartEva;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class MainFragment extends Fragment {

    @InjectView(R.id.numberPicker) NumberPicker language;
    private String[] languages;
    private Locale myLocale;
    @InjectView(R.id.textView2) TextView textChooseLanguage;
    @InjectView(R.id.textView) TextView textMain;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        languages = getResources().getStringArray(R.array.languages);
        language.setMinValue(0);
        language.setMaxValue(languages.length - 1);
        language.setDisplayedValues(languages);
        language.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        language.setOnValueChangedListener(new ChangeLanguage());
        textMain.setText(Html.fromHtml(getResources().getString(R.string.fraseInicial)));
        new ChangeLanguage().changeLang("en");
        return view;
    }

    private void changeValueByOne(final NumberPicker higherPicker, final boolean increment) {

        Method method;
        try {
            // refelction call for
            // higherPicker.changeValueByOne(true);
            method = higherPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(higherPicker, increment);

        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.play)
    public void startEva(){
        EventBus.getDefault().post(new EventStartEva());
        //RandomPicker randomPicker = new RandomPicker();
        // randomPicker.doInBackground(language);
    }

    private class ChangeLanguage implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            String lang = "en";
            switch(newVal){
                case 1:
                    lang = "it";
                    break;
                case 2:
                    lang = "pt";
                    break;
                case 3:
                    lang = "es";
                    break;
                case 5:
                    lang = "it";
                    break;
                case 6:
                    lang = "pt";
                    break;
                case 7:
                    lang = "es";
                    break;
                default:
                    break;
            }
            changeLang(lang);
        }

        public void changeLang(String lang)
        {
            if (lang.equalsIgnoreCase(""))
                return;
            myLocale = new Locale(lang);
            Locale.setDefault(myLocale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = myLocale;
            getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());
            textChooseLanguage.setText(R.string.text_choose_language);
            textMain.setText(Html.fromHtml(getResources().getString(R.string.fraseInicial)));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }



    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class RandomPicker extends AsyncTask<NumberPicker,NumberPicker,String> {

        @Override
        protected String doInBackground(NumberPicker... params) {
            Random rand = new Random();
            rand.setSeed(System.currentTimeMillis());
            Integer step = rand.nextInt(10) + 30;
            Integer timeSleep = 100;
            for (int i=0;i<step;i++) {
                publishProgress();
                SystemClock.sleep(timeSleep);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(NumberPicker... values) {
            super.onProgressUpdate(values);
            changeValueByOne(values[0], true);
        }
    }
}
