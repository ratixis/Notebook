package com.example.recyclerviewappintent

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView


class MainActivityDetail : AppCompatActivity() {

//    companion object{
//        const val USER_ENTITY_ID_DETAIL = "USER_ENTITY_ID_DETAIL"
//    }

    private val phoneBookDatabase by lazy {
        PhoneBookDatabase.getDatabase(this).userDao()
    }

    private fun transferAnotherActivity(context: Context, activityClass: Class<*>, id: Long? = null){
        val intent = Intent(context, activityClass)

        if (id != null){
            intent.putExtra(MainActivity1.USER_ENTITY_ID, id)
        }

        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_detail)


        val id = intent.getIntExtra(MainActivity1.USER_ENTITY_ID,-1)

        val textViewFN = findViewById<TextView>(R.id.textViewFirstNameUser)
        val textViewLN = findViewById<TextView>(R.id.textViewLastNameUser)
        val textViewB = findViewById<TextView>(R.id.textViewBirthdayUser)
        val textViewP = findViewById<TextView>(R.id.textViewPhoneUser)

        val userEntity = phoneBookDatabase.getById(id.toLong())

        textViewFN.text =  userEntity.firstName
        textViewLN.text =  userEntity.lastName
        textViewB.text =  userEntity.birthday
        textViewP.text =  userEntity.phone

        val buttonBack = findViewById<Button>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            transferAnotherActivity(this, MainActivity1::class.java)
        }

        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        buttonDelete.setOnClickListener{
            phoneBookDatabase.delete(userEntity)
            transferAnotherActivity(this, MainActivity1::class.java)
        }

        val buttonEdit = findViewById<Button>(R.id.buttonEdit)

        buttonEdit.setOnClickListener {
            transferAnotherActivity(this, MainActivity2::class.java, id.toLong())
        }

        textViewP.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:${textViewP.text}")
            startActivity(intent)
        }
    }
}