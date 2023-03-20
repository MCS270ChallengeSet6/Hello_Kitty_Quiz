package com.example.hellokittyquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hellokittyquiz.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_IS_TRUE = "com.example.hellokittyquiz.answer_is_true"
const val EXTRA_ANSWER_IS_SHOWN = "com.example.hellokittyquiz.answer_is_shown"

class CheatActivity : AppCompatActivity() {

    private val cheatViewModel: CheatViewModel by viewModels()


    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        val answerText = when{
               answerIsTrue -> R.string.true_string    //true_button
                else -> R.string.false_string   //false_button
           }

        binding.showAnswerButton.setOnClickListener{
//            val answerText = when{
//                answerIsTrue -> R.string.true_string    //true_button
//                else -> R.string.false_string   //false_button
//            }
            cheatViewModel.clicked = true
            binding.answerTextView.setText(answerText)
            setAnswerShownResult(cheatViewModel.clicked)


        }
        if(cheatViewModel.clicked){
            binding.answerTextView.setText(answerText)
            setAnswerShownResult(cheatViewModel.clicked)
        }


    }

    private fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply{
            putExtra(EXTRA_ANSWER_IS_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent{
            return Intent(packageContext, CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

}