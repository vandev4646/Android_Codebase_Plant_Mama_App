package com.nipunapps.alarmmanagerblog

import java.time.LocalDateTime

//Contians the items for the Alarm. I could add other items too like plant?
data class AlarmItem(
    val alarmTime : LocalDateTime,
    val message : String
)
