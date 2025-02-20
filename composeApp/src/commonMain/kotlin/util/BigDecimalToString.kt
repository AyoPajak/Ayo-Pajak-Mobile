package util

import kotlin.math.pow

fun BigDeciToString(value: String): String {
	if(value == "null") return "0"
	
	try {
		val (base, exponent) = value.split("E")
		val result = (base.toDouble() * 10F.pow(exponent.toInt())).toInt()
		return result.toString()
	}
	catch(ex: Exception) {
		return value.replace(".0", "")
	}
}

fun BigDeciToLong(value: String): Long {
	if(value == "null") return 0L
	
	try {
		val (base, exponent) = value.split("E")
		val result = (base.toDouble() * 10F.pow(exponent.toInt())).toInt()
		return result.toLong()
	}
	catch(ex: Exception) {
		return value.replace(".0", "").toLong()
	}
}