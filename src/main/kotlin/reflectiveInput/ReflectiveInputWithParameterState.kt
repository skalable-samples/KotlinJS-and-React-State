package reflectiveInput

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label

/**
 * enterWordWithParameter is a functional component that renders an input, a button and a label.
 */
private val enterWordWithParameter = functionalComponent<RProps> {
    /**
     * When using Primitive State, we declare every individual variable as a singular.
     * Here they states are based on two Strings and an Int. The types are inferred.
     */
    var wordState by useState { "" }
    var updateClickedState by useState { 0 }
    var updatedWordState by useState { "" }

    /**
     * Div container with input, button and label inside.
     */
    div {
        /**
         * Input has attributes of type text and value equal to wordState property.
         * @see wordState
         */
        input {
            attrs {
                type = InputType.text
                placeholder = "Enter your word"
                /**
                 * @see onChangeFunction is fired when the Input value changes.
                 * When executed, we update the wordState for word.
                 * This doesn't update updateClicked and this remains as the same value.
                 */
                onChangeFunction = { event ->
                    wordState = (event.target as HTMLInputElement).value
                }
            }
        }

        /**
         * Button has the onClickFunction attribute that handles onClick events.
         * When executed, we update the updateClicked and add +1 to the count.
         */
        div {
            button {
                /**
                 * Sets the text for the button.
                 */
                +"Update"
                attrs {
                    onClickFunction = {
                        updateClickedState += 1
                        updatedWordState = wordState
                    }
                }
            }
        }
    }
    /**
     * Label shows the updated word.
     */
    label {
        +updatedWordState
    }
}

/**
 * ReactBuilder function used to construct the React Component enterWordWithParameter.
 */
fun RBuilder.enterWordWithParameter() = child(enterWordWithParameter)

