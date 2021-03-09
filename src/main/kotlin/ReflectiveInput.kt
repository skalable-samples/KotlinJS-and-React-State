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
 * Props Interface allows us to pass in the parameter word
 * to the functional component.
 * @see enterWord
 */
external interface EnterWordProps : RProps {
    var word: String
}

/**
 * EnterWordState is used as a State Object
 * @property word is the word that is updated when the input changes.
 * @property updateClicked is the property that is updated when the button gets clicked.
 */
external interface EnterWordState {
    var word: String
    var updateClicked: Int
}

/**
 * enterWord is a functional component that renders an input, a button and a label.
 */
private val enterWord = functionalComponent<EnterWordProps> { props ->
    /**
     * When we first declare the useState, the default value is set in the parenthesis. This will be held in enterWord.
     * To modify this we use the setEnterWord function.
     * To clarify enterWord is a value, setEnterWord is a function.
     */
    var enterWordState by useState<EnterWordState>(object : EnterWordState {
        override var word = props.word
        override var updateClicked = 0
    })

    /**
     * Primitive State based on a String. The type is inferred.
     */
    var updatedWordState by useState("")
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
                value = enterWordState.word
                /**
                 * @see onChangeFunction is fired when the Input value changes.
                 * When executed, we update the EnterWordState for word.
                 * This doesn't update updateClicked and this remains as the same value.
                 */
                onChangeFunction = { event ->
                    enterWordState = object : EnterWordState {
                        override var word = (event.target as HTMLInputElement).value
                        override var updateClicked = enterWordState.updateClicked
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
                        enterWordState = object : EnterWordState {
                            override var word = enterWordState.word
                            override var updateClicked = enterWordState.updateClicked + 1
                        }
                        updatedWordState = enterWordState.word
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
 * ReactBuilder function used to construct the React Component enterWord.
 */
fun RBuilder.enterWord(word: String = "Enter a word") = child(enterWord) {
    attrs.word = word
}

