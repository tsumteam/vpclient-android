package ru.mercury.vpclient.core.ktx

val Int?.orEmpty: Int
    get() = this ?: 0

val Int?.isNotEmpty: Boolean
    get() = this != null && this != 0

val Long?.isNotEmpty: Boolean
    get() = this != null && this != 0L

val Float?.orEmpty: Float
    get() = this ?: 0F

val Float?.isNotEmpty: Boolean
    get() = this != null && this != 0F

val Double?.orEmpty: Double
    get() = this ?: 0.0

val Double?.isEmpty: Boolean
    get() = this == null || this == 0.0

val Double?.isNotEmpty: Boolean
    get() = this != null && this != 0.0

val Boolean?.orEmpty: Boolean
    get() = this ?: false

val String.isValidAmount: Boolean
    get() = toFloatOrNull().isNotEmpty
