package com.example.rickandmortywiki

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.jvm.internal.Intrinsics.Kotlin


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val rickAndMortyService: RickAndMortyService = retrofit.create(RickAndMortyService::class.java)

        rickAndMortyService.getCharacterById(10).enqueue(object : Callback<GetCharacterByIdResponse> {
            override fun onResponse(call: Call<GetCharacterByIdResponse>, response: Response<GetCharacterByIdResponse>) {
                if (!response.isSuccessful) {
                    // Aquí puedes procesar la respuesta exitosa
                    Toast.makeText(this@MainActivity, "Unsuccessful network call!", Toast.LENGTH_LONG).show()
                    return
                } else {
                    val body = response.body()!!
                    val name = body.name

                    textView.text = name

                }
            }

            override fun onFailure(call: Call<GetCharacterByIdResponse>, t: Throwable) {
                // Aquí puedes manejar la excepción que se produce durante la llamada de red
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }
}

