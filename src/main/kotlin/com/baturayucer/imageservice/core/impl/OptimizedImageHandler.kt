package com.baturayucer.imageservice.core.impl

import com.baturayucer.imageservice.core.ImageHandler
import com.baturayucer.imageservice.core.ImageStorageProvider
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import com.baturayucer.imageservice.core.utils.ImagePathGenerator
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class OptimizedImageHandler(
    private val storageProvider: ImageStorageProvider,
    private val originalImageService: OriginalImageHandler
) : ImageHandler {

    override fun handle(
        predefinedImageType: PredefinedImageType,
        reference: String
    ): ByteArray {
        val optimizedImagePath = ImagePathGenerator.generatePath(predefinedImageType, reference)
        return storageProvider.retrieveImage(optimizedImagePath)
            ?: kotlin.run {
                logger.info { "No image found in s3, looking for original image..." }
                getOriginalImageAndOptimize(
                    reference = reference,
                    optimizedImagePath = optimizedImagePath,
                    expectedType = predefinedImageType
                )
            }
    }

    override fun flush(
        predefinedImageType: PredefinedImageType,
        reference: String
    ) = storageProvider.deleteImage(
        path = ImagePathGenerator.generatePath(predefinedImageType, reference)
    )

    private fun getOriginalImageAndOptimize(
        reference: String,
        optimizedImagePath: String,
        expectedType: PredefinedImageType
    ): ByteArray {
        val originalImage = getOriginalImage(reference)
        val optimizedImage = optimizeImage(originalImage, expectedType)
        storageProvider.saveImage(optimizedImagePath, optimizedImage)
        return optimizedImage
    }

    private fun getOriginalImage(reference: String) =
        originalImageService.handle(PredefinedImageType.ORIGINAL, reference)
            .also {
                logger.info { "Original image fetched." }
            }

    //TODO: optimize image
    private fun optimizeImage(
        originalImage: ByteArray,
        expectedType: PredefinedImageType
    ) = originalImage.also {
        logger.info { "Original image fetched Optimized with $expectedType" }
    }

    companion object : KLogging()
}