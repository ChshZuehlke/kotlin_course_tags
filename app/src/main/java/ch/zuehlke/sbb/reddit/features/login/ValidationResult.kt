package ch.zuehlke.sbb.reddit.features.login

sealed class ValidationResult
object Ok : ValidationResult()
data class Failed(val message: String) : ValidationResult()