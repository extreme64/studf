package com.example.mastermind.praktikumandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.ArrayMap;

import com.example.mastermind.praktikumandroid.MainActivity;
import com.example.mastermind.praktikumandroid.MainFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import android.util.SparseArray;

/**
 * Created by Mastermind on 25-Jun-15.
 */
// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class CollectionPagerAdapter extends FragmentStatePagerAdapter
{

    public FragmentManager mFragmentManager;
    static final int NUM_ITEMS = 4;
    public final SparseArray<android.support.v4.app.Fragment> mPageReferences = new SparseArray<android.support.v4.app.Fragment>();


    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        //mFragmentTags = new HashMap<Integer, String>();
    }


    @Override
    public android.support.v4.app.Fragment getItem(int i) {
        android.support.v4.app.Fragment f = MainFragment.newInstance(i);
        mPageReferences.put(i, f);
        return MainFragment.newInstance(i);

    }

    @Override
    public int getCount()
    {
        return  NUM_ITEMS; //NUM_ITEMS
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferences.remove(position);
    }

    public MainFragment getFragment(int key) {
        return (MainFragment)mPageReferences.get(key);
    }
    public int getFragmentsCount()
    {
        return mPageReferences.size();
    }

    /**@Override
    public CharSequence getPageTitle(int position)
    {
        return "OBJECT " + (position + 1);
    }*/


    public MainFragment getFragmentWhere(int num)
    {
        List<android.support.v4.app.Fragment> lmf = this.mFragmentManager.getFragments();
        MainFragment mainf = new MainFragment();
        for(android.support.v4.app.Fragment f : lmf)
        {
            mainf = (MainFragment)f;
            if(mainf!=null){
                if(mainf.mNum == num) {
                    break;
                }
            }
        }
        return mainf;
    }



}