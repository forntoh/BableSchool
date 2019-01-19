package ga.forntoh.bableschool.data.model

data class User(
        var username: String? = null,
        var classe: String? = null,
        var picture: String? = null,
        var profileData: LinkedHashMap<String, String>? = null
)