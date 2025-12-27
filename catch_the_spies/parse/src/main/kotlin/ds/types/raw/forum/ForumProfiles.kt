package ds.types.raw.forum

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ForumProfiles(
    @param:JsonProperty("Forum Profiles")
    val profiles: List<ForumProfile>
)

data class ForumProfile(
    @param:JsonProperty("Registered Flights")
    val registeredFlights: List<Flight>,
    @param:JsonProperty("NickName")
    val nickname: String,
    @param:JsonProperty("Travel Documents")
    val travelDocuments: List<TravelDocument>,
    @param:JsonProperty("Sex")
    val sex: String?,
    @param:JsonProperty("Loyality Programm")
    val loyalityProgram: List<LoyalityProgram>,
    @param:JsonProperty("Real Name")
    val realName: RealName,
) {
    @JsonIgnore
    val id: UUID = UUID.randomUUID()
}

data class Flight(
    @param:JsonProperty("Date")
    val date: String,
    @param:JsonProperty("Codeshare")
    val codeshare: Boolean,
    @param:JsonProperty("Arrival")
    val arrival: Airport,
    @param:JsonProperty("Flight")
    val flight: String,
    @param:JsonProperty("Departure")
    val departure: Airport,
)

data class Airport(
    @param:JsonProperty("City")
    val city: String,
    @param:JsonProperty("Airport")
    val airport: String,
    @param:JsonProperty("Country")
    val country: String,
)

data class TravelDocument(
    @param:JsonProperty("Passports")
    val passports: Any?,
)

data class LoyalityProgram(
    @param:JsonProperty("Status")
    val status: String,
    @param:JsonProperty("programm")
    val program: String,
    @param:JsonProperty("Number")
    val type: String,
)

data class RealName(
    @param:JsonProperty("First Name")
    val firstName: String?,
    @param:JsonProperty("Last Name")
    val lastName: String?,
)