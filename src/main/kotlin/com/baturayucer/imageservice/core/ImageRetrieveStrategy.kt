package com.baturayucer.imageservice.core

import com.baturayucer.imageservice.core.enums.PredefinedImageType
import com.baturayucer.imageservice.core.impl.OptimizedImageHandler
import com.baturayucer.imageservice.core.impl.OriginalImageHandler
import org.springframework.context.annotation.Configuration

@Configuration
class ImageRetrieveStrategy(
    originalImageService: OriginalImageHandler,
    optimizedImageService: OptimizedImageHandler
) {
    val strategies = mapOf(
        PredefinedImageType.ORIGINAL to originalImageService,
        PredefinedImageType.THUMBNAIL to optimizedImageService,
        PredefinedImageType.DETAIL_LARGE to optimizedImageService
    )
}