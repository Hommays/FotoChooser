package com.maxim.fotochooser

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatViewInflater
import java.io.File
import java.net.URI

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.chose_picture)
        imageView = findViewById(R.id.chosen_picture)
        textView = findViewById(R.id.name_of_picture)

        button.setOnClickListener{
            pickImageFromGallery()
        }
    }
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
            textView.setText(getFilename().toString())
            imageView.setImageURI(data?.data)

    }
    fun getFilename(contentResolver: ContentResolver, uri: Uri): String? {
        return when(uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.getString(nameIndex);
                }
            }
            ContentResolver.SCHEME_FILE-> {
                uri.path?.let { path ->
                    File(path).name
                }
            }
            else -> null
        }
    }

}

