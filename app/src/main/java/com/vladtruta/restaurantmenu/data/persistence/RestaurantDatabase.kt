package com.vladtruta.restaurantmenu.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse

@Database(
    entities = [Category::class, MenuCourse::class, CartItem::class],
    version = 1,
    exportSchema = false
)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract val restaurantDao: RestaurantDao
}

private lateinit var INSTANCE: RestaurantDatabase

fun getDatabase(context: Context): RestaurantDatabase {
    synchronized(RestaurantDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    RestaurantDatabase::class.java,
                    "restaurant_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}