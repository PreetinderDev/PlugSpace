package com.plugspace.model

data class AutoChatModel(
    val message: List<Message>,
    val success: Boolean
) {
    data class Message(
        val created_at: String,
        val id: Int,
        val message: String,
        val updated_at: Any
    )
}