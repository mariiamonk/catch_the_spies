package ds.types.raw

import ds.utils.db.Table

@Table("airlines.boarding_pass")
data class BoardingPass(
    val name: String,
    val flight: String,
    val from: String,
    val to: String,
    val fromCode: String,
    val toCode: String,
    val date: java.time.LocalDate?,
    val time: java.time.LocalTime?,
    val operatedBy: String,
    val seat: String?,
    val pnr: String,
    val etiket: String,
    val sequence: Int
)