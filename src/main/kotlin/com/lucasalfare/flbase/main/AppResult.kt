package com.lucasalfare.flbase.main

import io.ktor.http.*

class AppResult<T>(
  val data: T,
  val statusCode: HttpStatusCode = HttpStatusCode.OK
)