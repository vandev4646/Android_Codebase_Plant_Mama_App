package com.nipunapps.alarmmanagerblog

//Methods that can be performed on the Alarm. Maybe I could add edit?
interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}