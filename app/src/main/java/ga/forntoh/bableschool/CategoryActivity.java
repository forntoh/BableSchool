package ga.forntoh.bableschool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.google.gson.Gson;

import ga.forntoh.bableschool.fragments.CourseNotesFragment;
import ga.forntoh.bableschool.fragments.ForumFragment;
import ga.forntoh.bableschool.fragments.MyProfileFragment;
import ga.forntoh.bableschool.fragments.NewsFragment;
import ga.forntoh.bableschool.fragments.RankingFragment;
import ga.forntoh.bableschool.model.Category;

public class CategoryActivity extends BaseActivity {

    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        disableFlags(true);

        Category category = new Gson().fromJson(getIntent().getStringExtra("category"), Category.class);
        toolbar_title = findViewById(R.id.title);
        toolbar_title.setText(category.getTitle());

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enableWhiteStatusBar();

        switch (category.getId()) {
            case 0:
                loadFragment(new NewsFragment());
                break;
            case 1:
                loadFragment(new CourseNotesFragment());
                break;
            case 2:
                loadFragment(new RankingFragment());
                break;
            case 3:
                loadFragment(new ForumFragment());
                break;
            case 4:
                loadFragment(new MyProfileFragment());
            default:
                break;
        }
    }

    public FragmentTransaction loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
        return transaction;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }
}
