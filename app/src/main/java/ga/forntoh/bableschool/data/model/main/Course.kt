package ga.forntoh.bableschool.data.model.main

import com.dbflow5.annotation.*
import com.dbflow5.database.DatabaseWrapper
import com.dbflow5.query.select
import com.dbflow5.structure.BaseModel
import com.dbflow5.structure.oneToMany
import com.dbflow5.structure.save
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class, allFields = false)
data class Course(
        @PrimaryKey var code: String? = null,
        @Column var title: String? = null
) : BaseModel() {

    @get:OneToMany(oneToManyMethods = [OneToManyMethod.ALL])
    var videos by oneToMany { select from Video::class where (Video_Table.courseCode.eq(code)) }

    @get:OneToMany(oneToManyMethods = [OneToManyMethod.ALL])
    var documents by oneToMany { select from Document::class where (Document_Table.courseCode.eq(code)) }

    private var docs: List<Document>? = null

    private var vids: List<Video>? = null

    override fun save(wrapper: DatabaseWrapper): Boolean {
        val res = super.save(wrapper)
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