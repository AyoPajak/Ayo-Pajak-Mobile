package util

import java.text.DecimalFormat

actual fun CurrencyFormatter(number: String): String {
	val numberLong = number.toLongOrNull() ?: return number // If conversion fails, return the original string
	val formatter = DecimalFormat("#,###")
	return formatter.format(numberLong)
}