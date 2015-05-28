package com.evapp.ui.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evapp.R;
import com.evapp.events.EventEnviar;
import com.evapp.ui.util.OpcionesComo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class EvaluacionFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int RESULT_OK = -1;
    public static final int RESULT_CANCELED = 0;
    private static final String mensajeInicio = "Esta es mi #evapp #evluaccion. Mira mi ";
    private static final String mensajeFinal = "@evapp";
    private Uri fileUri = null;
    private OpcionesComo opcion;
    private String mensaje;


    @InjectView(R.id.buttonCamera)
    ImageButton btCamera;

    @InjectView(R.id.buttonVideo)
    ImageButton btVideo;

    @InjectView(R.id.textView4)
    TextView textMain;


    public EvaluacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluacion, container, false);
        ButterKnife.inject(this, view);
        showOptionEva(opcion);
        return view;
    }

    private void showOptionEva(OpcionesComo opcion) {
        switch (opcion) {
            case aplausos:
                mensaje = mensajeInicio.concat("aplauso").concat(mensajeFinal);
                btCamera.setVisibility(View.INVISIBLE);
                btVideo.setVisibility(View.VISIBLE);
                textMain.setText(Html.fromHtml(getResources().getString(R.string.aplausos_descripcion)));
                break;
            case gritos:
                mensaje = mensajeInicio.concat("grito").concat(mensajeFinal);
                btCamera.setVisibility(View.INVISIBLE);
                btVideo.setVisibility(View.VISIBLE);
                textMain.setText(Html.fromHtml(getResources().getString(R.string.gritos_descripcion)));
                break;
            case saltos:
                mensaje = mensajeInicio.concat("salto").concat(mensajeFinal);
                btCamera.setVisibility(View.VISIBLE);
                btVideo.setVisibility(View.INVISIBLE);
                textMain.setText(Html.fromHtml(getResources().getString(R.string.saltos_descripcion)));
                break;
            case sonrisas:
                mensaje = mensajeInicio.concat("sonrisa").concat(mensajeFinal);
                btCamera.setVisibility(View.VISIBLE);
                btVideo.setVisibility(View.INVISIBLE);
                textMain.setText(Html.fromHtml(getResources().getString(R.string.sonrisas_descripcion)));
                break;
            case besos:
                mensaje = mensajeInicio.concat("beso").concat(mensajeFinal);
                btCamera.setVisibility(View.INVISIBLE);
                btVideo.setVisibility(View.VISIBLE);
                textMain.setText(Html.fromHtml(getResources().getString(R.string.besos_descripcion)));
                break;
            default:
                mensaje = mensajeInicio.concat("mueca").concat(mensajeFinal);
                btCamera.setVisibility(View.VISIBLE);
                btVideo.setVisibility(View.INVISIBLE);
                textMain.setText(Html.fromHtml(getResources().getString(R.string.muecas_descripcion)));
                break;
        }
    }

    @OnClick(R.id.buttonCamera)
     public void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name


        // start the image capture Intent
        getActivity().startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @OnClick(R.id.buttonVideo)
     public void dispatchTakeVideoIntent() {
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15); //set the max duration of video

        // start the Video Capture Intent
        getActivity().startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                EventBus.getDefault().post(new EventEnviar(fileUri,mensaje));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Tienes que tomar una foto para continuar", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Ha ocurrido un error al intentar tomar la foto, vuelva a intentarlo", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                EventBus.getDefault().post(new EventEnviar(fileUri,mensaje));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Tienes que tomar un video para continuar", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Ha ocurrido un error al intentar tomar el video, vuelva a intentarlo", Toast.LENGTH_LONG).show();
            }
        }

    }


    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Evapp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Evapp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void setOpcionComoEvaluar(OpcionesComo opcion) {
        this.opcion = opcion;
    }
}
