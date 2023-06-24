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
                val popup = OptionPopup(poOption, arrayListOf("1", "2", "3", "4"), "3")

                popup.setOnOptionSelectListener {

                }
                popup.show()
            }
            ppMultiOption.setOnClickListener {
                val popup =
                    MultiOptionPopup(
                        ppMultiOption,
                        arrayListOf("1", "2", "3", "4"),
                        arrayListOf("1", "2")
                    )
                popup.show()
            }
            ppTwoLayerOption.setOnClickListener {
                val map = mutableMapOf<String, List<String>>()
                map["全球"] = emptyList()
                map["中国"] = arrayListOf(
                    "中国(所有地区)",
                    "上海",
                    "北京",
                    "四川",
                    "广州",
                    "重庆",
                    "武汉",
                    "天津"
                )
                map["英国"] = arrayListOf("英格兰")
                val text = ppTwoLayerOption.text.toString()
                val popup = TwoLayerOptionPopup(ppTwoLayerOption, map, text)
                popup.setOnOptionSelectListener {
                    ppTwoLayerOption.text = it
                }
                popup.show()
            }
            uivTest.setOnClickListener {
                uivTest.tvRight.text = "测试"
            }
        }
    }
}