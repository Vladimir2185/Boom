package com.practicum.boom.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.boom.api.Product
import com.practicum.boom.api.ShopInfo


@Database(entities = [Product::class, ShopInfo::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private var db: AppDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `shopInfo_list` " +
                        "(`id` TEXT NOT NULL, `title` TEXT NOT NULL,`shortDescription` TEXT NOT NULL," +
                        "`longDescription` TEXT NOT NULL,`url` TEXT NOT NULL, " + "PRIMARY KEY(`id`))")
            }
        }


        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_NAME
                    ).addMigrations(MIGRATION_1_2)
                        .build()
                db = instance
                return instance
            }
        }
    }


    abstract fun productInfoDao(): ProductDao
}
