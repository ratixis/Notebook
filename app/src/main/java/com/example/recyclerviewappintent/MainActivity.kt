package com.example.recyclerviewappintent

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity1 : AppCompatActivity() {



    private val phoneBookDatabase by lazy {
         PhoneBookDatabase.getDatabase(this).userDao()
    }

    companion object {

        const val USER_ENTITY_ID = "ENTITY_ID"
    }

    lateinit var adapter: RecyclerAdapter;
    var listItem = mutableListOf<UserEntity>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("адресная книга")

        val userDao = phoneBookDatabase

        listItem.addAll(userDao.all)

        adapter = RecyclerAdapter(listItem, { updateId ->
            val intent = Intent(this, MainActivityDetail::class.java)
            intent.putExtra(USER_ENTITY_ID, listItem[updateId].id)
            startActivity(intent)
            finish()
        }, { indexList ->
            userDao.delete(listItem[indexList])
            listItem.removeAt(indexList)
            adapter.notifyItemRemoved(indexList)
        })


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val editText = findViewById<EditText>(R.id.searchContact)
        val buttonAdd = findViewById<Button>(R.id.buttonAddContact)

        buttonAdd.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        editText.doAfterTextChanged {
            adapter.updateAdpater(listItem.filter {
                    editText.text.toString().lowercase() in it.firstName.lowercase() ||
                    editText.text.toString().lowercase() in it.lastName.lowercase()
            })
            adapter.run { notifyDataSetChanged() }
        }
    }



}