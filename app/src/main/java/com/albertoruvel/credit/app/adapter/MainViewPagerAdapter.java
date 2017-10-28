package com.albertoruvel.credit.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.albertoruvel.credit.app.fragment.CreditCardsFragment;
import com.albertoruvel.credit.app.fragment.CurrentBankPeriodFragment;
import com.albertoruvel.credit.app.fragment.SummaryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jose.rubalcaba on 10/26/2017.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    static List<Fragment> fragments;
    static String[] titles = { "Credit Cards", "Summary", "Current Period" };
    static {
        fragments = new ArrayList<>();
        fragments.add(CreditCardsFragment.newInstance());
        fragments.add(SummaryFragment.newInstance());
        fragments.add(CurrentBankPeriodFragment.newInstance());
    }

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
