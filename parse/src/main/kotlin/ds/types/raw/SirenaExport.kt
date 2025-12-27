package ds.types.raw

import ds.utils.db.Table
import java.time.LocalDate
import java.time.LocalTime

@Table("airlines.sirena_export")
data class SirenaExport(
    val paxName: String,
    val paxBirthData: LocalDate?,
//    val departureTime: OffsetDateTime,
//    val arrivalTime: OffsetDateTime,
    val departureDate: LocalDate,
    val departureTime: LocalTime,
    val arrivalDate: LocalDate,
    val arrivalTime: LocalTime,
    val flight: String,
    val codeSh: Boolean,
    val from: String,
    val dest: String,
    val code: String,
    val eticket: String,
    val travelDoc: String,
    val seat: String,
    val meal: String,
    val trvCls: String,
    val fare: String,
    val baggage: String,
    val paxAdditionalInfo: String,
    val agentInfo: String,
)
