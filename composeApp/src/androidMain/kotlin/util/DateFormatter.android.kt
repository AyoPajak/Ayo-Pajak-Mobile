package util

import java.text.SimpleDateFormat
import java.util.*

actual fun DateFormatter(dateString: String): String {
	return try {
		// Define the input format (assuming the dateString is in yyyy-MM-dd or other known format)
		val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
		val date = inputFormat.parse(dateString)
		
		// Define the output format as dd-MM-yyyy
		val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
		date?.let { outputFormat.format(it) } ?: dateString
	} catch (e: Exception) {
		// In case of any error (invalid date format), return the original string
		dateString
	}
}