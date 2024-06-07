package br.com.stock.server.domain.entity.backoffice

import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

class BackofficeUser(
    @Column(name = "FIRST_NAME")
    val fisrtName: String,

    @Column(name = "LAST_NAME")
    val lastName: String,

    @Column(name = "CPF")
    val cpf: String,

    @Column(name = "EMAIL")
    val email: String,
) {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    var updatedAt: OffsetDateTime = OffsetDateTime.now()

    @Column(name = "DELETED_AT")
    var deletedAt: OffsetDateTime? = null
}