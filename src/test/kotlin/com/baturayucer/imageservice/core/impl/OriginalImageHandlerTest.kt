package com.baturayucer.imageservice.core.impl

import com.baturayucer.imageservice.ImageServiceTestHelper.generateImage
import com.baturayucer.imageservice.core.ImageStorageProvider
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import com.baturayucer.imageservice.core.exception.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class OriginalImageHandlerTest {

    @Mock
    private lateinit var storageProvider: ImageStorageProvider

    @InjectMocks
    private lateinit var originalImageHandler: OriginalImageHandler

    @Test
    fun `handle - should get original image when image is there`() {
        //Given
        val imageType = PredefinedImageType.ORIGINAL
        val reference = "baturay.png"
        val path = "/original/batu/baturay.png"

        whenever(storageProvider.retrieveImage(path))
            .thenReturn(generateImage())

        //When
        val image = originalImageHandler.handle(imageType, reference)

        //Then
        assertThat(image).isNotNull
        verify(storageProvider).retrieveImage(path)
    }

    @Test
    fun `handle - should download original image from server when image is not in s3`() {
        //Given
        val imageType = PredefinedImageType.ORIGINAL
        val reference = "baturay.jpg"
        val path = "/original/batu/baturay.jpg"

        whenever(storageProvider.retrieveImage(path))
            .thenReturn(null)

        //When
        val image = originalImageHandler.handle(imageType, reference)

        //Then
        assertThat(image).isNotNull
        verify(storageProvider).retrieveImage(path)
    }

    @Test
    fun `handle - should throw notfound exception when image is not in either s3 or in server`() {
        //Given
        val imageType = PredefinedImageType.ORIGINAL
        val reference = "test.png"
        val path = "/original/test/test.png"

        whenever(storageProvider.retrieveImage(path))
            .thenReturn(null)

        //When
        val exception = catchThrowable {
            originalImageHandler.handle(imageType, reference)
        }

        //Then
        assertThat(exception)
            .isNotNull
            .isExactlyInstanceOf(NotFoundException::class.java)
        verify(storageProvider).retrieveImage(path)
    }

    @Test
    fun `flush - should delete both optimized and original images`() {
        //Given
        val imageType = PredefinedImageType.ORIGINAL
        val reference = "abcd.png"

        //When
        val image = originalImageHandler.flush(imageType, reference)

        //Then
        assertThat(image).isNotNull
        verify(storageProvider, times(PredefinedImageType.values().size)).deleteImage(any())
    }
}