import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile.HomeActivity
import com.example.mobile.R
import com.example.mobile.network.ApiClient
import com.example.mobile.network.ApiService
import com.example.mobile.network.LoginRequest
import com.example.mobile.network.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query


private val Throwable.messagem: String

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailEditText)
        val passwordInput = findViewById<EditText>(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        val api = ApiClient.retrofit.create(ApiService::class.java)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val call = api.login(LoginRequest(email, password))
            call.enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call : retrofit2.Call<LoginResponse>,
                    response : retrofit2.Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val token = "Bearer ${loginResponse?.token}"

                        //salva token
                        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        prefs.edit().putString("auth_token", token).apply()

                        Toast.makeText(this@LoginActivity, "Login realizado", Toast.LENGTH_SHORT).show()

                        //Vai para HomeActivity
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()

                    }
                    else {
                        Toast.makeText(this@LoginActivity, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call:retrofit2.Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Erro: ${t.messagem}", Toast.LENGTH_SHORT).show()
                }
            })

        }

    }


}
