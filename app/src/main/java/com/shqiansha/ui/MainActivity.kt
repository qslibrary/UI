package com.shqiansha.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shqiansha.ui.dialog.ConfirmationDialog
import com.shqiansha.ui.dialog.OptionDialog
import com.shqiansha.ui.dialog.PictureSelectionDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvOption.setOnClickListener {
            val option=OptionDialog()
            option.setOnOptionSelectListener{

            }
            option.setValues(arrayOf("11")).setFragmentManager(supportFragmentManager).show()
        }
        tvConfirm.setOnClickListener {
            val confirm=ConfirmationDialog()
            confirm.title="titletitletitletitletitletitletitletitle"
            confirm.hideCancel=true
            confirm.show(supportFragmentManager)
        }
        tvPicture.setOnClickListener {
            val picture=PictureSelectionDialog("com.shqiansha.ui.fileprovider")
            picture.setOnPictureSelectedListener{

            }
            picture.show(supportFragmentManager)
        }
    }
}