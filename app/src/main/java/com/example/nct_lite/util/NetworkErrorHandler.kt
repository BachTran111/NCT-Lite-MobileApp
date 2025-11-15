package com.example.nct_lite.util

import android.util.Log
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object NetworkErrorHandler {
    
    private const val TAG = "NetworkErrorHandler"
    
    /**
     * Xử lý exception và trả về thông báo lỗi thân thiện với người dùng
     */
    fun getErrorMessage(exception: Throwable): String {
        Log.e(TAG, "Network error occurred", exception)
        
        // Kiểm tra message trước để bắt các lỗi connection
        val message = exception.message ?: ""
        val lowerMessage = message.lowercase()
        
        return when {
            // SocketTimeoutException với "failed to connect" - server không chạy hoặc không thể kết nối
            (exception is SocketTimeoutException && lowerMessage.contains("failed to connect")) ||
            // Lỗi kết nối cụ thể
            exception is ConnectException || 
            lowerMessage.contains("failed to connect", ignoreCase = true) ||
            lowerMessage.contains("connection refused", ignoreCase = true) ||
            lowerMessage.contains("connection timed out", ignoreCase = true) ||
            (exception is IOException && lowerMessage.contains("failed to connect")) -> {
                "Không thể kết nối đến server.\n\n" +
                "Vui lòng kiểm tra:\n" +
                "• Server đã được khởi động chưa?\n" +
                "• Địa chỉ IP: 10.0.2.2:5000 (emulator)\n" +
                "• Firewall có chặn kết nối không?"
            }
            // SocketTimeoutException thông thường - kết nối được nhưng quá thời gian
            exception is SocketTimeoutException || 
            lowerMessage.contains("timeout", ignoreCase = true) -> {
                "Kết nối quá thời gian chờ. Server có thể đang quá tải hoặc mạng chậm. Vui lòng thử lại."
            }
            exception is UnknownHostException ||
            lowerMessage.contains("unable to resolve host", ignoreCase = true) -> {
                "Không tìm thấy server. Vui lòng kiểm tra địa chỉ IP."
            }
            exception is SSLException -> {
                "Lỗi bảo mật kết nối. Vui lòng thử lại."
            }
            exception is IOException -> {
                "Lỗi kết nối mạng. Vui lòng kiểm tra kết nối internet và thử lại."
            }
            else -> {
                if (message.isNotEmpty()) {
                    message
                } else {
                    "Đã xảy ra lỗi không xác định. Vui lòng thử lại."
                }
            }
        }
    }
    
    /**
     * Log chi tiết lỗi để debug
     */
    fun logError(tag: String, operation: String, exception: Throwable) {
        Log.e(tag, "Error in $operation: ${exception.javaClass.simpleName}", exception)
        Log.e(tag, "Error message: ${exception.message}")
        exception.printStackTrace()
    }
}

