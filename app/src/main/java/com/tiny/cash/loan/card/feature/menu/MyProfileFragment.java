package com.tiny.cash.loan.card.feature.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentMyproficeBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyProfileFragment extends BaseFragment {
    private FragmentMyproficeBinding mBinding;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    public static MyProfileFragment newInstance() {
        Bundle args = new Bundle();
        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_myprofice, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initContent();
        initTab();
    }

    private void initTab() {
        mBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        mBinding.tablayout.setTabTextColors(ContextCompat.getColor(getContext(),
//                R.color.color_656565), ContextCompat.getColor(getContext(), R.color.color_333333));
        mBinding.tablayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(),
                R.color.color_09CAB3));
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        ViewCompat.setElevation(mBinding.tablayout, 10);
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
    }

    private void initContent() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("Basic info ");
        tabIndicators.add("Referee info ");
        tabIndicators.add("Details info ");
        tabFragments = new ArrayList<>();
        tabFragments.add(BasicInfoFragment.newInstance());
        tabFragments.add(ContactInfoFragment.newInstance());
        tabFragments.add(OtherInfoFragment.newInstance());
        contentAdapter = new ContentPagerAdapter(getChildFragmentManager());
        mBinding.viewpager.setOffscreenPageLimit(3);
        mBinding.viewpager.setAdapter(contentAdapter);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }
}
