package com.example.dicodingstory.base.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.dicodingstory.R
import com.example.dicodingstory.utils.helper.isEmailCorrect
import com.google.android.material.textfield.TextInputEditText

class customEmailEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s.isNotEmpty() && !s.toString().isEmailCorrect())
                    context.getString(R.string.email_check)
                else null
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
        setSingleLine()
    }
}