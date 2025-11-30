package com.example.nct_lite.di
import androidx.room.Room
import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.LocalDatabase
import android.content.Context
import com.example.nct_lite.data.auth.AuthLocalDataSource
import com.example.nct_lite.data.auth.AuthRemoteDataSource
import com.example.nct_lite.data.auth.AuthRepository

class AppContainer (context: Context) {

    private val retrofit = ApiClient.retrofit
    private val db = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        "app_db"
    ).build()

//    private val localDataSource = AuthLocalDataSource(db.userDao())
    private val remoteDataSource = AuthRemoteDataSource(ApiClient.authApi)

    val authRepository = AuthRepository(remoteDataSource)

    // USER DOMAIN
//    private val userRemote = UserRemoteDataSource(retrofit)
//    private val userLocal = UserLocalDataSource()
//    val userRepository = UserRepository(userLocal, userRemote)

    // MACHINE DOMAIN
//    private val machineRemote = MachineRemoteDataSource(retrofit)
//    val machineRepository = MachineRepository(machineRemote)

    // HISTORY DOMAIN
//    private val historyRemote = HistoryRemoteDataSource(retrofit)
//    val historyRepository = HistoryRepository(historyRemote)

    // LOGIN FLOW container
//    var loginContainer: LoginContainer? = null
}




