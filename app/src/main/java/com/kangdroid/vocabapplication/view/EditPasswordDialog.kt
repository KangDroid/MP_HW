package com.kangdroid.vocabapplication.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.user.UserDto
import com.kangdroid.vocabapplication.data.entity.user.UserSession
import com.kangdroid.vocabapplication.data.repository.UserRepository
import com.kangdroid.vocabapplication.databinding.DialogEditPasswordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Exception to this dialog:
 * Allow all business model and logic to be in this class. - Running out of time
 */
class EditPasswordDialog(
    context: Context,
    private val userDatabase: UserRepository
) : Dialog(context) {

    private val dialogEditPasswordBinding: DialogEditPasswordBinding by lazy {
        DialogEditPasswordBinding.inflate(layoutInflater)
    }
    private val passwordRegex: Regex = """^(?=.*[@#$%!\-_?&])(?=\S+$).*""".toRegex()

    private var isCurrentCorrect: Boolean = false
    private var isPasswordValidation: Boolean = false
    private var isPasswordCheckValidation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialogEditPasswordBinding.root)
        setTitle(dialogEditPasswordBinding.titleCredential.text)

        dialogEditPasswordBinding.editNegative.setOnClickListener {
            dismiss()
        }

        dialogEditPasswordBinding.editPositive.setOnClickListener {
            val isOkToGo: Boolean =
                isCurrentCorrect && isPasswordCheckValidation && isPasswordCheckValidation

            if (isOkToGo) {
                val userChangedPassword: String =
                    dialogEditPasswordBinding.newPasswordInput.editText?.text.toString()
                val userDto: UserDto = UserSession.currentUser ?: run {
                    Log.e(this::class.java.simpleName, "Cannot change userdata. It is null!")
                    return@setOnClickListener
                }
                userDto.userPassword = userChangedPassword
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        userDatabase.updateUser(userDto)
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Please check fields and try again",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            dismiss()
        }

        dialogEditPasswordBinding.currentPasswordInput.editText?.addTextChangedListener {
            val userDto: UserDto = UserSession.currentUser!!
            when {
                userDto.userPassword != it?.trim().toString() -> {
                    dialogEditPasswordBinding.currentPasswordInput.error =
                        context.getString(R.string.password_does_not_match)
                    isCurrentCorrect = false
                }
                else -> {
                    dialogEditPasswordBinding.currentPasswordInput.error = null
                    dialogEditPasswordBinding.currentPasswordInput.isErrorEnabled = false
                    isCurrentCorrect = true
                }
            }
        }

        dialogEditPasswordBinding.newPasswordInput.editText?.addTextChangedListener {
            val userPassword: String = it?.trim().toString()
            when {
                (userPassword.length < 8) -> {
                    dialogEditPasswordBinding.newPasswordInput.error =
                        context.getString(R.string.password_length_short)
                    isPasswordValidation = false
                }
                !passwordRegex.matches(userPassword) -> {
                    dialogEditPasswordBinding.newPasswordInput.error =
                        context.getString(R.string.password_no_special)
                    isPasswordValidation = false
                }
                else -> {
                    dialogEditPasswordBinding.newPasswordInput.error = null
                    dialogEditPasswordBinding.newPasswordInput.isErrorEnabled = false
                    isPasswordValidation = true
                }
            }
        }

        dialogEditPasswordBinding.inputNewPasswordCheckInput.editText?.addTextChangedListener {
            val inputText: String = it?.trim().toString()
            val targetText: String =
                dialogEditPasswordBinding.newPasswordInput.editText?.text?.trim().toString()

            when {
                inputText != targetText -> {
                    dialogEditPasswordBinding.inputNewPasswordCheckInput.error =
                        context.getString(R.string.password_does_not_match)
                    isPasswordCheckValidation = false
                }
                else -> {
                    dialogEditPasswordBinding.inputNewPasswordCheckInput.error = null
                    dialogEditPasswordBinding.inputNewPasswordCheckInput.isErrorEnabled = false
                    isPasswordCheckValidation = true
                }
            }
        }
    }
}