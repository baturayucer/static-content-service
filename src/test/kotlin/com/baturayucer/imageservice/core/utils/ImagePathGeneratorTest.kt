package com.baturayucer.imageservice.core.utils

import com.baturayucer.imageservice.core.enums.PredefinedImageType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ImagePathGeneratorTest {

    @Test
    fun `generatePath should generate single dir path for file names equal or under 4 characters`() {
        //Given
        val fourLetterFilename = "abcd.jpg"
        val expectedPath = "/thumbnail/abcd/abcd.jpg"

        //When
        val path = ImagePathGenerator.generatePath(
            PredefinedImageType.THUMBNAIL,
            fourLetterFilename
        )

        //Then
        assertThat(path).isEqualTo(expectedPath)
    }

    @Test
    fun `generatePath should generate single dir path for the filenames between 4 and 8 letters`() {
        //Given
        val fourLetterFilename = "abcde.jpg"
        val expectedPath = "/thumbnail/abcd/abcde.jpg"

        //When
        val path = ImagePathGenerator.generatePath(
            PredefinedImageType.THUMBNAIL,
            fourLetterFilename
        )

        //Then
        assertThat(path).isEqualTo(expectedPath)
    }

    @Test
    fun `generatePath should generate double dir path for the filenames above 8 letters`() {
        //Given
        val fourLetterFilename = "/somedir/anotherdir/abcdef.jpg"
        val expectedPath = "/thumbnail/_som/edir/_somedir_anotherdir_abcdef.jpg"

        //When
        val path = ImagePathGenerator.generatePath(
            PredefinedImageType.THUMBNAIL,
            fourLetterFilename
        )

        //Then
        assertThat(path).isEqualTo(expectedPath)
    }
}