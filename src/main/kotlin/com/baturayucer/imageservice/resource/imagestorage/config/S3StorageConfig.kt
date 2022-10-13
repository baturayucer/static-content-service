package com.baturayucer.imageservice.resource.imagestorage.config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3StorageConfig {

    @Value("\${aws.s3.region:us-east-1}")
    private val region: String = "us-east-1"

    @Bean
    fun s3(): AmazonS3 = AmazonS3ClientBuilder
        .standard()
        .withRegion(region)
        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
        .build()
}