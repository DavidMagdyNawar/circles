package circles.circles

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import circles.circles.retrofit.RetrofitClient
import circles.circles.retrofit.responses.ConfirmEmailResponse
import circles.circles.retrofit.responses.LoginResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import android.support.v4.content.LocalBroadcastManager



class LoginActivity : AppCompatActivity() {
//    private val ARG_ACCESS_TOKEN = "ACCESS_TOKEN"
//    private val ARG_TOKEN_TYPE = "TOKEN_TYPE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //on click sign up text view move to sign up activity
        val signupTV = findViewById<TextView>(R.id.signup_textview)
        signupTV.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        //end

        //for changing icon states when edittext is not empty
        input_email.addTextChangedListener( GenericTextWatcher(input_email,0))
        input_password.addTextChangedListener( GenericTextWatcher(input_password,6))
        //end


        //show password
        var checkbox = findViewById<View>(R.id.check_password) as CheckBox
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // show password
                input_password.transformationMethod = PasswordTransformationMethod.getInstance()

            } else {
                // hide password
                input_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
        //end showing password

        cardview_login.setOnClickListener{
            LoginViaEmail()

        }
    }

    fun LoginViaEmail(){


        val email = input_email.text.toString().trim()
        val passworde = input_password.text.toString().trim()

        if (passworde.isEmpty()) {
            input_password.setError("Password required")
            input_password.requestFocus()
            return
        }
        if (passworde.length < 8) {
            input_password.setError("Password should be atleast 8 character long")
            input_password.requestFocus()
            return
        }
        if (email.isEmpty()) {
            input_email.setError("Email is required")
            input_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Enter a valid email")
            input_email.requestFocus()
            return
        }

        var call: Call<LoginResponse> = RetrofitClient
                .getInstance()
                .api
                .login(email, passworde)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Toast.makeText(this@LoginActivity, "OnFailure", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {

                if (response!!.isSuccessful)
                {

                    var s = response.body()!!.message
                    Toast.makeText(this@LoginActivity, s, Toast.LENGTH_LONG).show()

                    var loginResponse:LoginResponse = response.body()!!
                    var accessToken = loginResponse.accessToken
                    var tokenType = loginResponse.tokenType

                    var dataIntent = Intent(this@LoginActivity, Home::class.java)
                    dataIntent.putExtra("ACCESS_TOKEN", accessToken)
                    dataIntent.putExtra("TOKEN_TYPE", tokenType)
                    startActivity(dataIntent)



                }

                else if(response.code() == 401) {
                    try{
                            Toast.makeText(this@LoginActivity, "Check your email and password then try again", Toast.LENGTH_LONG).show()

                    }
                    catch (e: IOException)
                    {
                        Toast.makeText(this@LoginActivity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }


            }

        })
    }



}
