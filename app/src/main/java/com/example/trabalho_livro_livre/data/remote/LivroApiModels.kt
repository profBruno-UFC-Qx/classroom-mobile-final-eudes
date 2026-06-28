package com.example.trabalho_livro_livre.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(val items: List<VolumeInfoWrapper>? = null)

@Serializable
data class VolumeInfoWrapper(val volumeInfo: VolumeInfo)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null
)
