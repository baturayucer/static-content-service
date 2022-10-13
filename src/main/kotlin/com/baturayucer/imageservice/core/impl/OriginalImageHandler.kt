package com.baturayucer.imageservice.core.impl

import com.baturayucer.imageservice.core.ImageHandler
import com.baturayucer.imageservice.core.ImageStorageProvider
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import com.baturayucer.imageservice.core.exception.NotFoundException
import com.baturayucer.imageservice.core.utils.ImagePathGenerator
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths


@Service
class OriginalImageHandler(
    private val storageProvider: ImageStorageProvider
) : ImageHandler {

    @Value("\${image-service.resource-path:src/main/resources/images}")
    private val resourcePath: String = "src/main/resources/images"

    override fun handle(
        predefinedImageType: PredefinedImageType,
        reference: String
    ): ByteArray {
        val imagePath = ImagePathGenerator.generatePath(predefinedImageType, reference)
        return storageProvider.retrieveImage(imagePath)
            ?: kotlin.run {
                logger.info { "No original image found in s3, downloading image from server..." }
                downloadFromServerAndPublishToS3(imagePath, reference)
            }
            ?: throw NotFoundException("Image not found with reference: $reference")
    }

    override fun flush(predefinedImageType: PredefinedImageType, reference: String) =
        PredefinedImageType.values().forEach {
            storageProvider.deleteImage(ImagePathGenerator.generatePath(it, reference))
        }

    private fun downloadFromServerAndPublishToS3(imagePath: String, reference: String): ByteArray? =
        downloadImage(reference).also {
            it?.let { image ->
                storageProvider.saveImage(imagePath, image)
            }
        }

    //TODO: Better exception handling?
    private fun downloadImage(reference: String): ByteArray? =
        try {
            Files.readAllBytes(
                Paths.get("$resourcePath/$reference")
            )
        } catch (exception: Exception) {
            null
        }

    companion object: KLogging()
}