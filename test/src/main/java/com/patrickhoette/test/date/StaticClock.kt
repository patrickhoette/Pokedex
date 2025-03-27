@file:Suppress("unused")

package com.patrickhoette.test.date

import kotlinx.datetime.*

class StaticClock(
    private val instant: Instant,
) : Clock {

    constructor(
        localDateTime: LocalDateTime,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ) : this(localDateTime.toInstant(timeZone))

    constructor(
        localDate: LocalDate,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ) : this(localDate.atStartOfDayIn(timeZone))

    constructor(
        year: Int,
        month: Month,
        dayOfMonth: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanosecond: Int = 0,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ) : this(
        localDateTime = LocalDateTime(
            year = year,
            month = month,
            dayOfMonth = dayOfMonth,
            hour = hour,
            minute = minute,
            second = second,
            nanosecond = nanosecond
        ),
        timeZone = timeZone,
    )

    constructor(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanosecond: Int = 0,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ) : this(
        localDateTime = LocalDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hour = hour,
            minute = minute,
            second = second,
            nanosecond = nanosecond
        ),
        timeZone = timeZone,
    )

    override fun now(): Instant = instant
}
