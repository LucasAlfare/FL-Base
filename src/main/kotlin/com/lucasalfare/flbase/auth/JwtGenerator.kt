package com.lucasalfare.flbase.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.lucasalfare.flbase.AppError
import com.lucasalfare.flbase.Constants
import com.lucasalfare.flbase.EnvsLoader
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlin.time.Duration.Companion.minutes

/**
 * Utility object for generating and verifying JSON Web Tokens (JWT).
 *
 * This component uses HMAC256 algorithm to sign and verify JWTs.
 * The signing secret is loaded from environment configuration.
 *
 * @see EnvsLoader.jwtAlgorithmSignSecret
 */
object JwtGenerator {

  /**
   * The secret key used to sign JWTs.
   */
  private val jwtAlgorithmSignSecret = EnvsLoader.jwtAlgorithmSignSecret

  /**
   * A [JWTVerifier] instance configured with HMAC256 and the signing secret.
   * Used to verify token authenticity and integrity.
   *
   * TODO: In future, consider loading algorithm type from environment variables.
   */
  val verifier: JWTVerifier = JWT
    .require(Algorithm.HMAC256(jwtAlgorithmSignSecret))
    .build()

  /**
   * Generates a JWT with an optional expiration time and custom string claims.
   *
   * @param expiresAt The expiration timestamp of the token. Defaults to now + [Constants.DEFAULT_JWT_EXPIRATION_TIME] minutes.
   * @param claims A variable number of string key-value pairs to embed as claims in the token.
   * @return A signed JWT string.
   * @throws AppError if token creation fails due to an internal [JWTCreationException].
   */
  fun generate(
    expiresAt: Instant = (Clock.System.now() + Constants.DEFAULT_JWT_EXPIRATION_TIME.minutes),
    vararg claims: Pair<String, String>
  ): String = try {
    var tokenBuilder = JWT.create()
    tokenBuilder = tokenBuilder.withExpiresAt(expiresAt.toJavaInstant())
    claims.forEach {
      tokenBuilder = tokenBuilder.withClaim(it.first, it.second)
    }

    tokenBuilder.sign(Algorithm.HMAC256(jwtAlgorithmSignSecret))
  } catch (e: JWTCreationException) {
    throw AppError(customMessage = "Error creating JWT", parent = e)
  }
}