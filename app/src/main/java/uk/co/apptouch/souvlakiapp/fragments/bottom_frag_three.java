package uk.co.apptouch.souvlakiapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import uk.co.apptouch.souvlakiapp.BuildConfig;
import uk.co.apptouch.souvlakiapp.R;
import uk.co.apptouch.souvlakiapp.remote_config.DownloadImageTask;

//import android.support.v4.app.Fragment;

public class bottom_frag_three extends Fragment {
    public static bottom_frag_three newInstance() {
        bottom_frag_three fragment = new bottom_frag_three();
        return fragment;
    }

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String IMAGE_DRAWER_ONE= "image_drawer_one";

    View myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remote config code
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.drawer_frag_one, container, false);

        fetchWelcome();

        return myView;
    }


    private void fetchWelcome() {

        long cacheExpiration = 3600; // 1 hour in seconds.

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(getActivity(), "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                    }
                });
    }

    private void displayWelcomeMessage() {

        String updateImage = mFirebaseRemoteConfig.getString(IMAGE_DRAWER_ONE);
        new DownloadImageTask((ImageView) myView.findViewById(R.id.imageViewDrawerOne))
                .execute(updateImage);
    }
}