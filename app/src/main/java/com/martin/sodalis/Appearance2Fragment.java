package com.martin.sodalis;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

public class Appearance2Fragment extends Fragment {

    private Button chooseButton;

    private Uri uriParsed;

    private ScalableVideoView scalableVideoView;

    private ProgressBar videoProgressBar;

    private DatabaseReference databaseReference;

    private String userId;

    private static final String TAG = "Appearance2";

    /**
     * exact same logic as first appearance, but isn't a premium appearance and the user can choose
     * this one for free. Otherwise indistinguishable
     *
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View appearance2View = inflater.inflate(R.layout.fragment_appearance2, container, false);

        videoProgressBar = appearance2View.findViewById(R.id.videoview_bar);

        scalableVideoView = appearance2View.findViewById(R.id.video_view_tester);
        scalableVideoView.setVisibility(View.GONE);

        chooseButton = appearance2View.findViewById(R.id.premium_button0);

        videoProgressBar.setVisibility(View.VISIBLE);

        userId = getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        playVideoLocal();

        /*try {
            playVideoView();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        chooseButton = appearance2View.findViewById(R.id.appearance_button2);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())

                        .setMessage("Is this the appearance you want?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d(TAG, "onClick: yes");

                                Log.i(TAG, "Setting user's appearance base: appearance2");

                                // set selection in user's db node to be read later
                                databaseReference.child("users").child(userId).child("appearanceBase")
                                        .setValue("appearance2");

                                Intent iNext = new Intent(getActivity(), ChooseOutfitActivity.class);
                                startActivity(iNext);

                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });


        return appearance2View;
    } // end of oncreate

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void playVideoLocal() {

        try {
            scalableVideoView.setRawData(R.raw.rach);

            scalableVideoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i("videoView2", "Video view is prepared");
                    //Log.i("videoView3", "Uri used is: " + uriParsed.toString());

                    scalableVideoView.setVisibility(View.VISIBLE);
                    videoProgressBar.setVisibility(View.GONE);

                    scalableVideoView.setVolume(0,0);
                    scalableVideoView.setLooping(true);

                    scalableVideoView.start();

                    if (scalableVideoView.isPlaying()) {
                        videoProgressBar.setVisibility(View.GONE);
                    }
                }
            });

        } catch (IOException ioe) {
            //handle error
        }
    }

    /*private void playVideoView() throws IOException {

        Log.i("videoView2", "Video view is doing something");

        uriParsed = Uri.parse("https://firebasestorage.googleapis.com/v0/b/sodalis-53c9d.appspot.com/o/rach.mp4?alt=media&token=c2c96148-b93e-4230-ac09-336ee8d3f630");
        scalableVideoView.setDataSource(getActivity(), uriParsed);

        scalableVideoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("videoView2", "Video view is prepared");
                Log.i("videoView2", "Uri used is: " + uriParsed.toString());

                scalableVideoView.setVisibility(View.VISIBLE);
                videoProgressBar.setVisibility(View.GONE);

                scalableVideoView.start();
                scalableVideoView.setVolume(0,0);
                scalableVideoView.setLooping(true);

                if (scalableVideoView.isPlaying()) {
                    videoProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }*/
}
