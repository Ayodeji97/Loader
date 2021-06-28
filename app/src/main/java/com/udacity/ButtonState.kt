package com.udacity

// Button State
sealed class ButtonState (var buttonState : Int){
    object Clicked : ButtonState(R.string.button_clicked)
    object Loading : ButtonState(R.string.button_loading)
    object Completed : ButtonState(R.string.button_completed)
}