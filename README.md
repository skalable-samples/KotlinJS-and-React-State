![image](https://storage.googleapis.com/skalable.appspot.com/logo.png)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.txt)
[![Kotlin JS IR supported](https://img.shields.io/badge/Kotlin%2FJS-IR%20supported-yellow)](https://kotl.in/jsirsupported)

# KotlinJS and State Hooks

At sKalable we are Kotlin Obsessed! Making the environment better is part of our daily mission. We want to make all
things KotlinJS amazingly easy to work with too.

As part of our pursuit to clean up code, we will be delving into state management in this two part tutorial. :
sunglasses:

`useState` part of React Hooks for `state` management is something that even [`Javascript`](https://www.javascript.com/) and [`Typescript`](https://www.typescriptlang.org/) engineers struggle with from time to time. We are going to reduce this struggle within the React ecosystem using KotlinJS and the ever incredible [Kotlin-React](https://github.com/JetBrains/kotlin-wrappers/blob/master/kotlin-react/README.md) library.

## Understanding state

To get an idea of what we are trying to do we need to grasp what `state` is in [react](https://reactjs.org/) programming.

**_So lets start!_**

### What is state

The React library provides components with a built-in `state` management object. In this `state` object we can store and manipulate the state of the React component on the fly. If the state object changes, then the component will re-render with the updated state and any UI changes will be reflected.

### How does it work

![image](https://storage.googleapis.com/skalable.appspot.com/KotlinJS%20State%20/KotlinJS%20State.png)

### Keeping things reactive

We can describe `state` as being reactive since it stores dynamic data for the component. The `state` object itself allows the component to keep track of changes and updates to the data and render views accordingly. It works similarly to the [Observer Pattern](https://www.tutorialspoint.com/design_pattern/observer_pattern.htm) given that it defines a subscription mechanism to notify observers of the data about any events that happen to the payload they are observing.

_Moving on we will cover `state` in both Class and Functional components._

### State in Class Components

A Stateful Class component is [lifecycle aware](https://reactjs.org/docs/state-and-lifecycle.html#converting-a-function-to-a-class) and has its `state` defined in an `external interface`. They can also initialise `state` as a class property _(which we will cover later in [The `useState` Hook](#the-usestate-hook))_ or in a constructor function — both approaches achieve the same result.

When we first initialise our custom `state`, it creates a `getter` and `setter` for the value of the property we want to have state aware. The getter is named similarly to a property variable in Kotlin [(see naming properties in Kotlin)](https://kotlinlang.org/docs/coding-conventions.html#property-names) such as `count` or `word`, i.e. descriptive of the data it holds.

To update the data in this `getter` we use the function defined as `setState`. Inside the Lambda block from this function we have access the variable we want to update.

```kotlin
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
```

Let's see the code in action!

![here](https://storage.googleapis.com/skalable.appspot.com/KotlinJS%20State%20/IndentByDot.gif)

Even though there is nothing wrong with class components, they can be quite verbose and heavyweight so let's compare how this code looks when optimised with the `useState` hook and Functional Components!

### The useState Hook!

Prior to [React 16.8](https://reactjs.org/blog/2019/02/06/react-v16.8.0.html), functional components could not hold
a `state`. Luckily, this is no longer the case as we can now use [React Hooks](https://reactjs.org/docs/hooks-state.html) that include the power of `useState`!

Before this, one of the key differences between them was that functional components lacked the capability to hold an abstracted `state` property. With the introduction of the `useState` hook there is now an alternative to this. :)

```kotlin
val (word, setWord) = useState("")
```

The example above shows a simple `useState` variable of type `String`. The default value is initialised in the parameters of the `useState` function — i.e `useState("hello")`, this would declare the `getter` value as `"hello"`. To update the value of the `word` we use the function `setWord("World")`. Essentially, `word` is the getter and `setWord` is the setter.

We can actually tidy up this logic further with delegation using the [by](https://kotlinlang.org/docs/delegated-properties.html) keyword to delegate the `get` and `set` of `useState`.

```kotlin
var wordState by useState("")
```

To benefit from delegation, we need to convert the way we instantiate the state variable. To have `state` capability, the type of the property needs to become mutable — i.e. `val` to `var`. There is also no need to keep two properties for `get` and `set` either. Renaming the variable is important as it has a hidden superpower.

Here @sKalable our preference is to give it a suffix named `State` for more clarity around our code and hidden functionality.

### State in Functional Components

_lets refactor our Class Component to a Functional Component!_

```kotlin
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
```

Running the code again we can see it works exactly the same as before, except with much less boilerplate.

![](https://storage.googleapis.com/skalable.appspot.com/KotlinJS%20State%20/IndentByDotFC.gif)

There we have it, two approaches to using state in both a Class and Functional Component!

## To Summarise

Effective code is clean and readable code. Also, you might be wondering how to handle multiple states? We cover this in Part 2 of KotlinJS and State Hooks!

As always, you can find the sample project for the above [here](https://github.com/skalable-samples/KotlinJS-and-React-State)

_[Part 2 is now available](https://github.com/skalable-samples/KotlinJS-and-React-State/blob/master/README_PART_2.md)_

Thank you for taking your time to learn with us! Feel free to reach out and say hello. 

@sKalable we are a Kotlin-centric agency that builds code to ensure it is _Maintainable_, _Flexible_ and of course, _sKalable_.

Follow us on [Twitter](https://twitter.com/skalable_dev) and [Dev.to](https://dev.to/skalabledev) and [LinkedIn](https://www.linkedin.com/company/skalable-dev/) to get the latest on Kotlin Multiplatform for your business or personal needs. 
