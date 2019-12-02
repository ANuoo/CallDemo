package com.anuo.worksix.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {
    private static final int TAB_IN = 0;
    private static final int TAB_OUT = 1;
    private static final int TAB_MISSED = 2;

    private static Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    public static Fragment createFragment(int index) {
        Fragment fragment = mFragmentMap.get(index);
        if (fragment == null) {
            switch (index) {
                case TAB_IN:
                    fragment = new InFragment();
                    break;
                case TAB_OUT:
                    fragment = new CallFragment();
                    break;
                case TAB_MISSED:
                    fragment = new MissedFragment();
                    break;
            }
            mFragmentMap.put(index, fragment);
        }
        return fragment;
    }
}
