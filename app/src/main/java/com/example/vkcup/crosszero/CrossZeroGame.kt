package com.example.vkcup.crosszero

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vkcup.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Thread.sleep
import java.util.concurrent.Executors

class CrossZeroGame : AppCompatActivity() {
    private val playForCross = "play_for_cross"
    private val turn = "turn"
    private val cross = "cross"
    private val zero = "zero"

    private var game = Array(3) { Array(3) { 0 } }
    private var curPos = 0
    private var id: String? = null
    private var name: String? = null
    private var op: String? = null
    private val ID: String = "ID"
    private val NAME: String = "NAME"

    private var isPlayCross = false
    private lateinit var turnText: TextView
    private lateinit var scoreOp: TextView
    private lateinit var scoreMe: TextView
    private lateinit var view: View
    private lateinit var allSquares: Array<ImageView>
    private val root = FirebaseDatabase.getInstance().reference.root

    private val find = "Ищем 2-го игрока"
    private val find1 = "Ищем 2-го игрока."
    private val find2 = "Ищем 2-го игрока.."
    private val find3 = "Ищем 2-го игрока..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_zero_game)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        id = intent.getStringExtra(ID)
        name = intent.getStringExtra(NAME)
        view = findViewById(R.id.me_circle)
        turnText = findViewById(R.id.turn)
        scoreMe = findViewById(R.id.me)
        scoreOp = findViewById(R.id.score)
        title = "Game"
        val sq1: ImageView = findViewById(R.id.sq1)
        val sq2: ImageView = findViewById(R.id.sq2)
        val sq3: ImageView = findViewById(R.id.sq3)
        val sq4: ImageView = findViewById(R.id.sq4)
        val sq5: ImageView = findViewById(R.id.sq5)
        val sq6: ImageView = findViewById(R.id.sq6)
        val sq7: ImageView = findViewById(R.id.sq7)
        val sq8: ImageView = findViewById(R.id.sq8)
        val sq9: ImageView = findViewById(R.id.sq9)
        val submit: Button = findViewById(R.id.submit)
        submit.visibility = View.INVISIBLE
        allSquares = arrayOf(sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9)
        clearAll()
        getOp()
        submit.setOnClickListener {
            if (turnText.text == "Твой ход!") {
                val posStr: Int = curPos / 3
                val posCol: Int = curPos % 3
                d("pos", curPos.toString())
                d("cross", getBinaryFormat(1).toString())
                d("zero", getBinaryFormat(-1).toString())
                val a = if (isPlayCross) {
                    1
                } else {
                    -1
                }
                game[posStr][posCol] = a
                d("cross", getBinaryFormat(1).toString())
                d("zero", getBinaryFormat(-1).toString())
                allSquares[curPos].isClickable = false
                root.child("users").child(id.toString()).child(turn).setValue("0")
                if (check(a)) {
                    win()
                } else {
                    if (endGame()) {
                        draw()
                    } else {
                        root.child("users").child(id.toString()).child(cross)
                            .setValue(getBinaryFormat(1).toString())
                        root.child("users").child(id.toString()).child(zero)
                            .setValue(getBinaryFormat(-1).toString())
                        root.child("users").child(op.toString()).child(cross)
                            .setValue(getBinaryFormat(1).toString())
                        root.child("users").child(op.toString()).child(zero)
                            .setValue(getBinaryFormat(-1).toString())
                        root.child("users").child(op.toString()).child(turn).setValue("1")
                        submit.isClickable = false
                    }
                }
                submit.visibility = View.INVISIBLE
            }
        }

