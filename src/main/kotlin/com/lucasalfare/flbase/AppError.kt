@file:Suppress("unused")

package com.lucasalfare.flbase

import io.ktor.http.*

/**
 * Base class for application-level exceptions.
 *
 * Note: [customMessage] is intended for end users of this library,
 * while [parent] holds the underlying exception that caused the error.
 * Use [customMessage] to inform users about what happened, and [parent] for internal logging/debugging.
 *
 * @property customMessage Optional human-readable message describing the error.
 * @property status HTTP status code associated with the error. Defaults to 500 Internal Server Error.
 * @property parent Optional parent throwable for exception chaining.
 */
open class AppError(
  val customMessage: String?,
  val status: HttpStatusCode = HttpStatusCode.InternalServerError,
  val parent: Throwable? = null
) : Throwable()