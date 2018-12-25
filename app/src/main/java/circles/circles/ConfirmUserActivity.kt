package circles.circles

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import circles.circles.retrofit.RetrofitClient
import circles.circles.retrofit.responses.ConfirmEmailResponse
import kotlinx.android.synthetic.main.activity_confirm_user.*
import circles.circles.Home
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

 class ConfirmUserActivity : AppCompatActivity() {
    lateinit var accessToken:String
    lateinit var tokenType:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_user)

        input_email.addTextChangedListener( GenericTextWatcher(input_email,0))
        input_code_confirmation.addTextChangedListener( GenericTextWatcher(input_code_confirmation,6))
        var checkbox = findViewById<View>(R.id.check_password) as CheckBox
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // show password
                input_code_confirmation.transformationMethod = PasswordTransformationMethod.getInstance()

            } else {
                // hide password
                input_code_confirmation.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }


        
        accessToken=intent.getStringExtra("ACCESS_TOKEN")
         tokenType=intent.getStringExtra("TOKEN_TYPE")



        cardview_login.setOnClickListener {
            emailConfirmation()
        }
    }
        private fun emailConfirmation() {
            val email = input_email.text.toString().trim()
            val code = input_code_confirmation.text.toString().trim()

            var call: Call<ConfirmEmailResponse> = RetrofitClient
                .getInstance()
                .api
                .confirmEmail(email,code)

        call.enqueue(object : Callback<ConfirmEmailResponse> {
            override fun onFailure(call: Call<ConfirmEmailResponse>?, t: Throwable?) {
                Toast.makeText(this@ConfirmUserActivity, "OnFailure", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<ConfirmEmailResponse>?, response: Response<ConfirmEmailResponse>?) {

                if (response!!.isSuccessful)
                {
                    var s = response.body()!!.message
                    Toast.makeText(this@ConfirmUserActivity, s, Toast.LENGTH_LONG).show()

                    var dataIntent = Intent(this@ConfirmUserActivity, Home::class.java)
                    dataIntent.putExtra("ACCESS_TOKEN", accessToken)
                    dataIntent.putExtra("TOKEN_TYPE", tokenType)
                    startActivity(dataIntent)

//
//                    var loginResponse :LoginResponse = response.body()!!
//
//                    var accesTokeString = loginResponse.accessToken
//                    var tokenType = loginResponse.tokenType
//
//                    var dataIntent = Intent(this@LoginActivity, Home::class.java)
//                    dataIntent.putExtra("ACCESS_TOKEN", accesTokeString)
//                    dataIntent.putExtra("TOKEN_TYPE", tokenType)
//                    startActivity(dataIntent)
//
//                    val bundle = Bundle()
//                    bundle.putString("ACCESS_TOKEN", accesTokeString)
//                    bundle.putString("TOKEN_TYPE", tokenType)
//                    val homeFragment = HomeFragment()

                }
                else if(response.code() == 401) {
                    try{
                        Toast.makeText(this@ConfirmUserActivity, "Check your email and password then try again", Toast.LENGTH_LONG).show()

                    }
                    catch (e: IOException)
                    {
                        Toast.makeText(this@ConfirmUserActivity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }


            }

        })

    }

}
