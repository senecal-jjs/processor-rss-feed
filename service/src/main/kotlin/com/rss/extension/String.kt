package com.rss.extension

import java.util.*

fun String.toUuid(): UUID = UUID.fromString(this)