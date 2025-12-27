package ds.types.raw.pointz

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.util.UUID

@JacksonXmlRootElement(localName = "PointzAggregatorUsers")
data class PointzAggregatorUsers(
    @param:JsonProperty("user")
    @param:JacksonXmlElementWrapper(useWrapping = false)
    val users: List<PointzUser>
)

@JacksonXmlRootElement(localName = "user")
data class PointzUser(
    @param:JacksonXmlProperty(isAttribute = true, localName = "uid")
    val uid: Long,
    @param:JsonProperty("name")
    val name: PointzName,
    @param:JsonProperty("cards")
    val cards: PointzCards?
) {
    @JsonIgnore
    val id = UUID.randomUUID()
}

@JacksonXmlRootElement(localName = "name")
data class PointzName(
    @param:JacksonXmlProperty(isAttribute = true, localName = "first")
    val first: String,
    @param:JacksonXmlProperty(isAttribute = true, localName = "last")
    val last: String
)

@JacksonXmlRootElement(localName = "cards")
data class PointzCards(
    @param:JacksonXmlProperty(isAttribute = true, localName = "type")
    val type: String,
    @param:JsonProperty("card")
    @param:JacksonXmlElementWrapper(useWrapping = false)
    @param:JsonSetter(nulls = Nulls.SKIP)
    val cards: List<PointzCard> = emptyList()
)

@JacksonXmlRootElement(localName = "card")
data class PointzCard(
    @param:JacksonXmlProperty(isAttribute = true, localName = "number")
    val number: String,
    @param:JsonProperty("bonusprogramm")
    val bonusProgram: String,
    @param:JsonProperty("activities")
    val activities: PointzActivities?
)

@JacksonXmlRootElement(localName = "activities")
data class PointzActivities(
    @param:JacksonXmlProperty(isAttribute = true, localName = "type")
    val type: String,
    @param:JsonProperty("activity")
    @param:JacksonXmlElementWrapper(useWrapping = false)
    @param:JsonSetter(nulls = Nulls.SKIP)
    val activities: List<PointzActivity> = emptyList()
)

@JacksonXmlRootElement(localName = "activity")
data class PointzActivity(
    @param:JacksonXmlProperty(isAttribute = true, localName = "type")
    val type: String,
    @param:JsonProperty("Code")
    val code: String,
    @param:JsonProperty("Date")
    val date: String,
    @param:JsonProperty("Departure")
    val departure: String,
    @param:JsonProperty("Arrival")
    val arrival: String,
    @param:JsonProperty("Fare")
    val fare: String
)