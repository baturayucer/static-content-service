package com.baturayucer.imageservice.core.impl

import com.baturayucer.imageservice.ImageServiceTestHelper.generateImage
import com.baturayucer.imageservice.core.ImageStorageProvider
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class OptimizedImageHandlerTest {

    @Mock
    private lateinit var storageProvider: ImageStorageProvider

    @Mock
    private lateinit var originalImageHandler: OriginalImageHandler

    @InjectMocks
    private lateinit var optimizedImageHandler: OptimizedImageHandler

    @Test
    fun `handle - should return an image if image is found by predefinedImageType and reference`() {
        //Given
        val imageType = PredefinedImageType.THUMBNAIL
        val reference = "baturay.png"
        val path = "/thumbnail/batu/baturay.png"

        whenever(storageProvider.retrieveImage(path))
            .thenReturn(generateImage())

        //When
        val image = optimizedImageHandler.handle(imageType, reference)

        //Then
        assertThat(image).isNotNull
        verify(storageProvider).retrieveImage(path)
    }

    @Test
    fun `handle - should find original image and optimize it when there is no optimized image`() {
        //Given
        val imageType = PredefinedImageType.THUMBNAIL
        val reference = "baturay.png"
        val path = "/thumbnail/batu/baturay.png"

        whenever(storageProvider.retrieveImage(path))
            .thenReturn(null)
        whenever(originalImageHandler.handle(PredefinedImageType.ORIGINAL, reference))
            .thenReturn(generateImage())

        //When
        val image = optimizedImageHandler.handle(imageType, reference)

        //Then
        assertThat(image).isNotNull
        verify(storageProvider).retrieveImage(path)
    }

    @Test
    fun `flush - should delete only optimized image when optimized image is requested to delete`() {
        //Given
        val imageType = PredefinedImageType.THUMBNAIL
        val reference = "baturay.png"
        val path = "/thumbnail/batu/baturay.png"

        //When
        optimizedImageHandler.flush(imageType, reference)

        //Then
        verify(storageProvider).deleteImage(path)
    }

}