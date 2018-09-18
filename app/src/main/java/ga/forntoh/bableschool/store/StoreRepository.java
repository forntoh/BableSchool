package ga.forntoh.bableschool.store;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nytimes.android.external.fs3.SourcePersisterFactory;
import com.nytimes.android.external.store3.base.Persister;
import com.nytimes.android.external.store3.base.impl.BarCode;
import com.nytimes.android.external.store3.base.impl.ParsingStoreBuilder;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.middleware.GsonParserFactory;
import com.nytimes.android.external.store3.middleware.GsonTransformerFactory;

import java.io.IOException;
import java.lang.reflect.Type;

import ga.forntoh.bableschool.utils.GsonDeserializeExclusion;
import io.reactivex.Single;
import okio.BufferedSource;

public class StoreRepository<T> {

    private Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(new GsonDeserializeExclusion()).create();

    private Persister<BufferedSource, BarCode> persister;

    public StoreRepository(Application application) {
        try {
            this.persister = SourcePersisterFactory.create(application.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public Store<T, BarCode> create(Single<T> api, T type) {
        return ParsingStoreBuilder.<BufferedSource, T>builder()
                .fetcher(barCode -> api.compose(GsonTransformerFactory.createObjectToSourceTransformer(gson)))
                .parser(GsonParserFactory.createSourceParser(gson, (Type) type))
                .persister(persister)
                .open();
    }
}
