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
 * Menadzer svih frqagmenata
 * Koristi ga mViewPager kao svoj adapter
 */

public class CollectionPagerAdapter extends FragmentStatePagerAdapter
{

    public FragmentManager mFragmentManager;

    /** definise sa koliko ce stranica tj. fragmenta adapter da radi,
     * setovati pri inicijalizaciji */
    private int NUM_ITEMS;
    public final SparseArray<android.support.v4.app.Fragment> mPageReferences = new SparseArray<android.support.v4.app.Fragment>();


    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    /* TB. */
    public CollectionPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        mFragmentManager = fm;
        this.NUM_ITEMS = numOfTabs;
    }


    public int getNUM_PAGES() { return this.getCount(); }
    public void setNUM_PAGES(int num_pages) {
        this.NUM_ITEMS = num_pages;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int i) {
        android.support.v4.app.Fragment f = MainFragment.newInstance(i);
        mPageReferences.put(i, f);
        return MainFragment.newInstance(i);

    }

    /** automatski se poziva i odredjuje broj stranica,
     * prepisano iz klase PagerAdapter koja je parent klasa
     * FragmentStatePagerAdapter
     * */
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