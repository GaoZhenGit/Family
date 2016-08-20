package com.gz.family.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.androidquery.AQuery;
import com.gz.family.Mqtt.MService;
import com.gz.family.R;
import com.gz.family.adapter.TabPagerAdapter;
import com.gz.family.dataBase.UserStore;
import com.gz.family.fragment.FamilyTabFragment;
import com.gz.family.fragment.UserTabFragment;
import com.gz.family.model.ChatMessage;
import com.gz.family.view.PagerTabWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends BasePageActivity {

    private PagerTabWidget mTabWidget;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private AQuery aq;

    private FamilyTabFragment familyTabFragment;
    private UserTabFragment userTabFragment;

    @Override
    protected void initData() {
        Intent intent = new Intent(this, MService.class);
        startService(intent);
    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_main);
        aq = new AQuery(this);
    }

    @Override
    protected void initView() {
        aq.id(R.id.title_left_img).visible();
        aq.id(R.id.title_mid_text).text("家族网");
        aq.id(R.id.title_right_img).image(R.mipmap.add).visible();
        initTab();
    }

    @Override
    protected void setListener() {
        aq.id(R.id.title_left_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aq.id(R.id.title_right_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = {"搜索添加", "二维码添加"};
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        startActivity(new Intent(getBaseContext(), SearchFamilyActivity.class));
                                        break;
                                    case 1:
                                    default:
                                }
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    private void initTab() {
        mTabWidget = (PagerTabWidget) findViewById(R.id.me_tabwidget);
        mTabWidget.setDividerDrawable(null);
        mTabWidget.addTab(new View(this));
        mTabWidget.addTab(LayoutInflater.from(this).inflate(R.layout.tab_member, null, false));
        mTabWidget.addTab(LayoutInflater.from(this).inflate(R.layout.tab_user, null, false));

        List<Fragment> fragmentList = new ArrayList<>();

        familyTabFragment = new FamilyTabFragment();
        userTabFragment = new UserTabFragment();
        fragmentList.add(new Fragment());
        fragmentList.add(familyTabFragment);
        fragmentList.add(userTabFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager = (ViewPager) findViewById(R.id.me_viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabWidget.setmViewPager(mViewPager);
        mTabWidget.setmOnTabSelectedListener(new PagerTabWidget.OnTabSelectedListener() {
            @Override
            public void onSelected(List<View> tabViews, int position) {
                switch (position) {
                    /**调整下方tabwidget的图标和字体颜色随划动而改变
                     * 标题栏右上角按钮的出现和消失
                     */
                    case 0:
                        aq.id(R.id.tab_member_img).image(R.mipmap.family_grey);
                        aq.id(R.id.tab_member_text).textColorId(R.color.light_grey);
                        aq.id(R.id.tab_user_img).image(R.mipmap.ic_user_grey);
                        aq.id(R.id.tab_user_text).textColorId(R.color.light_grey);
                        break;
                    case 1:
                        aq.id(R.id.tab_member_img).image(R.mipmap.family_blue);
                        aq.id(R.id.tab_member_text).textColorId(R.color.color_theme);
                        aq.id(R.id.tab_user_img).image(R.mipmap.ic_user_grey);
                        aq.id(R.id.tab_user_text).textColorId(R.color.light_grey);
                        break;
                    case 2:
                        aq.id(R.id.tab_member_img).image(R.mipmap.family_grey);
                        aq.id(R.id.tab_member_text).textColorId(R.color.light_grey);
                        aq.id(R.id.tab_user_img).image(R.mipmap.ic_user_white);
                        aq.id(R.id.tab_user_text).textColorId(R.color.color_theme);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (familyTabFragment != null) {
            familyTabFragment.refreshData();
        }
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        super.onBackPressed();
    }
}
