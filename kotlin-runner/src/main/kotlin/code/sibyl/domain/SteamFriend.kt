package code.sibyl.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant
import java.time.LocalDateTime

@Entity
@org.springframework.data.relational.core.mapping.Table("t_biz_steam_friend")
open class SteamFriend {

    @Id
    @org.springframework.data.annotation.Id
    open var id: Long? = null

    open var steamId: String? = null

    open var streamFriendId: String? = null

    open var isDeleted: String? = "0"

    open var friendSince: LocalDateTime? = null

    open var relationship: String? = null
}