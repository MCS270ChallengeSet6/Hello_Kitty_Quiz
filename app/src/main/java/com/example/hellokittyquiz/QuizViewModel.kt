package com.example.hellokittyquiz

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
private const val  TAG = "QuizViewModel"
const val  IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle):ViewModel() {
    /*init {
        Log.d(TAG, "ViewModel Instance is created")
    }
    override fun onCleared(){
        super.onCleared()
        Log.d(TAG, "ViewModel Instance is about to be destroyed!!!")
    }*/

    private val questionBank= listOf(
        Question(R.string.question_1, true),
        Question(R.string.question_2, true),
        Question(R.string.question_3, false),
        //add more questions????
    )
    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?:false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var prevAnswers: MutableList<Int> = mutableListOf()
    var prevCheats: MutableList<Int> = mutableListOf()

    var currentIndex = 0 //was private?
    private var totalScore = 0

    val currentQuestionAnswer: Boolean
    get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
    get() = questionBank[currentIndex].textResId

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        currentIndex -= 1
        if (currentIndex < 0) currentIndex = questionBank.size - 1
    }
    fun answeredQuestion():Boolean{
        if(!prevAnswers.contains(currentIndex)){
            prevAnswers.add(currentIndex)
            return true
        }else{
            return false
        }
    }
    fun scorePlus(){
        totalScore++
    }
    //fun scoreMinus(){
    //    totalScore--
    //}

    fun ifFinished(): Boolean{
        return prevAnswers.size >= questionBank.size
    }

    fun quizResults(): String{
        var tot = totalScore/questionBank.size.toDouble()
        tot *= 100
        tot = tot.toInt().toDouble()

        val returned = StringBuilder("Score: ").append(tot).append("%").toString()
        //Toast.makeText(this, returned, Toast.LENGTH_SHORT )
        return returned
    }
}