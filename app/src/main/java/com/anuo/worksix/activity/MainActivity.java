package com.anuo.worksix.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.anuo.worksix.R;
import com.anuo.worksix.adapter.PagerAdapter;
import com.anuo.worksix.fragment.FragmentFactory;
import com.anuo.worksix.manager.ContactsMessageManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final int CODE = 1;
    TabLayout tabLayout;
    ViewPager viewPager;
    List<Fragment> fragmentList;
    PagerAdapter pagerAdapter;
    String[] titles = {"呼入电话", "呼出电话", "未接电话"};
    private List<String> unPermissionList = new ArrayList<String>();
    private String[] permissionList = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()) initView();
    }


    private void initView() {
        ContactsMessageManager.getInstance().refresh(this, new ContactsMessageManager.Callback() {
            @Override
            public void onSuccess() {
                initViewPagerFragment();
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initViewPagerFragment() {
        tabLayout = findViewById(R.id.tab_main);
        viewPager = findViewById(R.id.pager_main);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        fragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragmentList.add(FragmentFactory.createFragment(i));
        }
        pagerAdapter.setTitles(titles);
        pagerAdapter.setFragmentList(fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public boolean checkPermission() {
        unPermissionList.clear();
        for (String aPermissionList : permissionList) {
            if (ContextCompat.checkSelfPermission(this, aPermissionList) !=
                    PackageManager.PERMISSION_GRANTED) {
                unPermissionList.add(aPermissionList);
            }
        }
        if (unPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionList, CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CODE == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult == -1) {
                    Toast.makeText(this, "存在权限没有通过", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            initView();
        }
    }


}
