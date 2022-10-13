package com.baturayucer.imageservice.control

import com.baturayucer.imageservice.core.ImageService
import com.baturayucer.imageservice.core.enums.PredefinedImageType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping(
    consumes = [
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_OCTET_STREAM_VALUE
    ],
    produces = [
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        MediaType.IMAGE_GIF_VALUE
    ],
    value = ["/v1/image"]
)
class ImageController(
    private val imageService: ImageService
) {

    @Operation(
        summary = "Provides images by given filters"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Image found successfully.",
            content = [
                Content(mediaType = "application/json"),
                Content(mediaType = "image/jpeg"),
                Content(mediaType = "image/png")
            ]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Not found: The requested item could not be found.",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request: The sent request does not meet the API specification.",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error: The request couldn't be handled.",
            content = [Content(mediaType = "application/json")]
        )
    )
    @GetMapping(
        value = ["/show/{predefined-type-name}/{dummy-seo-name}"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getImage(
        @NotNull
        @PathVariable(value = "predefined-type-name", required = false)
        predefinedImageType: PredefinedImageType = PredefinedImageType.ORIGINAL,
        @PathVariable(value = "dummy-seo-name", required = false)
        dummySeoName: String? = null,
        @NotNull
        @RequestParam(value = "reference", required = true)
        reference: String,
    ): ByteArray = imageService.getImage(predefinedImageType, reference)


    @Operation(
        summary = "Deletes image by given filters"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Image deleted successfully.",
            content = [
                Content(mediaType = "application/json")
            ]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Not found: The requested item could not be found.",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request: The sent request does not meet the API specification.",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error: The request couldn't be handled.",
            content = [Content(mediaType = "application/json")]
        )
    )
    @DeleteMapping(
        value = ["/flush/{predefined-type-name}"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun flashImage(
        @NotNull
        @PathVariable(value = "predefined-type-name", required = false)
        predefinedImageType: PredefinedImageType = PredefinedImageType.ORIGINAL,
        @NotNull
        @RequestParam(value = "reference", required = true)
        reference: String,
    ) = imageService.deleteImage(predefinedImageType, reference)
}