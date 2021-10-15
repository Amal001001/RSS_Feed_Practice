package com.example.rss_feedpractice

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var RVAdapter: RecyclerView
    private var questionsArray = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RVAdapter = findViewById(R.id.rvQuestions)

        RVAdapter.layoutManager = LinearLayoutManager(this)
        BringRecentQuestions().execute()
    }

    private inner class BringRecentQuestions : AsyncTask<Void, Void, MutableList<Question>>() {
        val parser = XMLParser()

        override fun doInBackground(vararg p0: Void?): MutableList<Question> {
            val url = URL("https://stackoverflow.com/feeds")
            val urlConnection = url.openConnection() as HttpURLConnection
            questionsArray =
                urlConnection.getInputStream()?.let {
                    parser.parse(it)
                }
                        as MutableList<Question>
            return questionsArray
        }

        override fun onPostExecute(result: MutableList<Question>?) {
            super.onPostExecute(result)
            RVAdapter.adapter = result?.let { RVAdapter(it) }
            RVAdapter.adapter?.notifyDataSetChanged()
        }
    }

}