package vlkv.json

import kotlinx.serialization.Serializable

@Serializable
data class RecordsPage(
    val page: Int,
    val perPage: Int,
    val results: List<Record>,
    val totalPages: Int,
    val totalResults: Int
)
