package com.example.vkcup.crosszero

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.Button
import android.widget.EditText
import com.example.vkcup.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CrossZero : AppCompatActivity() {
    private var id: String? = null
    private var name = "Игрок"
    private val ID = "ID"
    private val NAME = "NAME"
    private val root = FirebaseDatabase.getInstance().reference.root
    private lateinit var auth: FirebaseAuth
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_zero)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        title = "Cross-Zero"
        auth = FirebaseAuth.getInstance()
        id = auth.uid
        if (id == null) {
            registration()
        }
        editText = findViewById(R.id.name)
        root.child("users").child(id.toString()).child("name")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        name = p0.value as String
                        editText.setText(name)
                        val start: Button = findViewById(R.id.start)
                        start.setOnClickListener {
                            validateName()
                            val intent = Intent(this@CrossZero, CrossZeroGame::class.java)
                            intent.putExtra(ID, id)
                            intent.putExtra(NAME, name)
                            startActivity(intent)
                        }

                        val raiting: Button = findViewById(R.id.rating)
                        raiting.setOnClickListener {
                            validateName()
                            val intent = Intent(this@CrossZero, CrossZeroRaiting::class.java)
                            startActivity(intent)
                        }
                    }
                }

            })
    }

    private fun registration() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    id = auth.uid.toString()
                    d("create user", id.toString())
                    root.child("users").child(id!!).child("win").setValue("0")
                    root.child("users").child(id!!).child("name").setValue("Игрок")
                    name = "Игрок"
                }
            }
    }

    private fun validateName(){
        if (editText.text.toString() != "Введите текст" && editText.text.isNotEmpty()){
            name = editText.text.toString()
            root.child("users").child(id.toString()).child("name").setValue(name)
        }
    }
}