package ga.forntoh.bableschool;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.alfianyusufabdullah.SPager;

import java.util.Objects;

import ga.forntoh.bableschool.fragments.TermScoreFragment;
import ga.forntoh.bableschool.fragments.YearGraphFragment;
import ga.forntoh.bableschool.fragments.YearScoreFragment;
import ga.forntoh.bableschool.utils.Utils;

public class ScoreSheetActivity extends BaseActivity {

    private TermScoreFragment firstTerm = new TermScoreFragment();
    private TermScoreFragment secondTerm = new TermScoreFragment();
    private TermScoreFragment thirdTerm = new TermScoreFragment();

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_sheet);

        TextView toolbar_title = findViewById(R.id.title);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        toolbar_title.setText(R.string.my_score_sheet);

        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        disableFlags(true);
        enableWhiteStatusBar();

        Bundle firstBundle = new Bundle();
        firstBundle.putInt("term", 1);
        firstTerm.setArguments(firstBundle);

        Bundle secondBundle = new Bundle();
        secondBundle.putInt("term", 2);
        secondTerm.setArguments(secondBundle);

        Bundle thirdBundle = new Bundle();
        thirdBundle.putInt("term", 3);
        thirdTerm.setArguments(thirdBundle);

        SPager sPager = findViewById(R.id.mainPage);
        sPager.initFragmentManager(getSupportFragmentManager());
        sPager.addPages("", new YearScoreFragment());
        sPager.addPages("1st Term", firstTerm);
        sPager.addPages("2nd Term", secondTerm);
        sPager.addPages("3rd Term", thirdTerm);
        sPager.addPages("", new YearGraphFragment());
        sPager.addTabLayout(tabLayout);
        sPager.build();

        Utils.setTabWidthAsWrapContent(tabLayout, tabLayout.getTabCount() - 1);
        tabLayout.getTabAt(tabLayout.getTabCount() - 1).setIcon(R.drawable.ic_multiline_chart);

        Utils.setTabWidthAsWrapContent(tabLayout, 0);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_class);

        sPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == tabLayout.getTabCount() - 1)
                    tabLayout.getTabAt(position).getIcon().setColorFilter(Color.parseColor("#2283FF"), PorterDuff.Mode.SRC_ATOP);
                else {
                    tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#C5C5C5"), PorterDuff.Mode.SRC_ATOP);
                    tabLayout.getTabAt(tabLayout.getTabCount() - 1).getIcon().setColorFilter(Color.parseColor("#C5C5C5"), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
