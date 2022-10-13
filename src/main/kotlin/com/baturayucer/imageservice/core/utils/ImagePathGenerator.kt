package com.baturayucer.imageservice.core.utils

import com.baturayucer.imageservice.core.enums.PredefinedImageType

object ImagePathGenerator {

    fun generatePath(
        predefinedImageType: PredefinedImageType,
        fileName: String
    ): String {
        val fileNameEscaped = fileName.escapeSlashes()
        val filenameWithoutExtension = fileNameEscaped.getFileNameWithoutExtension()
        val pathDetermined = determineImagePath(filenameWithoutExtension)
        return "/${predefinedImageType.value}$pathDetermined/$fileNameEscaped"
    }

    private fun String.escapeSlashes(): String =
        this.replace("/", "_")

    private fun String.getFileNameWithoutExtension(): String =
        ImageServiceValidator.validateFileNameExtension(this)
            .substring(0, this.lastIndexOf('.'))

    private fun String.getFirstAndSecondFourLetterPath(): String =
        "/${this.substring(0, 4)}/${this.substring(4, 8)}"

    private fun String.getFirstFourLetterPath(): String =
        "/${this.substring(0, 4)}"

    private fun determineImagePath(filenameWithoutExtension: String) = when {
        filenameWithoutExtension.length <= 4 -> "/$filenameWithoutExtension"
        filenameWithoutExtension.length <= 8 -> filenameWithoutExtension.getFirstFourLetterPath()
        else -> filenameWithoutExtension.getFirstAndSecondFourLetterPath()
    }

}