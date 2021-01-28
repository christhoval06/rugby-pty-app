package app.christhoval.rugbypty.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.fragments.base.BaseFragment;
import app.christhoval.rugbypty.utilities.Constant;

public class AboutFragment extends BaseFragment {

    public static final String DEEP_LINK = Constant.FRAGMENT_ABOUTUS;


    public static AboutFragment newInstance(Bundle extras) {
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_Base_Gray);

        getActivity().getTheme().applyStyle(R.style.AppTheme_Base_Gray, true);

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}



