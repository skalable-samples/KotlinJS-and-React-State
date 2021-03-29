import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import react.dom.render
import reflectiveInput.*
import styled.css
import styled.styledDiv

fun main() {
    window.onload = {
        render(document.getElementById("root")) {
//            indentByDots()
//            indentByDotsFC()

            styledDiv {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    justifyContent = JustifyContent.spaceAround
                }
                enterWordOriginal()
                enterWordWithParameter()
                enterWordWithBuilder()
                //enterWordWithDataClass()
                enterWordSetState()
            }
        }
    }
}