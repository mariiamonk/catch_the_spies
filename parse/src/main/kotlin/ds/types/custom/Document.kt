package ds.types.custom

data class Document(
    val ser: Int,
    val no: Int
) {
    companion object {
        fun from(string: String): Document {
            val split = string.split(" ")
            if (split.size != 2) {
                throw IllegalArgumentException("Illegal passport: $string")
            }
            return Document(split[0].toInt(), split[1].toInt())
        }
    }

    override fun toString(): String {
        return "$ser $no"
    }

    fun toNormalizedString(): String {
        return "${ser.toString().padStart(4, '0')} ${no.toString().padStart(6, '0')}"
    }
}