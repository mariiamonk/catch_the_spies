package ds.types.raw

import ds.utils.db.Table
import java.time.LocalDate
import java.time.LocalTime

@Table("airlines.boarding_data")
data class BoardingData(
    val passengerFirstName: String,
    val passengerSecondName: String,
    val passengerLastName: String,
    val passengerSex: String,
    val passengerBirthDate: LocalDate,
    val passengerDocument: String,
    val bookingCode: String,
    val ticketNumber: String,
    val baggage: String?,
    val flightDate: LocalDate,
    val flightTime: LocalTime,
    val flightNumber: String,
    val codeShare: String,
    val destination: String,
)

