package com.delixha.aplikasigithubuser.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.TABLE_NAME
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion._ID
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.USERNAME
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.NAME
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.LOCATION
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.REPOSITORY
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.COMPANY
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.AVATAR_URL

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbgithubapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_GITHUB = "CREATE TABLE $TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $USERNAME TEXT NOT NULL," +
                " $NAME TEXT NOT NULL," +
                " $LOCATION TEXT NOT NULL," +
                " $REPOSITORY INTEGER," +
                " $COMPANY TEXT NOT NULL," +
                " $AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_GITHUB)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}