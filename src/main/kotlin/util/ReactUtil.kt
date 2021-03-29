package util

import kotlinext.js.*
import react.RState

/**
 * By creating a utility function to map the current state with
 * the updated variables, it removes the need to create multiple
 * builder functions for larger states across the project.
 *
 * Using this function we can keep code clean and efficient.
 * @see T — The purpose of extending RState is to keep uniformity
 * across the code. If we look to change the type of component we can
 * be guaranteed the state will work for free.
 *
 * @param oldState — The current state values
 * @param newState — The new values we would like to apply to the state
 *
 * @return T — The values of old state plus the updated values of new state.
 */
internal inline fun <T : RState> setState(
    oldState: T,
    newState: T.() -> Unit
): T = clone(oldState).apply(newState)