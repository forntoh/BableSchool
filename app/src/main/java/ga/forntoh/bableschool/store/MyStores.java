package ga.forntoh.bableschool.store;

import com.nytimes.android.external.store3.base.impl.BarCode;

import ga.forntoh.bableschool.LoginActivity;
import ga.forntoh.bableschool.MainActivity;
import ga.forntoh.bableschool.model.Category;
import ga.forntoh.bableschool.model.User;
import ga.forntoh.bableschool.utils.Utils;

public class MyStores {

    public static final BarCode CATEGORIES = new BarCode(Category.class.getSimpleName(), Utils.getKey(MainActivity.class, "categories"));

    public static final BarCode USER = new BarCode(User.class.getSimpleName(), Utils.getKey(LoginActivity.class, "user"));

}
