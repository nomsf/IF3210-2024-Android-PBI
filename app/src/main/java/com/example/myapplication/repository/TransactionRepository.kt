import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Delete


class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: LiveData<List<TransactionEntity>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }
}