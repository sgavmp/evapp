package com.evapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.evapp.events.EventEnviar;
import com.evapp.events.EventEvaluar;
import com.evapp.events.EventRestart;
import com.evapp.events.EventSelectOption;
import com.evapp.ui.fragments.ChooseEvaFragment;
import com.evapp.ui.fragments.ComoFragment;
import com.evapp.ui.fragments.EnviarFragment;
import com.evapp.ui.fragments.EvaluacionFragment;
import com.evapp.ui.fragments.MainFragment;
import com.evapp.R;
import com.evapp.events.EventStartEva;
import com.facebook.FacebookSdk;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xzecK7YcyyPcff93GJgvocmCD";
    private static final String TWITTER_SECRET = "652Ozf4lIccIrbaqahkwiYXAqwA7ijtZ0SGaOavfMdBfML1VOo";

    private MainFragment mainFragment;
    private ChooseEvaFragment chooseEvaFragment;
    private ComoFragment comoFragment;
    private EvaluacionFragment evaluacionFragment;
    private EnviarFragment enviarFragment;
    private Toolbar toolbar;

    @InjectView(R.layout.activity_main) View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Evapp");
        setSupportActionBar(toolbar);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        EventBus.getDefault().register(this);
        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            chooseEvaFragment = new ChooseEvaFragment();
            comoFragment = new ComoFragment();
            evaluacionFragment = new EvaluacionFragment();
            enviarFragment = new EnviarFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, mainFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(EventStartEva event) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, chooseEvaFragment).addToBackStack(null).commit();
    }

    public void onEvent(EventSelectOption event) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, comoFragment).addToBackStack(null).commit();
    }

    public void onEvent(EventEvaluar event) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, evaluacionFragment).addToBackStack(null).commit();
        evaluacionFragment.setOpcionComoEvaluar(event.getOpcion());
    }

    public void onEvent(EventEnviar event) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, enviarFragment).addToBackStack(null).commit();
        enviarFragment.setDataFromEvent(event.getUri(), event.getText());
    }

    public void onEvent(EventRestart event) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mainFragment).addToBackStack(null).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (evaluacionFragment.isVisible())
            evaluacionFragment.onActivityResult(requestCode, resultCode, intent);

    }

}
