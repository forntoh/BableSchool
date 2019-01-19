package ga.forntoh.bableschool.data.model.main

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.structure.BaseModel
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Course(
        @PrimaryKey var code: String? = null,
        @Column var title: String? = null
) : BaseModel() {

    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var videos by oneToMany { select from Video::class where (Video_Table.courseCode.eq(code)) }

    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var documents by oneToMany { select from Document::class where (Document_Table.courseCode.eq(code)) }

    private var docs: List<Document>? = null

    private var vids: List<Video>? = null

    override fun save(): Boolean {
        val res = super.save()
        if (!docs.isNullOrEmpty()) docs!!.forEach { it.courseCode = code; it.save() }
        if (!vids.isNullOrEmpty()) vids!!.forEach { it.courseCode = code; it.save() }
        return res
    }

    val abbr: String
        get() {
            val words = this.title!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var title = ""
            return if (words.size > 1) {
                for (s in words) title += s[0]; title
            } else this.title!!.substring(0, 3)
        }
}