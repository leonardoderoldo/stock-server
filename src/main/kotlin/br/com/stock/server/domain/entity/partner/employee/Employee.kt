package br.com.stock.server.domain.entity.partner.employee

import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity
@Table(name = "EMPLOYEE")
class Employee(

    @Column(name = "FIRST_NAME")
    var firstName: String,

    @Column(name = "LAST_NAME")
    var lastName: String,

    @Column(name = "CPF")
    val cpf: String,

    @Column(name = "EMAIL")
    val email: String,

    @Column(name = "BIRTH_DATE")
    var birthDate: LocalDate,

    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID")
    val restaurant: Restaurant,

    @Enumerated(EnumType.STRING)
    @Column(name = "JOB_FUNCTION")
    var jobFunction: EmployeeJobFunction,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: EmployeeStatus = EmployeeStatus.TO_ACTIVATE

    @Column(name = "IMAGE_PATH")
    var imagePath: String? = null

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()

    fun isActive() = this.status == EmployeeStatus.ACTIVE

}

enum class EmployeeStatus {
    ACTIVE,
    TO_ACTIVATE,
    INACTIVE,
}

enum class EmployeeJobFunction(
    val title: String,
    val description: String,
) {

    OWNER("Dono(a) da Loja", "Este é o perfil ideal para quem comanda uma loja, pois garante acesso a todas as informações e funcionalidades do Portal do Parceiro."),
    STORE_MANAGEMENT ("Gestão de loja", "Este é o perfil ideal para gerentes, ou pessoas que precisam acompanhar a operação da loja."),
    FINANCIAL("Financeiro", "Este é o perfil ideal para quem cuida do financeiro da loja. Este tipo de acesso não permite que a pessoa altere nenhuma informação da loja no Portal do Parceiro."),
    MARKETING("Marketing", "Acessa e edita o cardápio, avaliações e perfil do restaurante. Além disso, pode acompanhar a performance das avaliações, operações e promoções"),
    CASHIER( "Operador(a) de caixa", "Este é o perfil ideal para quem opera o caixa da loja. Este tipo de acesso não permite que a pessoa visualize dados sensíveis e nem altere informações da loja."),
}