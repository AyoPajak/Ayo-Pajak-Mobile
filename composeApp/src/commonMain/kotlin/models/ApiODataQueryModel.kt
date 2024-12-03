package models

data class ApiODataQueryModel(val _itemCountPerPage: Int = 70) {
	var PageNum: Int = 0
	var ItemCountPerPage: Int = _itemCountPerPage
	var OrderByList: MutableMap<String, String> = mutableMapOf()
	var LastId: Int? = null
	var FilterModel: ODataFilterModel? = null
	
	fun resetPage() {
		PageNum = 0
	}
}

class ODataFilterModel(
	var value: String,
	var fields: List<String>,
	var filterType: ODataFilterType = ODataFilterType.CONTAIN
)

enum class ODataQueryOrderDirection(val value: String) {
	ASCENDING("asc"),
	DESCENDING("desc")
}

enum class ODataFilterType(val value: String) {
	CONTAIN("contains"),
	EQUAL("eq")
}