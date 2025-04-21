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
  val status: HttpStatusCode = HttpStatusCode.InternalServerError
) : Throwable()

/**
 * Exception indicating that the database service is currently unavailable.
 *
 * Typically used when the application fails to establish a connection with the database,
 * or encounters a critical failure during a database operation.
 *
 * @param customMessage A descriptive message. Defaults to "Error performing database operation."
 * @param status The associated HTTP status. Defaults to [HttpStatusCode.InternalServerError].
 */
class UnavailableDatabaseService(
  customMessage: String = "Error performing database operation.",
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)

/**
 * Exception thrown when the request payload is malformed, incomplete, or invalid.
 *
 * Used to indicate client-side errors where the provided data does not meet expected requirements.
 *
 * @param customMessage A descriptive message. Defaults to "Error in the requested payload."
 * @param status The associated HTTP status. Defaults to [HttpStatusCode.BadRequest].
 */
class BadRequest(
  customMessage: String = "Error in the requested payload.",
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

/**
 * Exception thrown when a serialization or deserialization process fails.
 *
 * This usually occurs when converting between raw data (e.g., JSON) and Kotlin objects fails.
 *
 * @param customMessage A descriptive message. Defaults to "Error in serialization process."
 * @param status The associated HTTP status. Defaults to [HttpStatusCode.BadRequest].
 */
class SerializationError(
  customMessage: String = "Error in serialization process.",
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

/**
 * Exception thrown when validation of request fields fails.
 *
 * Used to signal that one or more fields in a request do not meet validation constraints,
 * such as required fields, formats, ranges, etc.
 *
 * @param customMessage A descriptive message. Defaults to "Error in validation of fields."
 * @param status The associated HTTP status. Defaults to [HttpStatusCode.BadRequest].
 */
class ValidationError(
  customMessage: String = "Error in validation of fields.",
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

/**
 * Exception thrown when a required environment variable is null.
 *
 * Used to prevent the application from continuing execution with missing critical configuration.
 *
 * @param customMessage A descriptive message. Defaults to "Missing an environment variable in server (null)".
 * @param status The associated HTTP status. Defaults to [HttpStatusCode.InternalServerError].
 */
class NullEnvironmentVariable(
  customMessage: String = "Missing an environment variable in server (null)",
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)

/**
 * Exception thrown when a required environment variable is present but empty.
 *
 * Similar to [NullEnvironmentVariable], but specifically covers empty string cases,
 * helping differentiate configuration errors.
 *
 * @param customMessage A descriptive message. Defaults to "Missing an environment variable in server (empty)".
 * @param status The associated HTTP status. Defaults to [HttpStatusCode.InternalServerError].
 */
class EmptyEnvironmentVariable(
  customMessage: String = "Missing an environment variable in server (empty)",
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)