package com.baturayucer.imageservice.core.impl

import com.baturayucer.imageservice.core.ImageService
import com.baturayucer.imageservice.core.ImageRetrieveStrategy
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import com.baturayucer.imageservice.core.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class ImageServiceImpl(
    imageRetrieveStrategy: ImageRetrieveStrategy
) : ImageService {

    private val retrieveStrategies = imageRetrieveStrategy.strategies

    override fun getImage(
        predefinedImageType: PredefinedImageType,
        reference: String
    ): ByteArray = retrieveStrategies[predefinedImageType]?.handle(
        predefinedImageType,
        reference
    ) ?: throw NotFoundException("No image handle strategy found to retrieve for type: $predefinedImageType")

    override fun deleteImage(
        predefinedImageType: PredefinedImageType,
        reference: String
    ) = retrieveStrategies[predefinedImageType]?.flush(
        predefinedImageType,
        reference
    ) ?: throw NotFoundException("No image handle strategy found to delete for type: $predefinedImageType")

}