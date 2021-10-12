package com.kaspin.util

import android.content.Context
import android.widget.Toast

object CommonUtil {
    fun tryParseInt(number: String): Boolean {
        try {
            Integer.parseInt(number)
            return true
        } catch (e: Exception) {
            return false
        }

    }
}