package util

import kotlin.math.pow

fun BigDeciToString(value: String): String {
	try {
		val (base, exponent) = value.split("E")
		val result = (base.toDouble() * 10F.pow(exponent.toInt())).toInt()
		return result.toString()
	}
	catch(ex: Exception) {
		return value.replace(".0", "")
	}
}