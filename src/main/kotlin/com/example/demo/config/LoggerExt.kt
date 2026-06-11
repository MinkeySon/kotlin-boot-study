package com.example.demo.config

import io.github.oshai.kotlinlogging.KotlinLogging

inline fun <reified T> T.logger() = KotlinLogging.logger(T::class.java.name)