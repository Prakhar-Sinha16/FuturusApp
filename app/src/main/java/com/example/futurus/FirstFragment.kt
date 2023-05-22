package com.example.futurus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class FirstFragment : AppCompatActivity() {
    lateinit var queryEdt: TextInputEditText
    lateinit var messageRV: RecyclerView
    lateinit var messageRVAdapter: MessageRVAdapter
    lateinit var messageList: ArrayList<MessageRVModal>
    var url = "https://api.openai.com/v1/completions"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_fragment)

        queryEdt = findViewById(R.id.idEdtQuery)
        messageRV = findViewById(R.id.idRVMessages)
        messageList = ArrayList()
        messageRVAdapter = MessageRVAdapter(messageList)
        val layoutManager = LinearLayoutManager(applicationContext)
        messageRV.layoutManager = layoutManager
        messageRV.adapter = messageRVAdapter

        queryEdt.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEND){
                if(queryEdt.text.toString().length > 0){
                    messageList.add(MessageRVModal(queryEdt.text.toString(), "user"))
                    messageRVAdapter.notifyDataSetChanged()
                    getResponse(queryEdt.text.toString())
                }else{
                    Toast.makeText(this, "Please enter your query..",Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getResponse(query: String) {
        queryEdt.setText("")
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject: JSONObject? = JSONObject()
        jsonObject?.put("model", "text-davinci-003")
        jsonObject?.put("prompt", query)
        jsonObject?.put("temperature", 0)
        jsonObject?.put("max_tokens", 100)
        jsonObject?.put("top_p", 1)
        jsonObject?.put("frequency_penalty", 0.0)
        jsonObject?.put("presence_penalty", 0.0)

        val postRequest: JsonObjectRequest =
            object: JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener { response ->
            val responseMsg: String =
                response.getJSONArray("choices").getJSONObject(0).getString("text")
            messageList.add(MessageRVModal(responseMsg, "bot"))
                messageRVAdapter.notifyDataSetChanged()
        }, Response.ErrorListener {
            Toast.makeText(applicationContext,"Failed to get Resposne..", Toast.LENGTH_SHORT).show()
        }){
                override fun getHeaders(): MutableMap<String, String> {
                    val params: MutableMap<String,String> = HashMap()
                    params["Content-Type"] = "application/json"
                    params["Authorization"] = "Bearer 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"
                    return params
                }
            }

        postRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {

            }
        })
        queue.add(postRequest)
    }
}