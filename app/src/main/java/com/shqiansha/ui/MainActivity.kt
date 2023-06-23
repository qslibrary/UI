package com.shqiansha.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.shqiansha.ui.databinding.ActivityMainBinding
import com.shqiansha.ui.dialog.ConfirmationDialog
import com.shqiansha.ui.dialog.OptionDialog
import com.shqiansha.ui.dialog.PickerDialog
import com.shqiansha.ui.dialog.PictureSelectionDialog
import com.shqiansha.ui.popup.MultiOptionPopup
import com.shqiansha.ui.popup.OptionPopup
import com.shqiansha.ui.popup.TwoLayerOptionPopup

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIConfig.colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding
        binding.run {
            tvOption.setOnClickListener {
                val option = OptionDialog()
                option.setOnOptionSelectListener {

                }
                option.setValues(arrayOf("11")).show(supportFragmentManager)
            }
            tvConfirm.setOnClickListener {
                val confirm = ConfirmationDialog()
                confirm.title = "titletitletitletitletitletitletitletitle"
//            confirm.hideCancel = true
                confirm.show(supportFragmentManager)
            }
            tvPicture.setOnClickListener {
                val picture = PictureSelectionDialog("com.shqiansha.ui.fileprovider")
                picture.setOnPictureSelectedListener {

                }
                picture.show(supportFragmentManager)
            }
            tvPicker.setOnClickListener {
                val picker = PickerDialog(
                    arrayOf(
                        "1111",
                        "22222",
                        "33333",
                        "44444",
                        "55555",
                        "55556",
                        "555557",
                        "555558",
                        "555559"
                    ), 1
                )
                picker.title = "请选择"
                picker.show(supportFragmentManager, "picker")
            }
            poOption.setOnClickListener {
                val popup = OptionPopup(poOption, arrayOf("1", "2", "3", "4"), "3")

                popup.setOnOptionSelectListener {

                }
                popup.show()
            }
            ppMultiOption.setOnClickListener {
                val popup =
                    MultiOptionPopup(ppMultiOption, arrayOf("1", "2", "3", "4"), arrayOf("1", "2"))
                popup.show()
            }
            ppTwoLayerOption.setOnClickListener {
                val map = mutableMapOf<String, Array<String>>()
                map["全球"] = arrayOf()
                map["全球1"] = arrayOf()
                map["全球2"] = arrayOf()
                map["全球3"] = arrayOf()
                map["全球4"] = arrayOf()
                map["全球5"] = arrayOf()
                map["全球6"] = arrayOf()
                map["全球7"] = arrayOf()
                map["全球8"] = arrayOf()
                map["全球9"] = arrayOf()
                map["全球10"] = arrayOf()
                map["全球11"] = arrayOf()
                map["全球12"] = arrayOf()
                map["中国"] = arrayOf(
                    "中国(所有地区)",
                    "上海",
                    "北京",
                    "四川",
                    "广州",
                    "重庆",
                    "武汉",
                    "天津"
                )
                map["英国"] = arrayOf("英格兰")
                val popup = TwoLayerOptionPopup(ppTwoLayerOption, map,"四川")
                popup.show()
            }
            uivTest.setOnClickListener {
                uivTest.tvRight.text = "测试"
            }
        }
    }
}