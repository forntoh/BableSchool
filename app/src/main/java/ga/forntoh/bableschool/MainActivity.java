package ga.forntoh.bableschool;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import ga.forntoh.bableschool.adapters.CategoryAdapter;
import ga.forntoh.bableschool.model.Category;
import ga.forntoh.bableschool.utils.InsetDecoration;

import static ga.forntoh.bableschool.utils.Utils.setupListDisplay;

public class MainActivity extends BaseActivity {

    private CategoryAdapter adapter;
    private ArrayList<Category> categoriesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disableFlags(true);
        enableWhiteStatusBar();

        setSupportActionBar(findViewById(R.id.toolbar));

        RecyclerView rvCategories = findViewById(R.id.rv_categories);
        adapter = new CategoryAdapter(categoriesList);
        setupListDisplay(adapter, rvCategories, 2, false, new InsetDecoration(this, 16));

        fetchItems();
    }

    private void fetchItems() {
        categoriesList.clear();
        categoriesList.addAll(Category.getDummyCategories());
        adapter.notifyDataSetChanged();
    }
}
