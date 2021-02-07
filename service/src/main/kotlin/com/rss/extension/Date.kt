package com.rss.extension

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Date

fun Date.toOffsetDateTime(): OffsetDateTime = this.toInstant().atOffset(ZoneOffset.UTC)