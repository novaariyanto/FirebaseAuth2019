package kejarkoding.com.firebaseauth2019

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var providers:List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE: Int = 7117 // Any number you want

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(), //Email Login
            AuthUI.IdpConfig.FacebookBuilder().build(), //facebook login
            AuthUI.IdpConfig.GoogleBuilder().build(), //google login
            AuthUI.IdpConfig.PhoneBuilder().build() // phone login
        )
        showSigninOptions()

        btn_signout.setOnClickListener{
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    btn_signout.isEnabled = false
                    showSigninOptions()
                }
                .addOnFailureListener {
                    e-> Toast.makeText(this@MainActivity,e.message , Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,""+user?.email,Toast.LENGTH_LONG).show()
                btn_signout.isEnabled = true
            }else{
                Toast.makeText(this,""+response?.error?.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun showSigninOptions(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),MY_REQUEST_CODE
        )
    }
}
