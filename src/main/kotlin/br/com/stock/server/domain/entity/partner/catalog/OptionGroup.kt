package br.com.stock.server.domain.entity.partner.catalog

import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "OPTION_GROUP")
class OptionGroup(

    @Column(name = "NAME")
    var name: String,

    @Column(name = "MIN")
    val min: Int = 0,

    @Column(name = "MAX")
    val max: Int = 1,

    @Column(name = "SEQUENCE")
    val sequence: Int,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: OptionGroupStatus = OptionGroupStatus.AVAILABLE

    @OneToMany(mappedBy = "group")
    val options: List<Option> = emptyList()

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = createdAt
}

enum class OptionGroupStatus {
    AVAILABLE,
    PAUSED,
    DELETED,
}