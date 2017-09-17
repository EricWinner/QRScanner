package com.example.ubuntu.qc_scanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ubuntu.qc_scanner.fragment.WelcomeFrament;
import com.example.ubuntu.qc_scanner.util.ScreenUtil;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by ubuntu on 17-7-4.
 */

public class WelComeActivity extends AppCompatActivity  {

    private static final String TAG = "WelComeActivity";

    private static final int NUM_PAGES = 4;
    private static final int PAGE_ZERO = 0;
    private static final int PAGE_ONE = 1;
    private static final int PAGE_TWO = 2;
    private static final int PAGE_THREE = 3;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private LinearLayout mCircles;
    private Button mSkipButton;
    private Button mDoneButton;
    private ImageButton mNextButton;
    private boolean mIsOpaque = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_welcome_activiy);

        initViews();
        initCircles();
    }

    private void initViews() {
        mSkipButton = (Button) this.findViewById(R.id.welcome_skip);
        mNextButton = (ImageButton) this.findViewById(R.id.welcome_next);
        mDoneButton = (Button) this.findViewById(R.id.welcome_done);

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTWizard();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTWizard();
            }
        });

        mViewPager = (ViewPager) this.findViewById(R.id.welcome_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(true, new CrossfadePageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == NUM_PAGES - 1 && position > 0) {
                    if (mIsOpaque) {
                        mViewPager.setBackgroundColor(Color.TRANSPARENT);
                        mIsOpaque = false;
                    }
                } else {
                    if (!mIsOpaque) {
                        mViewPager.setBackgroundColor(getResources().getColor(R.color.primary_material_light));
                        mIsOpaque = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                if (position == NUM_PAGES - 1) {
                    mSkipButton.setVisibility(View.GONE);
                    mNextButton.setVisibility(View.GONE);
                    mDoneButton.setVisibility(View.VISIBLE);
                } else if (position < NUM_PAGES - 1) {
                    mSkipButton.setVisibility(View.VISIBLE);
                    mNextButton.setVisibility(View.VISIBLE);
                    mDoneButton.setVisibility(View.GONE);
                } else if (position == NUM_PAGES ) {
                    endTWizard();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initCircles() {
        mCircles = (LinearLayout) this.findViewById(R.id.welcome_circles);
        int padding = ScreenUtil.getPadding(this);
        for (int i = 0; i < NUM_PAGES ; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.ic_swipe_indicator_white_18dp);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            mCircles.addView(circle);
        }
        setIndicator(0);
    }


    private void endTWizard() {
        Intent intent = new Intent();
        intent.setClass(WelComeActivity.this, SimpleLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        finish();
    }

    private void setIndicator(int index) {
        if (index < NUM_PAGES) {
            for (int i = 0; i < NUM_PAGES ; i++) {
                ImageView circle = (ImageView) mCircles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(R.color.welcome_selected));
                } else {
                    circle.setColorFilter(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == PAGE_ZERO) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            WelcomeFrament fragment = null;
            switch (position) {
                case PAGE_ZERO:
                    fragment = WelcomeFrament.newInstance(R.layout.welcome_fragment1);
                    break;
                case PAGE_ONE:
                    fragment = WelcomeFrament.newInstance(R.layout.welcome_fragment2);
                    break;
                case PAGE_TWO:
                    fragment = WelcomeFrament.newInstance(R.layout.welcome_fragment3);
                    break;
                case PAGE_THREE:
                    fragment = WelcomeFrament.newInstance(R.layout.welcome_fragment4);
                    break;
                default:
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private class CrossfadePageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            View backgroundView = page.findViewById(R.id.welcome_fragment);
            View text_head = page.findViewById(R.id.heading);
            View text_content = page.findViewById(R.id.content);
            View object1 = page.findViewById(R.id.a000);
            View object2 = page.findViewById(R.id.a001);

            View object3 = page.findViewById(R.id.a002);
            View object4 = page.findViewById(R.id.a003);
            View object5 = page.findViewById(R.id.a004);
            View object6 = page.findViewById(R.id.a005);
            View object7 = page.findViewById(R.id.a006);
            View object8 = page.findViewById(R.id.a008);
            View object9 = page.findViewById(R.id.a010);
            View object10 = page.findViewById(R.id.a011);
            View object11 = page.findViewById(R.id.a007);
            View object12 = page.findViewById(R.id.a012);
            View object13 = page.findViewById(R.id.a013);

            if (0 <= position && position < 1) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }
            if (-1 < position && position < 0) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }

            if (position <= -1.0f || position >= 1.0f) {
            } else if (position == 0.0f) {
            } else {
                if (backgroundView != null) {
                    ViewHelper.setAlpha(backgroundView, 1.0f - Math.abs(position));

                }

                if (text_head != null) {
                    ViewHelper.setTranslationX(text_head, pageWidth * position);
                    ViewHelper.setAlpha(text_head, 1.0f - Math.abs(position));
                }

                if (text_content != null) {
                    ViewHelper.setTranslationX(text_content, pageWidth * position);
                    ViewHelper.setAlpha(text_content, 1.0f - Math.abs(position));
                }

                if (object1 != null) {
                    ViewHelper.setTranslationX(object1, pageWidth * position);
                }

                // parallax effect
                if (object2 != null) {
                    ViewHelper.setTranslationX(object2, pageWidth * position);
                }

                if (object3 != null) {
                    ViewHelper.setTranslationX(object3, (float) (pageWidth / 1.2 * position));
                }

                if (object4 != null) {
                    ViewHelper.setTranslationX(object4, pageWidth / 2 * position);
                }
                if (object5 != null) {
                    ViewHelper.setTranslationX(object5, pageWidth / 2 * position);
                }
                if (object6 != null) {
                    ViewHelper.setTranslationX(object6, pageWidth / 2 * position);
                }
                if (object7 != null) {
                    ViewHelper.setTranslationX(object7, pageWidth / 2 * position);
                }

                if (object8 != null) {
                    ViewHelper.setTranslationX(object8, (float) (pageWidth / 1.5 * position));
                }

                if (object9 != null) {
                    ViewHelper.setTranslationX(object9, (float) (pageWidth / 2 * position));
                }

                if (object10 != null) {
                    ViewHelper.setTranslationX(object10, pageWidth / 2 * position);
                }

                if (object11 != null) {
                    ViewHelper.setTranslationX(object11, (float) (pageWidth / 1.2 * position));
                }

                if (object12 != null) {
                    ViewHelper.setTranslationX(object12, (float) (pageWidth / 1.3 * position));
                }

                if (object13 != null) {
                    ViewHelper.setTranslationX(object13, (float) (pageWidth / 1.8 * position));
                }


            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewPager != null) {
            mViewPager.clearOnPageChangeListeners();
        }
    }
}
