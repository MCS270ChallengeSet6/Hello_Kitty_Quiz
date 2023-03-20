package com.example.hellokittyquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.example.hellokittyquiz.databinding.ActivityMainBinding
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

private lateinit var true_button:Button
private lateinit var false_button: Button


private const val TAG = "MainActivity"

//Cody Korman




class MainActivity : AppCompatActivity() {
    private val quizViewModel: QuizViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        //Handle results
        if(result.resultCode == Activity.RESULT_OK){
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_IS_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // hook up the button to its id
        true_button = findViewById(R.id.true_button)
        false_button = findViewById(R.id.false_button)

        Log.d(TAG, "QuizViewModel created.")


        // what happen if you click on those buttons
        binding.trueButton.setOnClickListener { view: View ->
            // Do something if you click on true button
            // have a correct toast that pops up
            if(quizViewModel.answeredQuestion()){
                checkAnswer(true)
            }else{
                //Toast.makeText(this, "ALREADY ANSWERED!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.falseButton.setOnClickListener { view: View ->
            if(quizViewModel.answeredQuestion()){
            checkAnswer(false)
            }else{
                //Toast.makeText(this, "ALREADY ANSWERED!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.questionTextView.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener{
            //start cheat activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer

            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            quizViewModel.prevCheats.add(quizViewModel.currentIndex)//whoops this is wrong. Cheat status can be set on one question, then false flags on launching cheatActivity
            //startActivity(intent)
            cheatLauncher.launch(intent)
        }


        updateQuestion()
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

       val messageResId = when {
           quizViewModel.isCheater && quizViewModel.prevCheats.contains(quizViewModel.currentIndex) -> R.string.judgement_string
           userAnswer == correctAnswer -> {
               quizViewModel.scorePlus()
               R.string.correct_string
           }
           else -> {
               R.string.incorrect_string
           }

       }
    //if (userAnswer == correctAnswer){
//            quizViewModel.scorePlus()
//            R.string.correct_string
//        }else{
//            //quizViewModel.scoreMinus()
//            R.string.incorrect_string


        //Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        Snackbar.make(true_button, messageResId, Snackbar.LENGTH_SHORT).show()

        if(quizViewModel.ifFinished()){
            val given = quizViewModel.quizResults()
            Toast.makeText(this, given, Toast.LENGTH_SHORT).show()
        }
    }


}