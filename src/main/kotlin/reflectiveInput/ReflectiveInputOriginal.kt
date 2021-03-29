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
 * EnterWordStateOriginal is used as a State Object
 *
 * @property word is the word that is updated when the input changes.
 * @property updateClicked is the property that is updated when the button gets clicked.
 * @property updatedWord the new word that has been updated.
 */
external interface EnterWordStateOriginal {
    var word: String
    var updateClicked: Int
    var updatedWord: String
}

/**
 * enterWordOriginal is a functional component that renders an input, a button and a label.
 */
private val enterWordOriginal = functionalComponent<RProps> {
    /**
     * When we first declare the useState, the default value is set in the parenthesis.
     * This will be held in enterWordState.
     *
     * To modify this we use the setEnterWord function, delegated with the [by] key.
     * To clarify enterWord is treated as a var with a getter and a setter.
     */
    var enterWordState by useState<EnterWordStateOriginal> {
        object : EnterWordStateOriginal {
            override var word = ""
            override var updateClicked = 0
            override var updatedWord = ""
        }
    }

    /**
     * Div container with input, button and label inside.
     */
    div {
        /**
         * Input has attributes of type text and value equal to EnterWordState's word property.
         * @see EnterWordState
         */
        input {
            attrs {
                type = InputType.text
                placeholder = "Enter your word"
                /**
                 * @see onChangeFunction is fired when the Input value changes.
                 * When executed, we update the EnterWordState for word.
                 * This doesn't update updateClicked and this remains as the same value.
                 */
                onChangeFunction = { event ->
                    enterWordState = object : EnterWordStateOriginal {
                        override var word = (event.target as HTMLInputElement).value
                        override var updateClicked = enterWordState.updateClicked
                        override var updatedWord = enterWordState.updatedWord
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
                        enterWordState = object : EnterWordStateOriginal {
                            override var word = enterWordState.word
                            override var updateClicked = enterWordState.updateClicked + 1
                            override var updatedWord = enterWordState.word
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
fun RBuilder.enterWordOriginal() = child(enterWordOriginal)

