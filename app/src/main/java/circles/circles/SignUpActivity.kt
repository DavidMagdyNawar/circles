package circles.circles

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*
import java.text.SimpleDateFormat
import android.graphics.Color
import android.view.View
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import android.util.Patterns
import android.widget.CheckBox
import android.widget.Toast
import circles.circles.retrofit.responses.SignUpResponse
import circles.circles.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import com.google.gson.Gson


class SignUpActivity : AppCompatActivity() {
    lateinit var edittext: EditText
    lateinit var myCalendar: Calendar
    private var gender: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        //card view gender selected
        female.setOnClickListener {
            radio_male.isSelected = false
            radio_male.setBackgroundResource(R.drawable.male)
            radio_female.setBackgroundResource(R.drawable.female_selected)
            gender = false

            femaleTV.textSize = 20f
            femaleTV.setTextColor(Color.BLACK)

            maleTV.setTextColor(Color.GRAY)
            maleTV.textSize = 18f

        }
        male.setOnClickListener {
            radio_female.isSelected = false
            radio_female.setBackgroundResource(R.drawable.female)
            radio_male.setBackgroundResource(R.drawable.male_selected)
            gender = true

            maleTV.textSize = 20f
            maleTV.setTextColor(Color.BLACK)

            femaleTV.setTextColor(Color.GRAY)
            femaleTV.textSize = 18f
        }
        //

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
        var checkbox_retype = findViewById<View>(R.id.check_retype_password) as CheckBox
        checkbox_retype.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // show password
                input_retype_password.transformationMethod = PasswordTransformationMethod.getInstance()

            } else {
                // hide password
                input_retype_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
        //end showing password


        //for changing icon states when edittext is not empty
        input_user_firstname.addTextChangedListener(GenericTextWatcher(input_user_firstname, 0))
        input_email.addTextChangedListener(GenericTextWatcher(input_email, 1))
        input_country.addTextChangedListener(GenericTextWatcher(input_country, 2))
        input_city.addTextChangedListener(GenericTextWatcher(input_city, 3))
        input_phone.addTextChangedListener(GenericTextWatcher(input_phone, 4))
        input_birth.addTextChangedListener(GenericTextWatcher(input_birth, 5))
        input_password.addTextChangedListener(GenericTextWatcher(input_password, 6))
        input_retype_password.addTextChangedListener(GenericTextWatcher(input_retype_password, 7))
        input_user_lastname.addTextChangedListener(GenericTextWatcher(input_user_lastname, 8))
        input_user_name_unique.addTextChangedListener(GenericTextWatcher(input_user_name_unique, 9))

        //end

        //calendar fot the birthday edittext
        myCalendar = Calendar.getInstance()
        edittext = findViewById(R.id.input_birth)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        edittext.setOnClickListener {
            // TODO Auto-generated method stub
            DatePickerDialog(this@SignUpActivity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        //calendar end


        cardview_signup.setOnClickListener {
            userSignUp()
        }
    }

    //calendar method start
    private fun updateLabel() {
        val myFormat = "dd-MM-yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        edittext.setText(sdf.format(myCalendar.time))
    }
    //calendar method end


    private fun userSignUp() {
        val firstName = input_user_firstname.text.toString().trim()
        val lastName = input_user_lastname.text.toString().trim()
        val email = input_email.text.toString().trim()
        val phone = input_phone.text.toString().trim()

        val city = input_city.text.toString().trim()
        val country = input_country.text.toString().trim()
        val birthdate = input_birth.text.toString().trim()
        val user_unique = input_user_name_unique.text.toString().trim()
//        val gender_type = gender
        val password = input_password.text.toString().trim()
        val password_retype = input_retype_password.text.toString().trim()

        if (firstName.isEmpty())
        {
            input_user_firstname.setError("name is required")
            input_user_firstname.requestFocus()
            return
        }

        if (lastName.isEmpty()) {
            input_user_lastname.setError("Name required")
            input_user_lastname.requestFocus()
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

        if (password.isEmpty()) {
            input_password.setError("Password required")
            input_password.requestFocus()
            return
        }
        if (password.length < 8) {
            input_password.setError("Password should be atleast 8 character long")
            input_password.requestFocus()
            return
        }

        if (password_retype.isEmpty()) {
            input_retype_password.setError("Password required")
            input_retype_password.requestFocus()
            return
        }
        if (password_retype.length < 8) {
            input_retype_password.setError("Password should be atleast 8 character long")
            input_retype_password.requestFocus()
            return
        }
        if (password_retype != password) {
            input_retype_password.setError("Passwords should match")
            input_password.setError("Passwords should match")
            return
        }

        if (phone.isEmpty()) {
            input_phone.setError("Phone required")
            input_phone.requestFocus()
            return
        }
        if(terms_concitions.isChecked==false)
        {
            Toast.makeText(this@SignUpActivity,"Please confirm our terms and conditions",Toast.LENGTH_SHORT).show()
            return
        }


        var call: Call<SignUpResponse> = RetrofitClient
                .getInstance()
                .api
                .createUser(firstName, lastName, email, phone, city, country, birthdate,
                        user_unique, gender, password, password_retype)

        call.enqueue(object : Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>?, t: Throwable?) {
                Toast.makeText(this@SignUpActivity, "OnFailure", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<SignUpResponse>?, response: Response<SignUpResponse>?) {

                if (response!!.isSuccessful && response.code() == 201)
                {
                    var s = response.body()!!.message
                    Toast.makeText(this@SignUpActivity, s, Toast.LENGTH_LONG).show()
                    // Check if the email is confirmed and User could sing up first
                    var intent  = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)



                }
                else {
                    try{
                        var signUpResponse: SignUpResponse

                    var gson: Gson = Gson()
                    signUpResponse = gson.fromJson(response.errorBody()!!.string(), SignUpResponse::class.java)


                        var validation = ""
                        if (signUpResponse.errors.email.size!=0) {
                            validation += signUpResponse.errors.email[0]
                        }
                        if (signUpResponse.errors.phone. size!=0) {
                            validation += signUpResponse.errors.phone[0]
                        }
                        if (signUpResponse.errors.username. size!=0) {
                            validation += signUpResponse.errors.username[0]
                        }
                        if(validation!= "") {
                            Toast.makeText(this@SignUpActivity, validation, Toast.LENGTH_LONG).show()
                        }
                    }
                    catch (e: IOException)
                    {
                    Toast.makeText(this@SignUpActivity,e.message,Toast.LENGTH_SHORT).show()
                    }
                }


            }

        })


    }

}