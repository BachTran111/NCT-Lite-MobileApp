package com.example.nct_lite.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

object ErrorDisplayHelper {
    
    /**
     * Hiển thị lỗi bằng Toast, tự động rút ngắn message nếu quá dài
     */
    fun showErrorToast(context: Context, message: String) {
        // Rút ngắn message nếu quá dài để Toast hiển thị tốt hơn
        val displayMessage = if (message.length > 100) {
            message.take(100) + "..."
        } else {
            message
        }
        Toast.makeText(context, displayMessage, Toast.LENGTH_LONG).show()
    }
    
    /**
     * Hiển thị lỗi bằng AlertDialog cho message dài và quan trọng
     */
    fun showErrorDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    
    /**
     * Hiển thị lỗi kết nối với AlertDialog (message dài, cần hướng dẫn chi tiết)
     */
    fun showConnectionError(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("Lỗi kết nối")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}

