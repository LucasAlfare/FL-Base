@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.lucasalfare.flbase

import io.ktor.http.*

/**
 * Represents the result of an operation in an application, containing the data and the associated HTTP status.
 *
 * @param T The type of data returned by the operation.
 * @property data The data resulting from the operation.
 * @property statusCode The HTTP status code associated with the result. The default is `HttpStatusCode.OK`.
 *
 * @constructor Creates an instance of `AppResult` with the provided data and optionally an HTTP status code.
 *
 * @example
 * ```
 * // Example usage with String type and default status (HttpStatusCode.OK)
 * val result = AppResult("Operation successful")
 *
 * // Example usage with Int type and a specific status
 * val errorResult = AppResult(404, HttpStatusCode.NotFound)
 * ```
 */
class AppResult<T>(
  val data: T,
  val statusCode: HttpStatusCode = HttpStatusCode.OK
)