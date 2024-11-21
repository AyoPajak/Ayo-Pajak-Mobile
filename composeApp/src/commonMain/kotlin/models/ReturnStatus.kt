package models

open class ReturnStatus {
	var ErrorCode: Int = 0
	var IsSuccess: Boolean = true
	var Message: String? = null
	var Data: Any? = null
	
	fun SetError(message: String, errorCode: Int = 1) {
		IsSuccess = false
		Message = message
		ErrorCode = errorCode
		Data = null
	}
}