@file:Suppress("unused")

package ga.forntoh.bableschool

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tripl3dev.prettystates.StatesConfigFactory
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.*
import ga.forntoh.bableschool.data.network.*
import ga.forntoh.bableschool.data.repository.*
import ga.forntoh.bableschool.ui.category.CategoryViewModelFactory
import ga.forntoh.bableschool.ui.category.courseNotes.CourseNotesViewModelFactory
import ga.forntoh.bableschool.ui.category.news.NewsViewModelFactory
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModelFactory
import ga.forntoh.bableschool.ui.category.profile.score.ScoreViewModelFactory
import ga.forntoh.bableschool.ui.category.profile.timeTable.TimeTableViewModelFactory
import ga.forntoh.bableschool.ui.category.ranking.RankingViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App : MultiDexApplication(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@App))

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApiService(instance()) }

        bind() from singleton { AppDatabase(instance()) }

        bind<BableSchoolDataSource>() with singleton { BableSchoolDataSourceImpl(instance()) }

        bind() from singleton { AppStorage(instance()) }

        bind<ProfileRepository>() with singleton { ProfileRepositoryImpl(instance(), instance(), instance()) }
        bind<ProfileViewModelFactory>() with provider { ProfileViewModelFactory(instance()) }

        bind<CategoryDao>() with singleton { instance<AppDatabase>().categoryDao() }
        bind<CategoryRepository>() with singleton { CategoryRepositoryImpl(instance(), instance(), instance()) }
        bind<CategoryViewModelFactory>() with provider { CategoryViewModelFactory(instance(), instance()) }

        bind<NewsDao>() with singleton { instance<AppDatabase>().newsDao() }
        bind<NewsRepository>() with singleton { NewsRepositoryImpl(instance(), instance(), instance()) }
        bind<NewsViewModelFactory>() with provider { NewsViewModelFactory(instance()) }

        bind<RankingDao>() with singleton { instance<AppDatabase>().rankingDao() }
        bind<RankingRepository>() with singleton { RankingRepositoryImpl(instance(), instance(), instance()) }
        bind<RankingViewModelFactory>() with provider { RankingViewModelFactory(instance()) }

        bind<CourseNoteDao>() with singleton { instance<AppDatabase>().courseNoteDao() }
        bind<CourseNoteRepository>() with singleton { CourseNoteRepositoryImpl(instance(), instance(), instance()) }
        bind<CourseNotesViewModelFactory>() with provider { CourseNotesViewModelFactory(instance()) }

        bind<PeriodDao>() with singleton { instance<AppDatabase>().periodDao() }
        //bind<PeriodRepository>() with singleton { PeriodRepositoryImpl2() }
        bind<PeriodRepository>() with singleton { PeriodRepositoryImpl(instance(), instance(), instance()) }
        bind<TimeTableViewModelFactory>() with provider { TimeTableViewModelFactory(instance()) }

        bind<ScoreDao>() with singleton { instance<AppDatabase>().scoreDao() }
        bind<ScoreRepository>() with singleton { ScoreRepositoryImpl(instance(), instance(), instance()) }
        bind<ScoreViewModelFactory>() with provider { ScoreViewModelFactory(instance()) }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseMessaging.getInstance().subscribeToTopic("global")
        AndroidThreeTen.init(this)
        StatesConfigFactory.intialize().initViews()
                .setDefaultEmptyLayout(R.layout.state_empty)
                .setDefaultLoadingLayout(R.layout.state_loading)
    }
}
