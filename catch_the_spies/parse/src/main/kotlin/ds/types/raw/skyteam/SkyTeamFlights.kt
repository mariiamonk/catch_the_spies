package ds.types.raw.skyteam

import com.fasterxml.jackson.annotation.JsonProperty

data class SkyTeamFlights(
    val dates: Map<String, Map<String, Flight>>
)

data class Flight(
    @param:JsonProperty("FF")
    val frequentFlyers: Map<String, FrequentFlyer>,
    @param:JsonProperty("FROM")
    val from: String,
    @param:JsonProperty("STATUS")
    val status: String,
    @param:JsonProperty("TO")
    val to: String
)

data class FrequentFlyer(
    @param:JsonProperty("CLASS")
    val cls: String,
    @param:JsonProperty("FARE")
    val fare: String
)