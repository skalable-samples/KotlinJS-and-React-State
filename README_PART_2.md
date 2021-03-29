![image](https://storage.googleapis.com/skalable.appspot.com/logo.png)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.txt)  
[![Kotlin JS IR supported](https://img.shields.io/badge/Kotlin%2FJS-IR%20supported-yellow)](https://kotl.in/jsirsupported)

# KotlinJS and MULTIPLE State Hooks (Part two)

At sKalable we are just in love with Kotlin! We really strive to make all things Kotlin simple, fun, and a breeze to work with :D <3 KotlinJS is no exception to our mission. 😃 ❤️

Following on from our [Part 1 tutorial of KotlinJS and State Hooks](https://dev.to/skalabledev/kotlinjs-and-state-hooks-2426) ,  that covers State as a singular, Hooks and the best practices for working with functional components, we want to take things further and  delve into using multiple State Hooks or State values in our code. Using multiple State Hooks can be advantageous as you can split them for different uses, better manage properties that change independently of each other... but with certain caveats... 

Helping to define the structure and improve the readability of our code so much more!

_(Before delving deeper into multiple State Hooks, feel free to take a look at [Part 1](https://dev.to/skalabledev/kotlinjs-and-state-hooks-2426) of this article as a refresher 😊 )_

_Let's give it a go!_

## Multiple States in action

Check out this diagram where we can see multiple states in action!

![](https://storage.googleapis.com/skalable.appspot.com/KotlinJS%20State%20/KotlinJS%20Multi%20State.png)

## The problem with Fixed State interfaces

Below is an example looking at some issues of setting an interface object as a `useState` type value.

```kotlin  
/**  
 * EnterWordStateOriginal is used as a State Object * 
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
 * enterWord is a functional component that renders an input, a button and a label. 
 */
private val enterWord = functionalComponent<RProps> {  
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

//... functional / render code .
  
/**
 * Setting a state object requires the full object to be set in functional
 * components. This can become very verbose, incredibly quickly.     
 */  
	//... HTML Input handler 
	onChangeFunction = { event -> 
		 enterWordState = object : EnterWordState {  
			 override var word = (event.target as HTMLInputElement).value  
			 override var updateClicked = enterWordState.updateClicked 
			 override var updatedWord = enterWordState.updatedWord
		 }  
	}
```  

It might not be the most elegant looking code, but it works. When using state objects in functional components, you'll see there is no requirement to set the `RState` type on the component itself. This is different to how `Class Components` work for instance.

Unlike with `Class Components`, `Functional Components` do not have a `setState {}` function to map old state to new state _(This is not the case for Props though)_ . Nor do they require knowledge of the state in their construction either.

We apply the concept of `state` to a functional component through `React Hooks`. Using hooks, the component now has the capability to handle `state` changes. There is a readability issue regarding this though...

Code should be clean, easy to write and read. Unfortunately, using `state` objects in functional components doesn't help us achieve that with the approach above.

Below, we see that in order to set `state` we must initialise the full object every time. This requires us to manually set the values of the previous states that don't change.

```kotlin  
/** 
 * Setting a state object requires the full object to be set in functional 
 * components. 
 * This can become very verbose, incredibly quickly.     
 */  
 onChangeFunction = { event ->   
	 enterWordState = object : EnterWordState {  
		 override var word = (event.target as HTMLInputElement).value  
		 override var updateClicked = enterWordState.updateClicked 
		 override var updatedWord = enterWordState.updatedWord
	 }  
 }
```  

Ughhh.... We can't be adding this everywhere each time we update the state. Ok, time to clean this up a little.

## Dividing state strategies

There is no real _"right"_ or _"wrong"_ method of tackling division of state, its mostly down to personal preference and use case for each component _(although some strategies can look ridiculous such as above)_.

Larger states have a different challenge than smaller states. Below we outline various strategies and how to decide which is best approach for the components needs and number of states you require.

### Dividing by individual values — Multi-State component
For small state interfaces,which can be described as having no more than three vars in a State, prefer an individual state for each value.

```kotlin  
/**  
 * Primitive State based on a String and an Int. The types are inferred. 
 */
var wordState by useState { props.word } // inferred String 
var updatedClickedState by useState { 0 } // inferred Int
```    
This allows for clean and easy methods to update and read the required state.
```kotlin  
updatedClickedState += 1 // update the value by 1  
```  
What about larger states? How should we handle those?

### Keeping composition / context as a Single State

If you find yourself writing a lot of repetitive code, always think of [DRY Principles](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself). We tend do repeat a lot of the `state` construction just to update a single value when using `state` as a single object. A separate function within the `functional component` can help resolve this issue.

Builder functions can be used to create new objects and handle the mapping of values. Kotlin has a feature called [default arguments](https://kotlinlang.org/docs/functions.html#default-arguments) allowing parameter values to have the default value to the corresponding states value. Automatically the parameters will have the value if one has not been provided by the caller.

Applying this approach allows for cleaner code. It does require "boilerplate" in the form of a separate function for each state interface in functional components with interface states.

Though it's a better approach to mapping, it's still not ideal nor efficient when writing components.

```kotlin
/**  
 * When we first declare the useState, the default value is set in the parenthesis. 
 * This will be held in enterWordState. 
 * 
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
```  
The result of creating a utility builder for the function state is a clean setter.
```kotlin  
/**  
 * Setting a state object requires the full object to be set in functional 
 * components. This can become very verbose, incredibly quickly. 
 */  
onChangeFunction = { event -> 
	setWordState(word = (event.target as HTMLInputElement).value)  
}  
```  

There must be another option...

As the number of `state` values grow, they become more and more cumbersome to maintain. If we need to create
large `builder functions` for each State object, our `functional components` will become more and more polluted.

### Utility function to the rescue!

The thought of writing different builders for each state object is daunting. Removing the need for this and providing a clean method of updating `state` objects without writing builders would be perfect. Even better if it meant changing the component from `functional` to a `class` didn't require the interface to change.

To solve this we look at Kotlin itself and the incredible [apply](https://kotlinlang.org/docs/scope-functions.html#apply) function. Using our old state and new state values together provides all the ingredients to create a new object by copying the existing values of the old state and applying the new state values atop.

Let us start by changing the state holder interface slightly.
```kotlin  
/**  
 * EnterWordStateOriginal is used as a State Object * 
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
```  

I know what you're all thinking, *"What's `RState` doing there?!"*

There is a genuine reason: earlier we mentioned maintaining cooperation of `state` if we change the component
from `functional` into a `class`?

Extending `RState` achieves this, but also plays a secret second purpose.👇

#### Functional setState

To prevent any regular interface being used as a `state` we can extend our state interface from `RState`. Using this as the type for our `setState` ensures only `state` objects can be used. Forcing better readability and cleaner code across our codebase naturally.

no more *"What is this badly named interface for?!"*

Our new utility function to handle this mapping will now provide us not just the clean setState we want, but the
setState we _deserve_!

```kotlin
/**  
 * By creating a utility function to map the current state with 
 * the updated variables, it removes the need to create multiple 
 * builder functions for larger states across the project. 
 * Using this function we can keep code clean and efficient. 
 * 
 * @see T — The purpose of extending RState is to keep uniformity across the code. 
 *          If we look to change the type of component we can * be guaranteed the state will work for free.
 * 
 * @param oldState — The current state values  
 * @param newState — The new values we would like to apply to the state  
 * 
 * @return T — The values of old state plus the updated values of new state.  
 */
internal inline fun <T : RState> setState(  
	oldState: T,  
	newState: T.() -> Unit  
): T = clone(oldState).apply(newState)
```

Time to break it down a little:

1. `internal`
   
_Prevents the `setState` function being exposed as part of the overall modules API._

2. `inline`


   _[`inline`](https://kotlinlang.org/docs/inline-functions.html#non-local-returns) optimises functions by inlining the lambda expressions for a reduction in runtime overhead._

3. `<T : RState>`


   _This defines the type of `oldState` and `newState`. Extending `RState` gives us the certainty this will be a `state`._


4. `oldState: T`

_The value of the existing state. Kotlin uses *"Copy by Value"* for function parameters. The `oldState` param will then
be a copy of the state we want to set. (*There is some discrepancy in this statement to the values inside, as only the
outlining object is copied, but that's for another time.*)_


5. `newState: T.() -> Unit`


   _For those of you who don't know, this has got to be one of the most amazing features of Kotlin. It's known as a  [*Function literals with receiver*](https://kotlinlang.org/docs/lambdas.html#lambda-expressions-and-anonymous-functions). We can set parameters of the `receiver T` and apply them to our clone._


6. `clone`


   _Ok, this might not be *exactly* part of the Kotlin language, but it is part of KotlinJS! It allows us to copy `oldState` into a new jsObject._


7. `apply(newState)`


   _We want to return the value of the `oldState` with the updates from `newState`. Using `apply` allows for this. `apply` returns an instance of `this` so is ideal for returning a new copy after adding `newState`._

#### Result

Adding our brand new `setState` to the `functional component` we get a clean, readable state management.

```kotlin
enterWordState = setState(enterWordState) {  
	updateClicked += 1  
	updatedWord = word
}
```
The best part of this approach is autocomplete and no need to define each value to set `state`. Our generic function infers the type of the `state` and gives us auto complete within the body of the lambda block while also mapping existing values that haven't changed to the new `state`.

***Awesome right?!***

The outcome is a clean `state` setter within a `functional component` that can have its interface values extended without requiring refactoring everywhere the state is set. *(As we would with the [initial approach](#the-problem-with-fixed-state-interfaces))*

## Closing remarks

Using large sets of values in a `state` object can be the most efficient way of keeping code clean and maintainable. Especially when dealing with larger state sets within components (such as forms).

As a rule of thumb, with smaller `state` values individual states can be used. These can lose context of "what they're for" as logic grows. 

Object states address this by grouping these values into a single value. Important when improving the clarity of code, also providing a "Context" to what state is.  
e.g "`formValuesState`' would hold the state of fields in a form.

One last tip to help avoid confusion is to make sure you include the actual word `State` as part of the state variable name, this is especially true with single states. i.e `nameState`, `emailState`

To help differentiate we have grouped each approach into separate examples in the project below, so you can get an overall understanding of each approach and its advantages.

Checkout it out [here](https://github.com/skalable-samples/KotlinJS-and-React-State)

@sKalable we are a Full Stack Kotlin-centric agency that create code to ensure it is consistently Maintainable, Flexible and of course,  ***sKalable***. 😎

We love to hear from the community, so if this helped feel free to get in touch or follow us on

[Twitter](https://twitter.com/skalable_dev)

[Dev.to](https://dev.to/skalabledev)  

[LinkedIn](https://www.linkedin.com/company/skalable-dev/)

to get the latest updates and strategies with Kotlin and Multiplatform for your business or personal needs.