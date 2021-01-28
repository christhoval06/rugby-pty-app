package app.christhoval.rugbypty.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import app.christhoval.rugbypty.R;

public class ComingSoonFragment extends Fragment {
    public static final String DEEP_LINK = "comingsoon";

    public static ComingSoonFragment newInstance(Bundle extras) {
        ComingSoonFragment fragment = new ComingSoonFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static ComingSoonFragment newInstance() {
        return new ComingSoonFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animateText();
    }

    private void animateText() {
        if (getView() != null) {
            TextView comingsoon = (TextView) getView().findViewById(R.id.comingsoon);
            if (comingsoon != null) {
                Animation animation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.right_in);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                comingsoon.startAnimation(animation);

            }
        }
    }
}
