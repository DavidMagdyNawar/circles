package circles.circles

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.activity_login_sign_up.*

class LoginSignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
           var EMAIL = "email"

        

        //Full screen for no status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        cardview_signup.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        cardview_login.setOnClickListener{

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        var callbackManager = CallbackManager.Factory.create()
    }
}
