package com.plugspace.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.plugspace.R
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory
import java.text.ParseException
import java.text.SimpleDateFormat

class Kaler_stpry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kaler_stpry)

        showStories()
    }

    fun showStories() {
        val myStories = ArrayList<MyStory>()
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        try {
            val story1 = MyStory(
                "https://media.pri.org/s3fs-public/styles/story_main/public/images/2019/09/092419-germany-climate.jpg?itok=P3FbPOp-",
                simpleDateFormat.parse("20-10-2019 10:00:00")
            )
//            myStories.add(story1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            val story2 = MyStory(
                "http://i.imgur.com/0BfsmUd.jpg",
                simpleDateFormat.parse("26-10-2019 15:00:00"),
                "#TEAM_STANNIS"
            )
//            myStories.add(story2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val story3 = MyStory(
            "https://mfiles.alphacoders.com/681/681242.jpg"
//            "https:\\/\\/www.plugspace.io\\/public\\/story\\/165380772462931a6cee2a6.mp4"
        )
        myStories.add(story3)
        StoryView.Builder(supportFragmentManager)
            .setStoriesList(myStories)
            .setStoryDuration(5000)
            .setTitleText("Hamza Al-Omari")
            .setSubtitleText("Damascus")
            .setStoryClickListeners(object : StoryClickListeners {
                override fun onDescriptionClickListener(position: Int) {
                    Toast.makeText(
                        applicationContext,
                        "Clicked: " + myStories[position].description,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onTitleIconClickListener(position: Int) {}
            })
            .setOnStoryChangedCallback { position ->
                Toast.makeText(
                    applicationContext,
                    position.toString() + "",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setStartingIndex(0)
            .build()
            .show()
    }

}