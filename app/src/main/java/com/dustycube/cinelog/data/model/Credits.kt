package com.dustycube.cinelog.data.model

data class Credits(
    val crew: List<CrewMember>
)

data class CrewMember(
    val name: String,
    val job: String
)