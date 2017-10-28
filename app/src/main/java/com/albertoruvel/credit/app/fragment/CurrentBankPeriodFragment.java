package com.albertoruvel.credit.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albertoruvel.credit.app.R;

public class CurrentBankPeriodFragment extends Fragment {

    public CurrentBankPeriodFragment() {
        // Required empty public constructor
    }

    public static CurrentBankPeriodFragment newInstance() {
        CurrentBankPeriodFragment fragment = new CurrentBankPeriodFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_bank_period, container, false);
    }
}
