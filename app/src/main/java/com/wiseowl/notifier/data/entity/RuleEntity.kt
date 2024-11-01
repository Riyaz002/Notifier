package com.wiseowl.notifier.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.Rule

@Entity(tableName = "rules_table")
data class RuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val location: Location,
    val radiusInMeter: Int,
    val active: Boolean,
    val actionType: ActionType,
    val delayInMinutes: Int
){
    companion object{
        fun Rule.toRuleEntity() = RuleEntity(
            title = title,
            description = description,
            location = location,
            radiusInMeter = radiusInMeter,
            active = active,
            actionType = actionType,
            delayInMinutes = delayInMinutes
        )

        fun RuleEntity.toRule() = Rule(
            id = id,
            title = title,
            description = description,
            location = location,
            radiusInMeter = radiusInMeter,
            active = active,
            actionType = actionType,
            delayInMinutes = delayInMinutes
        )
    }
}