package com.shqiansha.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

/**
 * 图片选择弹框
 * please ensure you have import UCrop,
 * if not, add "implementation 'com.github.yalantis:ucrop:2.2.4'" in app gradle and config UCropActivity in manifest
 *
 * authority: file provider authority,用于图片保存
 */
class PictureSelectionDialog(authority: String) : OptionDialog() {
    private val helper by lazy { PictureManager(this, authority) }
    private var onPictureSelectedListener: OnPictureSelectedListener? = null

    init {
        autoDismiss = false
        setTitle("修改头像")
        setValues(arrayOf("拍照", "从相册选择"))
        setOnOptionSelectListener(object : OnOptionSelectListener {
            override fun onSelect(value: String) {
                when (value) {
                    "拍照" -> helper.navToCamera()
                    "从相册选择" -> helper.navToGallery()
                }
            }
        })
    }

    fun setOnPictureSelectedListener(listener: OnPictureSelectedListener) {
        onPictureSelectedListener = listener
    }

    fun setOnPictureSelectedListener(listener: (path: String) -> Unit) {
        onPictureSelectedListener = object : OnPictureSelectedListener {
            override fun onSuccess(path: String) {
                listener.invoke(path)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        helper.onActivityResult(requestCode, resultCode, data) {
            onPictureSelectedListener?.onSuccess(helper.getPicturePath())
            dismiss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        helper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    interface OnPictureSelectedListener {
        fun onSuccess(path: String)
    }

    class PictureManager {
        private var uri: Uri? = null
        private var crop = true
        private val authority: String
        private val context: Context
        private val activity: AppCompatActivity?
        private val fragment: Fragment?

        companion object {
            private const val RC_CAMERA = 1
            private const val RC_GALLERY = 2
            private const val PRC_CAMERA = 4
            private const val PRC_GALLERY = 5
        }

        constructor(activity: AppCompatActivity, authority: String) {
            this.context = activity
            this.authority = authority
            this.activity = activity
            this.fragment = null
        }

        constructor(fragment: Fragment, authority: String) {
            this.context = fragment.requireContext()
            this.authority = authority
            this.activity = null
            this.fragment = fragment
        }

        private fun generateUri(file: File): Uri {
            return if (Build.VERSION.SDK_INT < 24) Uri.fromFile(file)
            else FileProvider.getUriForFile(context, authority, file)
        }

        fun navToCamera() {
            if (isPermissionGranted(Manifest.permission.CAMERA)) {
                val intent = Intent()
                val pictureFile = File(context.externalCacheDir, "temp.jpg")
                uri = generateUri(pictureFile)
                intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                intent.addCategory("android.intent.category.DEFAULT")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivity(intent, RC_CAMERA)
            } else {
                requestPermission(Manifest.permission.CAMERA, PRC_CAMERA)
            }
        }

        fun navToGallery() {
            if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivity(intent, RC_GALLERY)
            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PRC_GALLERY)
            }
        }

        private fun navToCrop(sourceUri: Uri?) {
            if (sourceUri == null) return
            //保存文件
            val file = File(context.externalCacheDir, "${System.currentTimeMillis()}_crop.jpg")
            val saveUri = Uri.fromFile(file)
            uri = saveUri
            //调用裁剪
//            val options = UCrop.Options()
//            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
//            options.setCompressionQuality(50)
//            val crop = UCrop.of(sourceUri, saveUri)
//                .withAspectRatio(1f, 1f)
//                .withMaxResultSize(400, 400)
//                .withOptions(options)
//            if (activity != null) crop.start(activity)
//            if (fragment != null) crop.start(context, fragment)
        }

        fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
            listener: () -> Unit
        ) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    RC_CAMERA -> if (crop) navToCrop(uri) else listener.invoke()
                    RC_GALLERY -> {
                        uri = data?.data
                        if (crop) navToCrop(uri) else listener.invoke()
                    }
//                    UCrop.REQUEST_CROP -> listener.invoke()
                }
            }
        }

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String?>,
            grantResults: IntArray
        ) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) return
            when (requestCode) {
                PRC_CAMERA -> navToCamera()
                PRC_GALLERY -> navToGallery()
            }
        }

        private fun startActivity(intent: Intent, requestCode: Int) {
            fragment?.startActivityForResult(intent, requestCode)
            activity?.startActivityForResult(intent, requestCode)
        }

        private fun isPermissionGranted(permission: String): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun requestPermission(permission: String, requestCode: Int) {
            fragment?.requestPermissions(arrayOf(permission), requestCode)
            activity?.requestPermissions(arrayOf(permission), requestCode)
        }

        fun getPicturePath(): String {
            return getPathFromUri(context, uri) ?: ""
        }

        private fun getPathFromUri(context: Context, uri: Uri?): String? {
            var path: String? = ""
            if (null == uri) return null
            val scheme = uri.scheme
            if (scheme == null) path =
                uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
                path = uri.path
            } else if (ContentResolver.SCHEME_CONTENT == scheme) {
                val cursor = context.contentResolver.query(
                    uri,
                    arrayOf(MediaStore.Images.ImageColumns.DATA),
                    null,
                    null,
                    null
                )
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        val index =
                            cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                        if (index > -1) {
                            path = cursor.getString(index)
                        }
                    }
                    cursor.close()
                }
            }
            return path
        }
    }

}