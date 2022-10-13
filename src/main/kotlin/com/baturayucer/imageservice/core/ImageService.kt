package com.baturayucer.imageservice.core

import com.baturayucer.imageservice.core.enums.PredefinedImageType

interface ImageService {
    fun getImage(
        predefinedImageType: PredefinedImageType,
        reference: String
    ): ByteArray

    fun deleteImage(
        predefinedImageType: PredefinedImageType,
        reference: String
    )
}