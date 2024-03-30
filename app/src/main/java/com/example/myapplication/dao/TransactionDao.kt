import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete


@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Query("SELECT * FROM transactionentity")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Update
    fun update(transaction: TransactionEntity)

    @Delete
    fun delete(transaction: TransactionEntity)
}