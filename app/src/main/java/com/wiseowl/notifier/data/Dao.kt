package com.wiseowl.notifier.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wiseowl.notifier.data.entity.RuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(ruleEntity: RuleEntity)

    @Query("DELETE FROM rules_table WHERE id=:id")
    suspend fun deleteRule(id: Int)

    @Query("SELECT * FROM rules_table WHERE id=:id")
    suspend fun getRuleById(id: Int): RuleEntity?

    @Query("SELECT * FROM rules_table")
    fun getRules(): Flow<List<RuleEntity>>
}