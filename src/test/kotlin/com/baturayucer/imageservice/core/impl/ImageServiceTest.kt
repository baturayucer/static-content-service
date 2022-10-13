package com.baturayucer.imageservice.core.impl

import com.baturayucer.imageservice.ImageServiceTestHelper.generateImage
import com.baturayucer.imageservice.core.ImageRetrieveStrategy
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import com.baturayucer.imageservice.core.exception.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class ImageServiceTest {

    @Mock
    private lateinit var originalImageHandler: OriginalImageHandler

    @Mock
    private lateinit var optimizedImageHandler: OptimizedImageHandler

    private lateinit var imageRetrieveStrategy: ImageRetrieveStrategy

    private lateinit var imageService: ImageServiceImpl

    @BeforeEach
    fun setup() {
        imageRetrieveStrategy = spy(
            ImageRetrieveStrategy(originalImageHandler, optimizedImageHandler)
        )
        imageService = ImageServiceImpl(imageRetrieveStrategy)
    }

    @Test
    fun `getImage - should get proper image by predefinedImageType and reference`() {
        //Given
        val imageType = PredefinedImageType.THUMBNAIL
        val reference = "test-image.png"

        whenever(optimizedImageHandler.handle(imageType, reference))
            .thenReturn(generateImage())

        //When
        val image = imageService.getImage(imageType, reference)

        //Then
        assertThat(image).isNotNull
    }

    @Test
    fun `getImage - should throw NotFoundException when there is no optimized or original image`() {
        //Given
        val imageType = PredefinedImageType.THUMBNAIL
        val reference = "test-image.png"

        whenever(optimizedImageHandler.handle(imageType, reference))
            .thenReturn(null)

        //When
        val exception = catchThrowable {
            imageService.getImage(imageType, reference)
        }

        //Then
        assertThat(exception)
            .isNotNull
            .isExactlyInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `deleteImage - should delete proper image by predefinedImageType and reference`() {
        //Given
        val imageType = PredefinedImageType.THUMBNAIL
        val reference = "test-image.png"

        //When
        imageService.deleteImage(imageType, reference)

        //Then
        verify(optimizedImageHandler).flush(imageType, reference)
        verifyNoInteractions(originalImageHandler)
    }

    @Test
    fun `deleteImage - should throw NotFoundException when there is no strategy found`() {
        //Given
        val imageType = PredefinedImageType.THUMBNAIL
        val reference = "test-image.png"

        whenever(optimizedImageHandler.flush(imageType, reference))
            .thenThrow(NotFoundException("No image handle strategy found to delete for type: $imageType"))

        //When
        val exception = catchThrowable {
            imageService.deleteImage(imageType, reference)
        }

        //Then
        assertThat(exception)
            .isNotNull
            .isExactlyInstanceOf(NotFoundException::class.java)
    }

}