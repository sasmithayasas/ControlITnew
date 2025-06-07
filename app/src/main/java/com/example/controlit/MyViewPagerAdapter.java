package com.example.controlit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new ImportantFragment();
            case 1:
                return new ReminderFragment();
            case 2:
                return new InformationFragment();
            default:
                return new ImportantFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
