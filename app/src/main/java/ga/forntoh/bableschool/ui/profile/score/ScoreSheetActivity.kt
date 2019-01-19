package ga.forntoh.bableschool.ui.profile.score

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.ui.BaseActivity
import ga.forntoh.bableschool.utils.enableWhiteStatusBar
import ga.forntoh.bableschool.utils.setTabWidthAsWrapContent
import kotlinx.android.synthetic.main.activity_score_sheet.*

class ScoreSheetActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private val firstTerm: TermScoreFragment by lazy {
        TermScoreFragment().apply {
            arguments = Bundle().apply { putInt("term", 1) }
        }
    }
    private val secondTerm: TermScoreFragment by lazy {
        TermScoreFragment().apply {
            arguments = Bundle().apply { putInt("term", 2) }
        }
    }
    private val thirdTerm: TermScoreFragment by lazy {
        TermScoreFragment().apply {
            arguments = Bundle().apply { putInt("term", 3) }
        }
    }

    private val toolbarTitle by lazy { findViewById<TextView>(R.id.title) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_sheet)

        toolbarTitle.setText(R.string.my_score_sheet)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        disableFlags(true)
        enableWhiteStatusBar()

        main_pager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)

        tab_layout.setupWithViewPager(main_pager)

        tab_layout.setTabWidthAsWrapContent(tab_layout.tabCount - 1)
        tab_layout.getTabAt(tab_layout.tabCount - 1)?.setIcon(R.drawable.ic_multiline_chart)

        tab_layout.setTabWidthAsWrapContent(0)
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_class)

        main_pager.addOnPageChangeListener(this)
    }

    override fun onPageSelected(position: Int) {
        if (position == 0 || position == tab_layout.tabCount - 1)
            tab_layout.getTabAt(position)?.icon?.setColorFilter(Color.parseColor("#2283FF"), PorterDuff.Mode.SRC_ATOP)
        else {
            tab_layout.getTabAt(0)?.icon?.setColorFilter(Color.parseColor("#C5C5C5"), PorterDuff.Mode.SRC_ATOP)
            tab_layout.getTabAt(tab_layout.tabCount - 1)?.icon?.setColorFilter(Color.parseColor("#C5C5C5"), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = 5

        override fun getItem(position: Int) = when (position) {
            0 -> YearScoreFragment()
            1 -> firstTerm
            2 -> secondTerm
            3 -> thirdTerm
            else -> YearGraphFragment()
        }

        override fun getPageTitle(position: Int) = when (position) {
            1 -> "1st Term"
            2 -> "2nd Term"
            3 -> "3rd Term"
            else -> ""
        }
    }

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
}
