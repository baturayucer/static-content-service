package com.baturayucer.imageservice.control.exception

import com.baturayucer.imageservice.control.exception.model.ExpectedError
import com.baturayucer.imageservice.control.exception.model.UnExpectedError
import com.baturayucer.imageservice.core.exception.NotFoundException
import com.baturayucer.imageservice.core.exception.ValidationException
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ImageServiceExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handle(
        exception: NotFoundException
    ) = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ExpectedError(exception.message!!))
        .also {
            logger.error { "Requested object is not found: ${exception.message}" }
        }

    @ExceptionHandler(ValidationException::class)
    fun handle(
        exception: ValidationException
    ) = ResponseEntity
        .badRequest()
        .body(ExpectedError(exception.message!!))
        .also {
            logger.error { "Bad request: ${exception.message}" }
        }

    @ExceptionHandler(Exception::class)
    fun handle(
        exception: Exception
    ): ResponseEntity<UnExpectedError> {
        val errorMessage = "Error occurred in image service. Detail: ${exception.message}"
        return ResponseEntity.internalServerError().body(
            UnExpectedError(
                errorMessage = errorMessage
            )
        ).also {
            logger.error { errorMessage }
        }
    }

    companion object : KLogging()
}