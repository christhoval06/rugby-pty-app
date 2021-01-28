package app.christhoval.rugbypty.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.utilities.Constant;

public class TournamentFragment extends Fragment {

    public TournamentFragment newInstance(Bundle extras) {
        TournamentFragment fragment = new TournamentFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public TournamentFragment newInstance() {
        return new TournamentFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.v(Constant.TAG, "init Post");

        return inflater.inflate(R.layout.fragment_tournament, container, false);
    }
}

