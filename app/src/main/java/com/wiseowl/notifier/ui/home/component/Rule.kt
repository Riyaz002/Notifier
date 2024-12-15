package com.wiseowl.notifier.ui.home.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.Place
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.ui.home.model.HomeEvent

@Composable
@Preview
fun Rule(
    rule: Rule = Rule(1, "Title", "description", Place(name = "place name", location = Location(111.0,111.0)), 12, true, ActionType.LEAVING, 10),
    onEvent: (HomeEvent) -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(animation = tween(400), repeatMode = RepeatMode.Reverse),
        label = ""
    )
    var expandedState by remember{
        mutableStateOf(false)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onPrimary)
            .border(2.dp, color = MaterialTheme.colorScheme.primary)
            .height(80.dp)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier
            .scale(if (rule.active==true) scale else 1f)
            .size(10.dp)
            .background(
                color = if (rule.active==true) Color.Green else Color.Red,
                shape = RoundedCornerShape(20.dp)
            ))

        Column(modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxHeight()
            .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = rule.name, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold))
            rule.description?.let { Text(text = it) }
        }

        IconButton(
            onClick = { expandedState = true},
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "more"
            )
            DropdownMenu(
                expanded = expandedState,
                onDismissRequest = { expandedState = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        expandedState = false
                        onEvent(HomeEvent.DeleteRule(rule.id))
                    }
                )
            }
        }
    }
}