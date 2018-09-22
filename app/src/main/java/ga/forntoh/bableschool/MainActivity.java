package ga.forntoh.bableschool;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.forntoh.EasyRecyclerView.EasyRecyclerView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ga.forntoh.bableschool.adapters.CategoryAdapter;
import ga.forntoh.bableschool.model.Category;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StoreRepository;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("unchecked")
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

        adapter = new CategoryAdapter(categoriesList);
        new EasyRecyclerView()
                .setType(EasyRecyclerView.Type.GRID)
                .setAdapter(adapter)
                .setRecyclerView(findViewById(R.id.rv_categories))
                .setItemSpacing(16, null)
                .setSpan(2)
                .initialize();

        fetchItems();
    }

    @SuppressLint("CheckResult")
    private void fetchItems() {
        ApiService service = RetrofitBuilder.createService(ApiService.class);
        StoreRepository repository = new StoreRepository(getApplication());
        repository
                .create(service.getFunctions(), new TypeToken<List<Category>>() {
                }.getType())
                .get(MyStores.CATEGORIES)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        categories -> {
                            categoriesList.clear();
                            categoriesList.addAll((Collection<? extends Category>) categories);
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        },
                        t -> ((Throwable) t).printStackTrace()
                );
    }
}
