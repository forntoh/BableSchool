package ga.forntoh.bableschool.ui.category

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.ui.base.BaseActivity
import ga.forntoh.bableschool.ui.category.courseNotes.CourseNotesFragment
import ga.forntoh.bableschool.ui.category.forum.ForumFragment
import ga.forntoh.bableschool.ui.category.news.NewsFragment
import ga.forntoh.bableschool.ui.category.profile.MyProfileFragment
import ga.forntoh.bableschool.ui.category.ranking.RankingFragment
import ga.forntoh.bableschool.utilities.enableWhiteStatusBar

class CategoryActivity : BaseActivity() {

    private val toolbarTitle by lazy { findViewById<TextView>(R.id.title) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        disableFlags(true)

        toolbarTitle.text = intent.getStringExtra("title")

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        enableWhiteStatusBar()

        when (intent.getLongExtra("id", 5).toInt()) {
            0 -> loadFragment(NewsFragment())
            1 -> loadFragment(CourseNotesFragment())
            2 -> loadFragment(RankingFragment())
            3 -> loadFragment(ForumFragment())
            4 -> loadFragment(MyProfileFragment())
        }
    }

    fun loadFragment(fragment: Fragment): FragmentTransaction =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, fragment)
                commit()
            }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }
}
