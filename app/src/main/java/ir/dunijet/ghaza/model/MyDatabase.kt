package ir.dunijet.ghaza.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Food::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract val foodDao: FoodDao

    companion object {

        private var dataBase: MyDatabase? = null
        fun getDatabase(context: Context): MyDatabase {
            if (dataBase == null) {
                dataBase = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "myDatabase.db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return dataBase!!
        }

    }

}