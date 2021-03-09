package indent

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div

/**
 * [indentWithDot] is a react [functionalComponent]. This type of component is not
 * lifecycle aware and is more lightweight than a class component [RComponent].
 */
private val indentWithDot = functionalComponent<RProps> {
    /**
     *  To initialise the state within the function component we need to
     *  declare the [useState]s as the first variables in the function. Doing
     *  so ensures the variables are available for the rest of the code within
     *  the function.
     *
     *  Using the `by` keyword allows for delegation of the get and set of [useState]
     *  into the indentState var.
     *
     *  @see IndentState for state values
     */
    var indentState by useState<IndentState>(object : IndentState {
        override var indentAmount = 1
        override var indentationValue = "."
    })

    /**
     *  In a [functionalComponent] (FC) the last code block should always be the HTML to
     *  render. Compared to a class component, there is no RBuilder.render() as the HTML
     *  at the end of the function is what gets rendered. A FCs first param is a lambda extending
     *  from [RBuilder] itself so RBuilder.render() is not required.
     *
     *  As we can see, the [button] we render within [div] has an [onClickFunction] attribute that
     *  handles click events.
     *
     *  Here, when handled, we update the [IndentState.indentAmount] by adding one.
     *  [IndentState.indentationValue] is then updated by adding a number of "."s equal
     *  to the amount of [IndentState.indentAmount].
     *
     *  This value is then reflected in the text of the button.
     */
    div {
        button {
            /**
             * Update the string using the values from [IndentState.indentationValue] and [IndentState.indentAmount]
             */
            +"press me to add another dot indent from FC ${indentState.indentationValue} ${indentState.indentAmount}"
            attrs {
                onClickFunction = {
                    indentState = object : IndentState {
                        /**
                         * reference the value of the [IndentState.indentAmount] and increment by one.
                         * The value of [IndentState.indentationValue] is then updated with a number of "."s
                         * equal to the new amount of [IndentState.indentAmount]
                         */
                        override var indentAmount = indentState.indentAmount + 1
                        override var indentationValue = ".".repeat(indentAmount)
                    }
                }
            }
        }
    }
}

/**
 * ReactBuilder function used to construct the React Component IndentWithDot.
 */
fun RBuilder.indentByDotsFC() = child(indentWithDot) {}
