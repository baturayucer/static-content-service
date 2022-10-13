package com.baturayucer.imageservice.core.utils

import com.baturayucer.imageservice.core.exception.ValidationException

object ImageServiceValidator {
    fun validateFileNameExtension(fileName: String): String =
        ALLOWED_EXTENSIONS.firstOrNull { fileName.endsWith(it) }
            ?.let { fileName }
            ?: throw ValidationException(
                "File name should end with following values: ${ALLOWED_EXTENSIONS.map { it }}"
            )

    private val ALLOWED_EXTENSIONS = listOf(
        ".jpg",
        ".jpeg",
        ".png",
        ".gif"
    )
}