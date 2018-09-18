package ga.forntoh.bableschool.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.text.SimpleDateFormat;

public class GsonDeserializeExclusion implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaredClass() == SimpleDateFormat.class;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
