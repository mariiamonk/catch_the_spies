package ds.types.custom

import ds.utils.normalizeName

data class Name(
    var first: String,
    var last: String,
    val second: String? = null
) {
    companion object {
        fun fromLFS(str: String, normalization: String.() -> String = String::normalizeName): Name {
            val split = str.trim().split(" ").map { it.normalization() }
            if (split.size == 2) {
                return Name(last = split[0], first = split[1])
            } else if (split.size == 3) {
                return Name(last = split[0], first = split[1], second = split[2])
            } else throw IllegalArgumentException("Name \"$str\" is not a valid name")
        }

    }

    override fun toString(): String {
        return "$last $first $second"
    }
}
