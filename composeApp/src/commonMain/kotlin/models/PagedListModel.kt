package models

import kotlinx.serialization.Serializable

@Serializable
data class PagedListModel<T> (
	val Count: Long? = null,
	val NextPageLink: String? = null,
	val Items: List<T>? = listOf()
)