package ga.forntoh.bableschool.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

import java.text.SimpleDateFormat

class GsonDeserializeExclusion : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean = f.declaredClass == SimpleDateFormat::class.java

    override fun shouldSkipClass(clazz: Class<*>): Boolean = false
}
