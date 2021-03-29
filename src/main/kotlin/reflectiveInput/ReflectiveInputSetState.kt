package reflectiveInput

import kotlinext.js.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label
import util.setState

/**
 * EnterWordStateOriginal is used as a State Object
 *
 * @property word is the word that is updated when the input changes.
 * @property updateClicked is the property that is updated when the button gets clicked.
 * @property updatedWord the new word that has been updated.
 */
external interface SetStateExampleState: RState {
    var word: String
    var updateClicked: Int
    var updatedWord: String
}

/**
 * Using the state interface for state, we can create outside of the
 * component itself, this keeps all the logic clean within the functional component.
 */
private val initialState = object : SetStateExampleState {
    override var word = ""
    override var updateClicked = 0
    override var updatedWord = ""
}

/**
 * enterWordSetState is a functional component that renders an input, a button and a label.
 */
private val enterWordSetState = functionalComponent<RProps> {
    /**
     * When we first declare the useState, the default value is set in the parenthesis.
     * This will be held in enterWordState.
     *
     * To modify this we use the setEnterWord function, delegated with the [by] key.
     * To clarify [enterWordState] is treated as a var with an overridden getter and a setter.
     */
    var enterWordState by useState { initialState }

    /**
     * Div container with input, button and label inside.
     */
    div {
        /**
         * Input has attributes of type text and value equal to EnterWordState's word
         * property.
         * @see enterWordState
         */
        input {
            attrs {
                type = InputType.text
                value = enterWordState.word
                placeholder = "Enter your word"
                /**
                 * @see onChangeFunction is fired when the Input value changes.
                 * When executed, we update the EnterWordState for word.
                 * This doesn't update updateClicked and this remains as the same value.
                 */
                onChangeFunction = { event ->
                    enterWordState = setState(enterWordState) {
                        word = (event.target as HTMLInputElement).value
                    }
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
                        enterWordState = setState(enterWordState) {
                            updateClicked += 1
                            updatedWord = word
                        }
                    }
                }
            }
        }
    }
    /**
     * Label shows the updated word.
     */
    label {
        +enterWordState.updatedWord
    }
}

/**
 * ReactBuilder function used to construct the React Component enterWord.
 */
fun RBuilder.enterWordSetState() = child(enterWordSetState)