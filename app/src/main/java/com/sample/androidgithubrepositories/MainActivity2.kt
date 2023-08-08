package com.sample.androidgithubrepositories

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwx.JsonWebStructure
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.X509EncodedKeySpec
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class MainActivity2 : AppCompatActivity() {
    private lateinit var button: Button
    private val base64OfEncodedDecryptionKey = "XGMkxPT4eLY/aIofj+BiHRqmaMO2x6AIxoRYNse0MYg="
    val base64OfEncodedVerificationKey =
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEswAUznJqotic9BxWGuVlShYILTE4RBqnFuJ3rOV1odDR5TJ82FBGTs2VDvxrKzIUHDEY+ILKIdTX0Sj4iOLdwA=="
    val TAG = "MainActivity2ER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            IntegrityManagerFactory.create(applicationContext).requestIntegrityToken(
                IntegrityTokenRequest.builder().setNonce(
                    Base64.encodeToString(
                        getRequestNonce(),
                        Base64.URL_SAFE or Base64.NO_WRAP
                    )
                ).build()
            ).addOnSuccessListener { response ->
                val integrityToken: String = response.token()

                Log.d(TAG, integrityToken)
                val decryptionKeyBytes: ByteArray =
                    Base64.decode(base64OfEncodedDecryptionKey, Base64.DEFAULT)
                // Deserialized encryption (symmetric) key.
                val decryptionKey: SecretKey = SecretKeySpec(
                    decryptionKeyBytes,
                    /* offset= */ 0,
                    decryptionKeyBytes.size,
                    "AES"
                )
                // base64OfEncodedVerificationKey is provided through Play Console.
                val encodedVerificationKey: ByteArray =
                    Base64.decode(base64OfEncodedVerificationKey, Base64.DEFAULT)
                // Deserialized verification (public) key.
                val verificationKey: PublicKey = KeyFactory.getInstance("EC")
                    .generatePublic(X509EncodedKeySpec(encodedVerificationKey))
                val jwe: JsonWebEncryption =
                    JsonWebStructure.fromCompactSerialization(integrityToken) as JsonWebEncryption
                jwe.key = decryptionKey
                // This also decrypts the JWE token.
                val compactJws: String = jwe.payload
                val jws: JsonWebSignature =
                    JsonWebStructure.fromCompactSerialization(compactJws) as JsonWebSignature
                jws.key = verificationKey
                // This also verifies the signature.
                val payload: String = jws.payload
                Log.d(TAG, payload);
            }
        }
    }

    private fun getRequestNonce(): ByteArray? {
        val nonceData = "epc safety Net API:" + System.currentTimeMillis()
        val random = SecureRandom()
        val byteStream = ByteArrayOutputStream()
        val bytes = ByteArray(24)
        random.nextBytes(bytes)
        try {
            byteStream.write(bytes)
            byteStream.write(nonceData.toByteArray())
        } catch (e: Exception) {
            return null
        }
        return byteStream.toByteArray()

    }
}