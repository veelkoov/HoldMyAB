package vlkv.json.record

import kotlinx.serialization.Serializable
import vlkv.json.record.author.CustomFields
import vlkv.json.record.author.Group
import vlkv.json.record.author.Rank

@Serializable
data class Author(
    val achievements_points: Int,
    val birthday: String?,
    val coverPhotoUrl: String,
    val customFields: CustomFields,
    val email: String,
    val formattedName: String,
    val id: Int,
    val joined: String,
    val lastActivity: String?,
    val lastPost: String,
    val lastVisit: String?,
    val name: String,
    val photoUrl: String,
    val photoUrlIsDefault: Boolean,
    val posts: Int,
    val primaryGroup: Group,
    val profileUrl: String,
    val profileViews: Int,
    val rank: Rank?,
    val registrationIpAddress: String,
    val reputationPoints: Int,
    val secondaryGroups: List<Group>,
    val timeZone: String,
    val title: String?,
    val validating: Boolean,
    val warningPoints: Int
)
