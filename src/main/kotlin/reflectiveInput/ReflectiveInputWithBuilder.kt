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
external interface EnterWordStateWithBuilder {
    var word: String
    var updateClicked: Int
    var updatedWord: String
}

/**
 * enterWordWithBuilder is a functional component that renders an input, a button and a label.
 */
private val enterWordWithBuilder = functionalComponent<RProps> {
    /**
     * When we first declare the useState, the default value is set in the parenthesis.
     * This will be held in enterWordState.
     * To modify this we use the setEnterWord function, delegated with the [by] key.
     * To clarify enterWord is treated as a var with a getter and a setter.
     */
    var enterWordState by useState<EnterWordStateWithBuilder> {
        object : EnterWordStateWithBuilder {
            override var word = ""
            override var updateClicked = 0
            override var updatedWord = ""
        }
    }

    /**
     * In this approach we use utility builders within the functional component to set state as a single
     * line when interfaces are used as state holders.
     *
     * Using default params pointed at [enterWordState] allows for cleaner setters.
     *
     * @param word — Has a default of the current state word
     * @param updateClicked — Has a default of the current state updateClicked
     * @param updatedWord — Has a default of the current state updatedWord
     */
    fun setWordState(
        word: String = enterWordState.word,
        updateClicked: Int = enterWordState.updateClicked,
        updatedWord: String = enterWordState.updatedWord
    ) {
        enterWordState = object : EnterWordStateWithBuilder {
            override var word = word
            override var updateClicked = updateClicked
            override var updatedWord = updatedWord
        }
    }

    /**
     * Div container with input, button and label inside.
     */
    div {
        /**
         * Input has attributes of type text and value equal to EnterWordStateWithBuilder's word property.
         * @see EnterWordStateWithBuilder
         */
        input {
            attrs {
                type = InputType.text
                placeholder = "Enter your word"
                /**
                 * @see onChangeFunction is fired when the Input value changes.
                 * When executed, we update the EnterWordStateWithBuilder for word.
                 * This doesn't update updateClicked and this remains as the same value.
                 */
                onChangeFunction = { event ->
                    setWordState(word = (event.target as HTMLInputElement).value)
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
                        setWordState(
                            updateClicked = enterWordState.updateClicked + 1,
                            updatedWord = enterWordState.word
                        )
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
 * ReactBuilder function used to construct the React Component enterWordWithBuilder.
 */
fun RBuilder.enterWordWithBuilder() = child(enterWordWithBuilder)
