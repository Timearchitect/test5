package com.timearchitect.test5

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import kotlinx.coroutines.launch

import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey


class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    public var dateData = "before...."
    val TAG: String = "Alrik"

    val Context.dataStore by preferencesDataStore(name = "settings")
    val USERNAME = stringPreferencesKey("username")
    val SCORE = intPreferencesKey("score")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        var dateSelectEditText = findViewById<EditText>(R.id.editTextDate2)

        // SAVE via dataStore
        lifecycleScope.launch {
            // ändra via dataStore
            dataStore.edit {
                it[USERNAME] = "Anna"
            }

            dataStore.data.collect { preferences ->

                val username = preferences[USERNAME] ?: "Unknown"
                val score = preferences[SCORE] ?: 0

                Log.i("ALRIK", "onCreate: ${username}")
                Log.i("ALRIK", "onCreate: ${score}")

            }

        }



        dateSelectEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val datePickerDialog = DatePickerDialog(
                    // on below line we are passing context.
                    this, { _: DatePicker, year: Int, month: Int, day: Int ->
                        dateData = "date:${year}-${month}-${day}"
                        Log.d("Alrik", dateData)
                    },
                    2023,
                    9,
                    7
                )
                datePickerDialog.show()
                dateSelectEditText.clearFocus()
                dateSelectEditText.setText(dateData)
            }
        }


        database = Firebase.database.reference
        database.child("users/user3/password").get().addOnSuccessListener {
            Log.i("Alrik", "Got value ${it.value}")
        }.addOnFailureListener {
            Log.e("Alrik", "Error getting data", it)
        }

        database.child("users/user2").removeValue().addOnSuccessListener {
            Log.i("Alrik", "deleted")
        }.addOnFailureListener {
            Log.e("Alrik", "Error getting data", it)
        }

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)
            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) { // när någont värde ändras
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key} : ${dataSnapshot.value}")
                Toast.makeText(this@MainActivity, "data CHANGED", Toast.LENGTH_SHORT).show()
                vibrate()
                playSound()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(
                    this@MainActivity,
                    "Failed to load comments.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
        var usersPath: DatabaseReference = database.child("users") //kollar på alla users i db
        usersPath.addChildEventListener(childEventListener) //lyssnar på om värden ändras/raderas/flyttas

        writeNewUser() //skapar users/user3/123

        var btn: Button = findViewById<Button>(R.id.button)
        Log.i("ALRIK", "BUTTON!!!")
        var first = false


        btn.setOnClickListener {
            vibrate() // vibrera
            dateSelectEditText.setText(dateData)
            // Läsa username via dataStore
            lifecycleScope.launch {
                dataStore.data.collect {
                    val username = it[USERNAME] ?: "No name"
                    Log.i("ALRIK", "onCreate: ${username}")

                }
            }
            if (first) {
                Log.i("ALRIK", "BUTTON!!! 1")
                supportFragmentManager.commit {
                    replace<BlankFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack("name") // Name can be null
                }
            } else {
                Log.i("ALRIK", "BUTTON!!! 2")
                supportFragmentManager.commit {
                    replace<BlankFragment2>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack("name") // Name can be null
                }
            }
            first = !first; //flip bool
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun writeNewUser() {
        var obj = HashMap<String, String>()
        obj["username"] = "Bea"
        obj["password"] = "teddy!123"

        database.child("users").child("user3").setValue(obj)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v.vibrate(1000)
        }
    }

    fun playSound() {
        var mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.pickup_coin)
        mediaPlayer.start() // no need to call prepare(); create() does that for you
    }

}