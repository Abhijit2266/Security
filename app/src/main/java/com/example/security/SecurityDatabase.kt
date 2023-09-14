package com.example.security

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class],version=1,exportSchema = false)
public abstract class SecurityDatabase:RoomDatabase() {

    abstract fun contactDao(): ContactDao



    companion object{
        @Volatile
        private var INSTANCE:SecurityDatabase? = null


        }
        fun getDatabase(context:Context): SecurityDatabase {

            INSTANCE?.let {
                return it
            }
            return synchronized(SecurityDatabase::class.java){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SecurityDatabase::class.java,
                    "SecurityDatabase"
                ).build()

                INSTANCE = instance
                instance

            }


        }

    }
}