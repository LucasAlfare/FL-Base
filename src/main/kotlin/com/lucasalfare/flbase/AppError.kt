@file:Suppress("unused")

package com.lucasalfare.flbase

import io.ktor.http.*

/**
 * Represents a general application error with a custom message and an associated HTTP status.
 *
 * This is the base class for all application-specific exceptions.
 * It can be used to standardize error handling and response formatting in the application.
 *
 * By extending [Throwable], this class allows these errors to be thrown and caught like regular exceptions.
 *
 * @property customMessage A message providing more context about the error.
 * @property status The HTTP status code associated with the error. Defaults to [HttpStatusCode.InternalServerError].
 */
open class AppError(
  val customMessage: String?,
  val status: HttpStatusCode = HttpStatusCode.InternalServerError,
  val parent: Throwable? = null
) : Throwable()