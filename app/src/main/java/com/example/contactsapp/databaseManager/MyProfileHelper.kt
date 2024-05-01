import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyProfileHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        const val table_pos = "Profile"
        const val col_id = "Id"
        const val col_last_name = "LastName"
        const val col_first_name = "FirstName"
        const val col_num = "Num"
        const val isFavorite = "isFavorite"
    }

    private val req = "CREATE TABLE IF NOT EXISTS $table_pos(" +
            "$col_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$col_last_name TEXT NOT NULL," +
            "$col_first_name TEXT NOT NULL," +
            "$col_num TEXT NOT NULL," +
            "$isFavorite INTEGER NOT NULL" +
            ")"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(req)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $table_pos")
        onCreate(db)
    }
}
