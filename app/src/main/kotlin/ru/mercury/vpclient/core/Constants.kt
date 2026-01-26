package ru.mercury.vpclient.core

const val FORMAT_RUB = "%s ₽"
const val FORMAT_BONUSES = "-%s ₽"
const val FORMAT_MASK = "**%s"
const val FORMAT_PLUS = "+%s"
const val FORMAT_HH_MM = "HH:mm"
const val FORMAT_D_MMMM_HH_MM = "d MMMM, HH:mm"
const val FORMAT_MM_SS = "%02d:%02d"

const val NOTIFICATION_TAG_CLOSE_TRIP = "close trip notification tag %s"

const val PREFIX_RUB = " ₽"
const val PREFIX_SPACE = " "

const val PAYMENT_VALUE_MAX_LENGTH = 13
const val PAYMENT_STATUS_DELAY = 3_000L

const val FITTING_MAX_TIME = 3_600_000
const val FITTING_TIMER_DELAY = 60_000L

const val PIN_MAX_TIME = 60_000L
const val PIN_TIMER_DELAY = 1_000L

const val DELIVERY_PRODUCT_CARD_COUNT = 4

const val TAG_CLICKABLE_PHONE = "clickablePhone"
val REGEX_PHONE = Regex("""(\+7|8|7)[\s\u00A0\u202F\-\(\)]*\d(?:[\s\u00A0\u202F\-\(\)]*\d){9}""")

const val FEATURE_ENABLED_SINGLE_SALE = false
