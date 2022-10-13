package com.baturayucer.imageservice

import java.nio.file.Files
import java.nio.file.Paths

object ImageServiceTestHelper {

    fun generateImage(): ByteArray =
        Files.readAllBytes(
            Paths.get("src/test/resources/images/test-image.png")
        )
}