package com.timearchitect.test5

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.commit


class BlankFragment2 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v: View = inflater.inflate(R.layout.fragment_blank2, container, false)
        var fl = v.findViewById<FrameLayout>(R.id.frame2) //funkar efter inflation
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tv2=view.findViewById<TextView>(R.id.blankText2)

        tv2.setTextColor(Color.BLUE) //logik rekommenderat

    }


}