package com.evapp.ui.fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.evapp.R;
import com.evapp.events.EventEvaluar;
import com.evapp.events.EventRestart;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.google.api.services.plus.Plus;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnviarFragment extends Fragment {

    @InjectView(R.id.textView5)
    TextView textMain;
    @InjectView(R.id.textView7)
    TextView textEva;
    @InjectView(R.id.imageView)
    ImageView imagen;
    @InjectView(R.id.videoView)
    VideoView video;
    @InjectView(R.id.btFace)
    ImageButton btFace;
    @InjectView(R.id.btGoogle)
    ImageButton btGoogle;
    @InjectView(R.id.btInsta)
    ImageButton btInsta;
    @InjectView(R.id.btTwitter)
    ImageButton btTwitter;

    Uri uriFile;
    String text;

    private static final int REQ_SELECT_PHOTO = 1;
    private static final int REQ_START_SHARE = 2;

    public EnviarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enviar, container, false);
        ButterKnife.inject(this, view);
        textMain.setText(Html.fromHtml(getResources().getString(R.string.mensajeEnviar)));
        textEva.setText(text);
        if (uriFile.toString().endsWith(".mp4")) {
            video.setVisibility(View.VISIBLE);
            imagen.setVisibility(View.INVISIBLE);
            video.setVideoPath(uriFile.getPath());
            video.setMediaController(new MediaController(getActivity()));
            video.requestFocus();
        } else {
            if (isAppInstalled(getActivity(),"com.twitter.android")) {
                btTwitter.setEnabled(true);
            }
            video.setVisibility(View.INVISIBLE);
            imagen.setVisibility(View.VISIBLE);
            imagen.setImageURI(uriFile);
        }
        if (isAppInstalled(getActivity(),"com.facebook.katana")) {
            btFace.setEnabled(true);
        }
        if (isAppInstalled(getActivity(),"com.instagram.android")) {
            btInsta.setEnabled(true);
        }
        if (isAppInstalled(getActivity(),"com.google.android.apps.plus")) {
            btGoogle.setEnabled(true);
        }
        Toast.makeText(getActivity(), "Pulsa en la imagen para reproducir el video", Toast.LENGTH_LONG).show();
        return view;
    }

    @OnClick(R.id.btTwitter)
    public void twitter() {
        TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                .text(text)
                .image(uriFile);

        builder.show();
    }

    @OnClick(R.id.btFace)
    public void facebook() {
        ShareContent content = null;
        if (uriFile.toString().endsWith(".mp4")) {
            ShareVideo video = new ShareVideo.Builder()
                    .setLocalUrl(uriFile)
                    .build();
            ShareVideoContent videoContent = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .setContentTitle("Evapp Evaluacción")
                    .setContentDescription(text)
                    .build();
            content = videoContent;
        } else {
            SharePhoto photo = new SharePhoto.Builder()
                    .setImageUrl(uriFile).setCaption(text).build();
            SharePhotoContent photoContent = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            content = photoContent;

        }
        ShareDialog.show(this,content);
    }

    @OnClick(R.id.btRestart)
    public void restart() {
        EventBus.getDefault().post(new EventRestart());
    }

    @OnClick(R.id.btGoogle)
    public void google() {
        String mime;
        if (uriFile.toString().endsWith(".mp4"))
            mime = "video/*";
        else
            mime = "image/*";
        // Launch the Google+ share dialog with attribution to your app.
        Intent shareIntent = new PlusShare.Builder(getActivity())
                .setType(mime)
                .setText(text)
                .addStream(uriFile)
                .getIntent();

        getActivity().startActivityForResult(shareIntent, 0);

    }

    @OnClick(R.id.btInsta)
    public void insta() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage("com.instagram.android");
        // Set the MIME type
        if (uriFile.toString().endsWith(".mp4"))
            intent.setType("video/*");
        else
            intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, uriFile);
        getActivity().startActivityForResult(intent, 1);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void setDataFromEvent(Uri uri, String text) {
        this.uriFile = uri;
        this.text = text;
    }

}
