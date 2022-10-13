package com.baturayucer.imageservice.core

import com.baturayucer.imageservice.core.enums.PredefinedImageType

interface ImageHandler {

    fun handle(
        predefinedImageType: PredefinedImageType,
        reference: String
    ): ByteArray

    fun flush(
        predefinedImageType: PredefinedImageType,
        reference: String
    )
}