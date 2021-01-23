package com.rss.extension

import java.time.ZoneOffset
import java.util.Date

fun Date.toOffsetDateTime() = this.toInstant().atOffset(ZoneOffset.UTC)