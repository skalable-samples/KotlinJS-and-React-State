package indent

import react.RState

/**
 * IndentState is used as a State Object
 * We will be using the variables indentAmount and indentValue for the amount of times
 * the indent was pressed and the updated string with dots.
 * @property indentationValue — The updated string with dots
 */
external interface IndentState : RState {
    var indentAmount: Int
    var indentationValue: String
}