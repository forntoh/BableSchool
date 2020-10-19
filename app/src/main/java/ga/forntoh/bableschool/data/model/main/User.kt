package ga.forntoh.bableschool.data.model.main

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class User(
        var username: String? = null,
        var classe: String? = null,
        var picture: String? = null,
        var profileData: ProfileData = ProfileData()
) {

    fun profileDataMap(): LinkedHashMap<String, String> = ObjectMapper().readValue(
            Gson().toJson(profileData),
            object : TypeReference<Map<String, String>>() {}
    ) as LinkedHashMap<String, String>

    data class ProfileData(
            @SerializedName("Class")
            var clazz: String = "",
            @SerializedName("Full name")
            var fullName: String = "",
            @SerializedName("Matriculation")
            var matriculation: String = "",
            @SerializedName("Password")
            var password: String = "",
            @SerializedName("Phone")
            var phone: String = "",
            @SerializedName("School")
            var school: String = ""
    )
}