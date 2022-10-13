package com.baturayucer.imageservice.core

interface ImageStorageProvider {
    fun retrieveImage(path: String): ByteArray?
    fun saveImage(path: String, image: ByteArray)
    fun deleteImage(path: String)
}