package com.example.gif_search_app.data.model

import com.google.gson.annotations.SerializedName

data class GiphyResponse(
    @SerializedName("data")
    val data: List<GifObject>,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("meta")
    val meta: Meta
)

data class GifObject(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("images")
    val images: Images
)

data class Images(
    @SerializedName("original")
    val original: Original,
)

data class Original(
    @SerializedName("url")
    val url: String,
)

data class Pagination(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("offset")
    val offset: Int,
)

data class Meta(
    @SerializedName("status")
    val status: Int,
    @SerializedName("msg")
    val msg: String,
)