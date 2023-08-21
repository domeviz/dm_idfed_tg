package com.vizcarra.practicauceidfed

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.vizcarra.practicauceidfed.databinding.ActivityMainBinding

enum class ProviderType{
    GOOGLE
}
class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN= 100
    private lateinit var binding:ActivityMainBinding
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs =getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.apply()

        binding.button.setOnClickListener {
            val googleConf=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()

            val googleClient=GoogleSignIn.getClient(this,googleConf)
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
//        binding.button.setOnClickListener {
//            oneTapClient = Identity.getSignInClient(this)
//            signInRequest = BeginSignInRequest.builder()
//                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
//                    .setSupported(true)
//                    .build())
//                .setGoogleIdTokenRequestOptions(
//                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.clave))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                // Automatically sign in when exactly one credential is retrieved.
//                .setAutoSelectEnabled(true)
//                .build()
//
//            // Iniciar el proceso de autenticación
//            oneTapClient.beginSignIn(signInRequest)
//                .addOnSuccessListener(this) { result ->
//                    try {
//                        startIntentSenderForResult(
//                            result.pendingIntent.intentSender, REQ_ONE_TAP,
//                            null, 0, 0, 0, null)
//                    } catch (e: IntentSender.SendIntentException) {
//                        Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                    }
//                }
//                .addOnFailureListener(this) { e ->
//                    // No saved credentials found. Launch the One Tap sign-up flow, or
//                    // do nothing and continue presenting the signed-out UI.
//                    Log.d(TAG, e.localizedMessage)
//                }
//        }

    }
    companion object {
        private const val REQ_ONE_TAP = 1
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            // Manejar la respuesta de One Tap aquí
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            val account=task.getResult(ApiException::class.java)
            if(account!=null){
                val credential=GoogleAuthProvider.getCredential(account.idToken,null)
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(
                            baseContext,
                            "Authentication success.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }else{
                        Toast.makeText(
                            baseContext,
                            "Nel",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }

        }
    }

}