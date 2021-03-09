package indent

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div

/**
 * A class component extends from [RComponent]. There is no requirement for
 * an external prop or state. The values of [RProps] and / or [RState]
 * can be provided without the need to declare external interfaces reflecting these.
 */
private class IndentWithDot : RComponent<RProps, IndentState>() {
    /**
     * To initialise the `state` when the class component is created we
     * must override the `RState.init()` function corresponding to the external
     * interface we provided to the component. In our case its `IndentState.init()`
     *
     * @see RComponent<IndentProps, IndentState> — (We're interested in IndentState)
     * @see IndentState
     */
    override fun IndentState.init() {
        indentAmount = 1
        indentationValue = "."
    }

    /**
     * The render function gets called when the component mounts or is updated.
     * Code inside the render function gets rendered when called.
     *
     * In our render function we have a single [button] that updates
     * the [indent] each time its pressed and displays the current update to the user.
     *
     * In order to read the `indentationValue` and `indentAmount` we need to reference the `state` from our class
     * and get the `indent` values from it.
     * @see IndentState
     *
     */
    override fun RBuilder.render() {
        div {
            button {
                // Update the string using the values from state.indentationValue and state.ident
                +"press me to add another dot indent ${state.indentationValue} ${state.indentAmount}"
                attrs {
                    onClickFunction = {
                        setState {
                            /**
                             * Reference the value of the `state.indent` and add 1.
                             * This will become the new value of `indent`.
                             */
                            indentAmount = state.indentAmount + 1
                            indentationValue = ".".repeat(indentAmount)
                        }
                    }
                }
            }
        }
    }
}

/**
 * ReactBuilder function used to construct the React Component IndentWithDot.
 */
fun RBuilder.indentByDots() = child(IndentWithDot::class) {}
