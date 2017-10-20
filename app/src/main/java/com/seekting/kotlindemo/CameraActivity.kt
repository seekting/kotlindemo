package com.seekting.kotlindemo

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import com.seekting.demo_lib.Demo
import java.io.File


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@Demo(title = "拍照", desc = "自定义分辨率拍照")
class CameraActivity : AppCompatActivity(), TextureView.SurfaceTextureListener, ImageReader.OnImageAvailableListener {
    companion object StaticProp {
        const private val DEBUG = true
        const private val TAG = "CameraActivity.kt"
        private val ORIENTATION = SparseIntArray()

        init {
            ORIENTATION.append(Surface.ROTATION_0, 90)
            ORIENTATION.append(Surface.ROTATION_90, 0)
            ORIENTATION.append(Surface.ROTATION_180, 270)
            ORIENTATION.append(Surface.ROTATION_270, 180)
        }
    }

    override fun onImageAvailable(reader: ImageReader?) {


        if (DEBUG) {
            Log.d(TAG, "CameraActivity.onImageAvailable()")
        }

        val img = reader!!.acquireLatestImage()
        val buffer = img.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)
        img.close()
        val dir = Environment.getExternalStorageDirectory()
        val file = File(dir, "pai.jpg")
        file.writeBytes(data)
        mUiHandler.post(
                {
                    image.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
                    image.visibility = View.VISIBLE
                }
        )

    }

    val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult?) {
            super.onCaptureCompleted(session, request, result)

            restartPreView()
        }

    }
    val deviceStateCallBack = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {
            startPreView(camera)
        }


        override fun onDisconnected(camera: CameraDevice?) {
        }

        override fun onError(camera: CameraDevice?, error: Int) {
        }

    }
    val mSessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession?) {
        }

        override fun onConfigured(session: CameraCaptureSession?) {
            mCaptureRequest = mPreviewBuilder.build()
            mSession = session!!
            session!!.setRepeatingRequest(mCaptureRequest, captureCallback, mHandler)

        }

    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return false
    }


    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {

        val cameraManager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val characteristics = cameraManager.getCameraCharacteristics("0")
        val map: StreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        mPreviewSize = map.getOutputSizes(SurfaceTexture::class.java)[0]
        cameraManager.openCamera("0", deviceStateCallBack, mHandler)


    }

    lateinit var mSession: CameraCaptureSession
    lateinit var mCaptureRequest: CaptureRequest
    lateinit var mPreviewView: TextureView
    lateinit var mHandler: Handler
    lateinit var mThreadHandler: HandlerThread
    lateinit var mPreviewSize: Size
    lateinit var mPreviewBuilder: CaptureRequest.Builder
    lateinit var mImageReader: ImageReader
    lateinit var mTakePic: View
    lateinit var mCameraDevice: CameraDevice
    lateinit var mUiHandler: Handler
    lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.image) as ImageView
        image.visibility = View.GONE
        image.setOnClickListener({ image.visibility = View.GONE })
        mTakePic = findViewById(R.id.take_picture)
        mTakePic.setOnClickListener {

            if (mSession != null) {
                val requestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                requestBuilder.addTarget(mImageReader.surface)
                mSession.stopRepeating()
                val rotation = windowManager.defaultDisplay.rotation
                requestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation))
                mSession.capture(requestBuilder.build(), captureCallback, mHandler)
            }

        }
        mUiHandler = Handler()
        mThreadHandler = HandlerThread("camera2")
        mThreadHandler.start()
        mHandler = Handler(mThreadHandler.looper)
        mPreviewView = findViewById(R.id.textureview) as TextureView
        mPreviewView.setSurfaceTextureListener(this)


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (DEBUG) {
            Log.d(TAG, "CameraActivity.onWindowFocusChanged()hasFocus="+hasFocus)
        }

    }

    private fun setUpCamera() {

    }

    private fun restartPreView() {
        mUiHandler.postDelayed({
            mSession.setRepeatingRequest(mCaptureRequest, null, mHandler)
        }, 500)
    }

    private fun startPreView(camera: CameraDevice?) {
        val texture = mPreviewView.surfaceTexture
        texture.setDefaultBufferSize(mPreviewView.width, mPreviewView.height)
        val surface = Surface(texture)

        mPreviewBuilder = camera!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        mCameraDevice = camera
        mImageReader = ImageReader.newInstance(mPreviewView.getWidth(), mPreviewView.getHeight(), ImageFormat.JPEG, 2)
        mImageReader.setOnImageAvailableListener(this, mHandler)
        mPreviewBuilder.addTarget(surface)
//        mPreviewBuilder.addTarget(mImageReader.surface)
//        val list = mutableListOf(surface)
        val list = mutableListOf(surface, mImageReader.surface)
        mCameraDevice.createCaptureSession(list, mSessionStateCallback, mHandler)

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
