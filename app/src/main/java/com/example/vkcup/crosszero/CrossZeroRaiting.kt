package com.example.vkcup.crosszero

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.vkcup.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CrossZeroRaiting : AppCompatActivity() {

    private var ratings: ArrayList<Rating> = arrayListOf()
    private val root = FirebaseDatabase.getInstance().reference.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_zero_raiting)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        title = "Ratings"
        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        root.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val map = p0.value as Map<String, Map<String, String>>
                    for (i in map.keys) {
                        ratings.add(Rating(map[i]?.get("name"), map[i]?.get("win")))
                    }
                    ratings.sortByDescending { list -> Integer.parseInt(list.wins.split(" ")[3])}
                    val ratingAdapter =
                        RatingAdapter(ratings)
                    recyclerView.adapter = ratingAdapter
                }
            }

        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(this, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                }
            })
        )
    }

    class RecyclerItemClickListener internal constructor(
        context: Context?,
        private val mListener: OnItemClickListener?
    ) :
        OnItemTouchListener {
        interface OnItemClickListener {
            fun onItemClick(view: View?, position: Int)
        }

        private var mGestureDetector: GestureDetector =
            GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })

        override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
            val childView: View? = view.findChildViewUnder(e.x, e.y)
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
                return true
            }
            return false
        }

        override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    }
}