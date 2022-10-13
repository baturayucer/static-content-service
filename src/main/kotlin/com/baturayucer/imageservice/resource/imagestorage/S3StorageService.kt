package com.baturayucer.imageservice.resource.imagestorage

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import com.baturayucer.imageservice.core.ImageStorageProvider
import com.baturayucer.imageservice.core.enums.BucketName
import mu.KLogging
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream


@Service
class S3StorageService(
    private val amazonS3: AmazonS3
) : ImageStorageProvider {

    override fun retrieveImage(path: String): ByteArray? =
        getImageFromS3(path).also {
            logger.info { "Image retrieved successfully with path: $path" }
        }

    override fun saveImage(path: String, image: ByteArray) =
        putImageToS3(image, path)
            .also {
                logger.info { "Image is sent to s3 successfully with path: $path" }
            }

    override fun deleteImage(path: String) =
        deleteImageFromS3(path)
            .also {
                logger.info { "Image is deleted successfully from s3 with path: $path" }
            }

    private fun getImageFromS3(path: String): ByteArray? =
        handleS3CallAndReturn {
            val imageObject = amazonS3.getObject(BucketName.IMAGE_STORAGE.value, path)
            val objectContent = imageObject.objectContent
            IOUtils.toByteArray(objectContent)
        }

    private fun putImageToS3(image: ByteArray, path: String) =
        handleS3Call {
            val request = PutObjectRequest(
                BucketName.IMAGE_STORAGE.value,
                path,
                ByteArrayInputStream(image),
                ObjectMetadata()
            )
            amazonS3.putObject(request)
        }

    private fun deleteImageFromS3(path: String) =
        handleS3Call {
            amazonS3.deleteObject(BucketName.IMAGE_STORAGE.value, path)
        }

    private fun <T : Any> handleS3CallAndReturn(callBack: () -> T): T? {
        return callS3(callBack)
    }

    private fun <T : Any> handleS3Call(callBack: () -> T) {
        callS3(callBack)
    }

    private fun <T : Any> callS3(callBack: () -> T): T? {
        return try {
            callBack()
        } catch (e: AmazonServiceException) {
            logger.info { "Requested resource does not exist in image storage(s3)" }
            null
        } catch (e: IOException) {
            logger.warn { "IO Error occurred while calling image storage(s3)." }
            null
        }
    }

    companion object : KLogging()
}