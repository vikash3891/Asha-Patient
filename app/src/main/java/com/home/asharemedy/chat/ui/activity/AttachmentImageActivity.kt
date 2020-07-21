package com.home.asharemedy.chat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.home.asharemedy.R
import com.home.asharemedy.chat.utils.PREFERRED_IMAGE_SIZE_FULL
import com.home.asharemedy.chat.utils.shortToast


private const val EXTRA_URL = "url"

class AttachmentImageActivity : BaseActivity() {

    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private var imageLoaded = false

    companion object {
        fun start(context: Context, url: String) {
            val intent = Intent(context, AttachmentImageActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)
        initUI()
        loadImage()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.toolbar_video_player_background))
        }
        supportActionBar?.elevation = 0f
        imageView = findViewById(R.id.iv_full_view)
        progressBar = findViewById(R.id.progress_show_image)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_video_player, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_player_save -> {
                saveFileToGallery()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun saveFileToGallery() {
        if (imageLoaded) {
            val bitmapToSave = imageView.drawable.toBitmap()
            MediaStore.Images.Media.insertImage(contentResolver, bitmapToSave, "attachment", "")
            shortToast("Image saved to the Gallery")
        } else {
            shortToast("Image not yet downloaded")
        }
    }

    private fun loadImage() {
        val url = intent.getStringExtra(EXTRA_URL)
        progressBar.visibility = View.VISIBLE
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(DrawableListener(progressBar))
                .error(R.drawable.ic_notification)
                .dontTransform()
                .override(PREFERRED_IMAGE_SIZE_FULL, PREFERRED_IMAGE_SIZE_FULL)
                .into(imageView)
    }

    private inner class DrawableListener(private val progressBar: ProgressBar) : RequestListener<String, GlideDrawable> {

        override fun onException(e: Exception?, model: String, target: Target<GlideDrawable>,
                                 isFirstResource: Boolean): Boolean {
            e?.printStackTrace()
            showErrorSnackbar(R.string.error_load_image, null, null)
            progressBar.visibility = View.GONE
            return false
        }

        override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>,
                                     isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
            progressBar.visibility = View.GONE
            imageLoaded = true
            return false
        }
    }
}