        root.child("users").child(id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        val map = p0.value as Map<String, String>
                        if (map.keys.contains("op")) {
                            op = map["op"]
                            val isYourTurn = map[turn] == "1"
                            isPlayCross = map[playForCross] == "1"
                            if (isYourTurn) {
                                turnText.text = "Твой ход!"
                                val curCross = Integer.parseInt(map[cross])
                                val curZero = Integer.parseInt(map[zero])
                                update(curCross, curZero)
                                submit.visibility = View.VISIBLE
                                val a = if (isPlayCross) {
                                    1
                                } else {
                                    -1
                                }
                                if (check(-a)) {
                                    lose()
                                } else {
                                    if (endGame()) {
                                        draw()
                                    }
                                }

                                for (i in 0 until 9) {
                                    allSquares[i].setOnClickListener {
                                        val j = i / 3
                                        val k = i % 3
                                        if (game[j][k] == 0) {
                                            clearAllNotActiveCross()
                                            if (isPlayCross) {
                                                allSquares[i].setImageResource(R.drawable.cross)
                                            } else {
                                                allSquares[i].setImageResource(R.drawable.zero)
                                            }
                                            curPos = i
                                            submit.isClickable = true
                                        }
                                    }
                                }
                            } else {
                                turnText.text = "Ходит соперник"
                            }
                        } else {
                            waitText()
                        }
                    }
                }

            })
    }

    private fun waitText() {
        turnText.text = find
        val pool = Executors.newSingleThreadExecutor()
        pool.submit {
            val turnText: TextView = findViewById(R.id.turn)
            while (true) {
                if (turnText.text == find) {
                    turnText.text = find1
                    sleep(500)
                }
                if (turnText.text == find1) {
                    turnText.text = find2
                    sleep(500)
                }
                if (turnText.text == find2) {
                    turnText.text = find3
                    sleep(500)
                }
                if (turnText.text == find3) {
                    turnText.text = find
                    sleep(500)
                }
                if (turnText.text != find &&
                    turnText.text != find1 &&
                    turnText.text != find2 &&
                    turnText.text != find3
                ) {
                    break
                }
            }
        }
    }

    private fun update(curCross1: Int, curZero1: Int) {
        var curCross = curCross1
        var curZero = curZero1
        var pos = 8
        while (curCross != 0) {
            if (curCross % 2 == 1) {
                val i = pos / 3
                val j = pos % 3
                game[i][j] = 1
            }
            curCross /= 2
            pos--
        }
        pos = 8
        while (curZero != 0) {
            if (curZero % 2 == 1) {
                val i = pos / 3
                val j = pos % 3
                game[i][j] = -1
            }
            curZero /= 2
            pos--
        }
        for (i in 0 until 9) {
            val posStr: Int = i / 3
            val posCol: Int = i % 3
            if (game[posStr][posCol] == 1) {
                allSquares[i].setImageResource(R.drawable.cross)
                allSquares[i].isClickable = false
            }
            if (game[posStr][posCol] == -1) {
                allSquares[i].setImageResource(R.drawable.zero)
                allSquares[i].isClickable = false
            }
        }
    }

    private fun getOp() {
        root.child("players")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        val str = p0.value as String
                        val op = str.split(" ")[0]
                        val nam = str.split(" ")[1]
                        if (op != id) {
                            val opponent: TextView = findViewById(R.id.opponent)
                            opponent.text = nam
                            root.child("players").removeValue()
                            root.child("users").child(id.toString()).child(turn).setValue("0")
                            root.child("users").child(id.toString()).child("op").setValue(op)
                            root.child("users").child(id.toString()).child(playForCross)
                                .setValue("0")
                            root.child("users").child(id.toString()).child(cross).setValue("0")
                            root.child("users").child(id.toString()).child(zero).setValue("0")
                            root.child("users").child(op).child(playForCross).setValue("1")
                            root.child("users").child(op).child(cross).setValue("0")
                            root.child("users").child(op).child(zero).setValue("0")
                            root.child("users").child(op).child("op").setValue(id.toString())
                            root.child("users").child(op).child(turn).setValue("1")
                        }
                    } else {
                        root.child("players").setValue("$id $name")
                    }
                }

            })
    }

    private fun clearAllNotActiveCross() {
        for (i in 0 until 9) {
            val posStr: Int = i / 3
            val posCol: Int = i % 3
            if (game[posStr][posCol] == 0) {
                allSquares[i].setImageResource(R.drawable.rectangle1)
            }
        }
    }

    private fun check(a: Int): Boolean {
        if (game[0][0] + game[0][1] + game[0][2] == 3 * a) {
            return true
        }
        if (game[1][0] + game[1][1] + game[1][2] == 3 * a) {
            return true
        }
        if (game[2][0] + game[2][1] + game[2][2] == 3 * a) {
            return true
        }
        if (game[0][0] + game[1][0] + game[2][0] == 3 * a) {
            return true
        }
        if (game[0][1] + game[1][1] + game[2][1] == 3 * a) {
            return true
        }
        if (game[0][2] + game[1][2] + game[2][2] == 3 * a) {
            return true
        }
        if (game[0][0] + game[1][1] + game[2][2] == 3 * a) {
            return true
        }
        if (game[0][2] + game[1][1] + game[2][0] == 3 * a) {
            return true
        }
        return false
    }

    private fun endGame(): Boolean {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (game[i][j] == 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun lose() {
        d("game", "lose")
        scoreOp.text = "1"
        root.child("users").child(id.toString()).child("op").setValue(null)
        view.background = resources.getDrawable(R.drawable.circle3)
        if (op != null) {
            root.child("users").child(op.toString()).child(cross)
                .setValue(getBinaryFormat(1).toString())
            root.child("users").child(op.toString()).child(zero)
                .setValue(getBinaryFormat(-1).toString())
            root.child("users").child(op.toString()).child(turn).setValue("1")
        }
        showAlert()
    }

    private fun win() {
        d("game", "win")
        scoreMe.text = "1"
        root.child("users").child(id.toString()).child("op").setValue(null)
        view.background = resources.getDrawable(R.drawable.circle2)
        if (op != null) {
            root.child("users").child(op.toString()).child(cross)
                .setValue(getBinaryFormat(1).toString())
            root.child("users").child(op.toString()).child(zero)
                .setValue(getBinaryFormat(-1).toString())
            root.child("users").child(op.toString()).child(turn).setValue("1")
        }
        showAlert()
        set("win")
    }

    private fun draw() {
        d("game", "draw")
        root.child("users").child(id.toString()).child("op").setValue(null)
        if (op != null) {
            root.child("users").child(op.toString()).child(cross)
                .setValue(getBinaryFormat(1).toString())
            root.child("users").child(op.toString()).child(zero)
                .setValue(getBinaryFormat(-1).toString())
            root.child("users").child(op.toString()).child(turn).setValue("1")
        }
        showAlert()
    }

    private fun showAlert() {
        val aboutDialog: AlertDialog = AlertDialog.Builder(
            this@CrossZeroGame
        ).setMessage("Сыграем еще?")
            .setNegativeButton("Нее") { _, _ ->
                clearAll()
                this@CrossZeroGame.finish()
            }
            .setPositiveButton("Даа") { _, _ ->
                clearAll()
                getOp()
            }.create()

        aboutDialog.show()
        aboutDialog.setCanceledOnTouchOutside(false)
        aboutDialog.setCancelable(false)

        (aboutDialog.findViewById(android.R.id.message) as TextView).movementMethod =
            LinkMovementMethod.getInstance()
    }

    private fun clearAll() {
        view.background = resources.getDrawable(R.drawable.cirle)
        scoreMe.text = "0"
        scoreOp.text = "0"
        game = Array(3) { Array(3) { 0 } }
        root.child("users").child(id.toString()).child("op").setValue(null)
        op = null
        waitText()
        for (c in allSquares) {
            c.setImageResource(R.drawable.rectangle1)
            c.isClickable = true
        }
    }

    private fun set(str: String) {
        root.child("users").child(id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val map = p0.value as Map<String, String>
                    val value: Int = Integer.parseInt(map[str].toString())
                    root.child("users").child(id!!).child(str).setValue((value + 1).toString())
                }

            })
    }

    private fun getBinaryFormat(a: Int): Int {
        var sum = 0
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (game[i][j] == a) {
                    sum += (1 shl (8 - (i * 3 + j)))
                }
            }
        }
        return sum
    }

    override fun onDestroy() {
        super.onDestroy()
        root.child("players").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val str = p0.value as String
                    val op = str.split(" ")[0]
                    if (op == id) {
                        root.child("players").removeValue()
                    }
                }
            }

        })
        root.child("users").child(id!!).child("op").setValue(null)
    }

}