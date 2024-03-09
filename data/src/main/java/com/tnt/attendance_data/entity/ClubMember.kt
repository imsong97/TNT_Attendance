package com.tnt.attendance_data.entity

import com.google.gson.annotations.SerializedName

data class ClubMember(
    @SerializedName("name")
    val name: String,
    @SerializedName("position")
    private val _position: Int,
    @SerializedName("phoneNo")
    val phoneNumber: String?,
    @SerializedName("isExecutive")
    val isExecutive: Boolean = false,
    @SerializedName("isDormancy")
    val isDormancy: Boolean = false
) {
    val position: POSITION
        get() = when(_position) {
            0 -> POSITION.GUEST
            1 -> POSITION.MEMBER
            10 -> POSITION.CHAIRMAN
            11 -> POSITION.DIRECTOR
            12 -> POSITION.MARKETER
            13 -> POSITION.MANAGER
            14 -> POSITION.CLERK
            else -> POSITION.MEMBER
        }
    enum class POSITION {
        // 회장, 경기이사, 홍보, 서기, 총무, 정회원, 임시회원
        CHAIRMAN, DIRECTOR, MARKETER, CLERK, MANAGER, MEMBER, GUEST
    }
}
