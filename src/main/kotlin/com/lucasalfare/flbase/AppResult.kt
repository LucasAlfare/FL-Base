@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.lucasalfare.flbase

import io.ktor.http.*

/**
 * A generic wrapper class that represents the result of an operation in the application,
 * encapsulating both the data and an associated HTTP status code.
 *
 * This class is commonly used in web applications or APIs to provide a uniform structure
 * for responses. It allows returning any type of result (`T`) along with an HTTP status
 * code, which helps in standardizing success and error responses across the application.
 *
 * @param T The type of the data returned by the operation (e.g., a model, list, message, etc.).
 *
 * @property data The actual result data of the operation. It can be any type, depending on context.
 * @property statusCode The HTTP status code indicating the result's status.
 *                      Defaults to [HttpStatusCode.OK], representing success.
 *
 * @constructor Creates a new instance of `AppResult` with the given data and an optional HTTP status.
 *
 * @example
 * ```
 * // Example usage with a success message and default status (200 OK)
 * val result = AppResult("User created successfully")
 *
 * // Example usage with an error message and custom status
 * val errorResult = AppResult("User not found", HttpStatusCode.NotFound)
 *
 * // Example with a more complex return type
 * val userResult = AppResult(userDto, HttpStatusCode.Created)
 * ```
 */
class AppResult<T>(
  val data: T,
  val statusCode: HttpStatusCode = HttpStatusCode.OK
)