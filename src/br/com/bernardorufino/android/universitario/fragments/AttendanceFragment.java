package br.com.bernardorufino.android.universitario.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.bernardorufino.android.universitario.R;
import roboguice.fragment.RoboFragment;

public class AttendanceFragment extends RoboFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attendance, container, false);
    }
}
