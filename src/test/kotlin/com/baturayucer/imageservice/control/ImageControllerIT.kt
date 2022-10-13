package com.baturayucer.imageservice.control

import com.baturayucer.imageservice.ImageServiceTestHelper.generateImage
import com.baturayucer.imageservice.control.exception.model.ExpectedError
import com.baturayucer.imageservice.control.exception.model.UnExpectedError
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import com.baturayucer.imageservice.resource.imagestorage.S3StorageService
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImageControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var s3StorageService: S3StorageService

    @Test
    fun `getImage - should provide proper image with predefinedImageType and reference with 200 OK`() {
        //Given
        val reference = "test-image.png"
        val imageType = PredefinedImageType.THUMBNAIL

        whenever(s3StorageService.retrieveImage(any()))
            .thenReturn(generateImage())

        //When
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/image/show/$imageType/dumm-seo?reference=$reference")
                .accept(MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        //Then
        val responseBody = response.contentAsByteArray
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        assertThat(responseBody).isNotNull
    }

    @Test
    fun `getImage - should return 400 Bad Request when parameters are not valid`() {
        //Given
        val invalidReference = "test-image.xls"
        val imageType = PredefinedImageType.THUMBNAIL

        //When
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/image/show/$imageType/dumm-seo?reference=$invalidReference")
                .accept(MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        //Then
        val error = objectMapper.readValue(response.contentAsString, ExpectedError::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(error.errorMessage).isEqualTo("File name should end with following values: [.jpg, .jpeg, .png, .gif]")
    }

    @Test
    fun `getImage - should return 404 Not Found when no image found with given parameters`() {
        //Given
        val invalidReference = "test-image1.png"
        val imageType = PredefinedImageType.THUMBNAIL

        whenever(s3StorageService.retrieveImage(any())).thenReturn(null)

        //When
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/image/show/$imageType/dumm-seo?reference=$invalidReference")
                .accept(MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        //Then
        val error = objectMapper.readValue(response.contentAsString, ExpectedError::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.NOT_FOUND.value())
        assertThat(error.errorMessage).isEqualTo("Image not found with reference: $invalidReference")
    }

    @Test
    fun `getImage - should return 500 Internal Server Error when unexpected error happens`() {
        //Given
        val reference = "test-image.png"
        val imageType = PredefinedImageType.THUMBNAIL

        whenever(s3StorageService.retrieveImage(any()))
            .thenThrow(RuntimeException("Unexpected exception"))

        //When
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/image/show/$imageType/dumm-seo?reference=$reference")
                .accept(MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        //Then
        val error = objectMapper.readValue(response.contentAsString, UnExpectedError::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        assertThat(error.errorMessage).isEqualTo("Error occurred in image service. Detail: Unexpected exception")
    }

    @Test
    fun `deleteImage - should delete proper image with predefinedImageType and reference with 200 OK`() {
        //Given
        val reference = "test-image.png"
        val imageType = PredefinedImageType.THUMBNAIL

        //When
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/v1/image/flush/$imageType?reference=$reference")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        //Then
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `deleteImage - should return 400 Bad Request when parameters are not valid`() {
        //Given
        val invalidReference = "test-image.xls"
        val imageType = PredefinedImageType.THUMBNAIL

        //When
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/v1/image/flush/$imageType?reference=$invalidReference")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        //Then
        val error = objectMapper.readValue(response.contentAsString, ExpectedError::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(error.errorMessage).isEqualTo("File name should end with following values: [.jpg, .jpeg, .png, .gif]")
    }

    @Test
    fun `deleteImage - should return 500 Internal Server Error when unexpected error happens`() {
        //Given
        val reference = "test-image.png"
        val imageType = PredefinedImageType.THUMBNAIL

        whenever(s3StorageService.deleteImage(any()))
            .thenThrow(RuntimeException("Unexpected exception"))

        //When
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/v1/image/flush/$imageType?reference=$reference")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        //Then
        val error = objectMapper.readValue(response.contentAsString, UnExpectedError::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        assertThat(error.errorMessage).isEqualTo("Error occurred in image service. Detail: Unexpected exception")
    }

}