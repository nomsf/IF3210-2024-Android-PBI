import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "transactionentity")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "nominal")
    val nominal: String,

    @ColumnInfo(name = "kategori")
    val kategori: String,

    @ColumnInfo(name = "lokasi")
    val lokasi: String,

    @ColumnInfo(name = "tanggal")
    val tanggal: Date
)