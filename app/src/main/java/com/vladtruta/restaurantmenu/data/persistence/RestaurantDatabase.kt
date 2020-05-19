package com.vladtruta.restaurantmenu.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vladtruta.restaurantmenu.data.model.local.*

@Database(
    entities = [Category::class, MenuCourse::class, CartItem::class, Customer::class, OrderedItem::class],
    version = 8,
    exportSchema = false
)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract val restaurantDao: RestaurantDao
}

private lateinit var instance: RestaurantDatabase

fun getDatabase(context: Context): RestaurantDatabase {
    synchronized(RestaurantDatabase::class) {
        if (!::instance.isInitialized) {
            instance = Room
                .databaseBuilder(
                    context.applicationContext,
                    RestaurantDatabase::class.java,
                    "restaurant_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return instance
}