package ga.forntoh.bableschool.store;

import android.content.Context;

import com.nytimes.android.external.store3.base.impl.BarCode;

import ga.forntoh.bableschool.LoginActivity;
import ga.forntoh.bableschool.MainActivity;
import ga.forntoh.bableschool.fragments.CourseNotesFragment;
import ga.forntoh.bableschool.fragments.RankingFragment;
import ga.forntoh.bableschool.fragments.TermScoreFragment;
import ga.forntoh.bableschool.fragments.YearScoreFragment;
import ga.forntoh.bableschool.model.Category;
import ga.forntoh.bableschool.model.Score;
import ga.forntoh.bableschool.model.User;
import ga.forntoh.bableschool.model.misc.AnnualRank;
import ga.forntoh.bableschool.model.misc.TopSchool;
import ga.forntoh.bableschool.utils.Utils;

public class MyStores {

    public static final BarCode CATEGORIES = new BarCode(Category.class.getSimpleName(), Utils.getKey(MainActivity.class, "categories"));

    public static final BarCode USER = new BarCode(User.class.getSimpleName(), Utils.getKey(LoginActivity.class, "user"));

    public static final BarCode RANKING = new BarCode(TopSchool.class.getSimpleName(), Utils.getKey(RankingFragment.class, "topList"));

    public static BarCode TERM_RANK(Context c) {
        return new BarCode(AnnualRank.class.getSimpleName(), Utils.getKey(YearScoreFragment.class, "rank-" + StorageUtil.with(c).loadMatriculation()));
    }

    public static BarCode COURSES(Context c) {
        return new BarCode(User.class.getSimpleName(), Utils.getKey(CourseNotesFragment.class, "courses-" + StorageUtil.with(c).loadMatriculation()));
    }

    public static BarCode TERM_SCORE(Context c, int term) {
        return new BarCode(Score.class.getSimpleName(), Utils.getKey(TermScoreFragment.class, "term_score-" + StorageUtil.with(c).loadMatriculation(), term));
    }
}
