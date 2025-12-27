package ds.utils

private val regex = "(?<=.)[A-Z]".toRegex()

fun String.camelToSnakeCase(): String {
    val str = this.replace(regex, "_$0").lowercase()
    if (str[0] == '_')
        return str.substring(1)
    return str
}

// YULIYA [ boarding_pass ]
private val normalizationTable = mapOf<Char, String>(
    'А' to "A",  'Б' to "B",    'В' to "V",  'Г' to "G",  'Д' to "D",
    'Е' to "E",  'Ё' to "YO",   'Ж' to "ZH", 'З' to "Z",  'И' to "I",
    'Й' to "Y",  'К' to "K",    'Л' to "L",  'М' to "M",  'Н' to "N",
    'О' to "O",  'П' to "P",    'Р' to "R",  'С' to "S",  'Т' to "T",
    'У' to "U",  'Ф' to "F",    'Х' to "KH", 'Ц' to "TS", 'Ч' to "CH",
    'Ш' to "SH", 'Щ' to "SHCH", 'Ъ' to "",   'Ы' to "Y",  'Ь' to "",
    'Э' to "E",  'Ю' to "YU",   'Я' to "YA",

    '\'' to ""
) + "QWERTYUIOPASDFGHJKLZXCVBNM".associateWith { it }

// IULIIA  [ boarding_data ]
private val normalizationTableGOST = mapOf<Char, String>(
    'А' to "A",  'Б' to "B",    'В' to "V",  'Г' to "G",  'Д' to "D",
    'Е' to "E",  'Ё' to "E",    'Ж' to "ZH", 'З' to "Z",  'И' to "I",
    'Й' to "I",  'К' to "K",    'Л' to "L",  'М' to "M",  'Н' to "N",
    'О' to "O",  'П' to "P",    'Р' to "R",  'С' to "S",  'Т' to "T",
    'У' to "U",  'Ф' to "F",    'Х' to "KH", 'Ц' to "TS", 'Ч' to "CH",
    'Ш' to "SH", 'Щ' to "SHCH", 'Ъ' to "",   'Ы' to "Y",  'Ь' to "",
    'Э' to "E",  'Ю' to "IU",   'Я' to "IA",

    '\'' to ""
) + "QWERTYUIOPASDFGHJKLZXCVBNM".associateWith { it }

fun String.normalizeName(): String {
    return this.uppercase().map { normalizationTable[it] ?: "" }.joinToString("")
}

fun String.normalizeNameGOST(): String {
    return this.uppercase().map { normalizationTableGOST[it] ?: "" }.joinToString("")

}