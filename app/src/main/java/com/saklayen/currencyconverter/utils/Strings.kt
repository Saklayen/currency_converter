package com.saklayen.currencyconverter.utils

val String.Companion.BACK_SLASH: String
    get() = "/"

val String.Companion.EMPTY: String
    get() = ""

val String.Companion.NEW_LINE: String
    get() = "\n"

val String.Companion.CLONE: String
    get() = ":"

val String.Companion.SPACE: String
    get() = " "

val String.Companion.COMMA: String
    get() = ", "

val String.BEARER_TOKEN: String
    get() = "Bearer $this"

fun isNotEmptyOrNull(data: String?): Boolean = !data.isNullOrEmpty()

fun String?.isValidEmailAddress(): Boolean =
    !this.isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Int.toPercentageValue(value: Int): Int {
    if (this == 0) return 0
    if (value == 0) return 0
    return this.times(100).div(value)
}

val Int.Companion.INVALID_ID: Int
    get() = MIN_VALUE
