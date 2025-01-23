package ir.dunijet.ghaza.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_food")
data class Food(

    @PrimaryKey(autoGenerate = true)
    val id :Int? = null ,

    val txtSubject :String ,
    val txtPrice :String ,
    val txtDistance :String ,
    val txtCity :String ,

//    @ColumnInfo(name = "url")
    val urlImage :String ,

    val numOfRating :Int ,
    val rating :Float

)