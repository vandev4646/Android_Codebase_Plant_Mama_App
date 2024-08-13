/*
package com.example.plantmamaapp_v2;


import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.plantmamaapp_v2.databinding.ActivityTakePhotoBinding;
//import com.android.example.plantmamaapp_v2.databinding.ActivityMainBinding;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.core.Preview;
import androidx.camera.core.CameraSelector;
import android.util.Log;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.video.FallbackStrategy;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.content.PermissionChecker;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TakePhoto extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    private ActivityTakePhotoBinding binding;
    private ExecutorService cameraExecutor;
    private ImageCapture imageCapture;
    private Recorder recorder;
    private Recording recording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTakePhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        binding.imageCaptureButton.setOnClickListener(v -> takePhoto());
       /* binding.videoCaptureButton.setOnClickListener(v -> {
            if (recording != null && recording.isRecording()) {
                stopRecording();
            } else {
                startRecording();
            }
        });

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startCamera() {
        ProcessCameraProvider.getInstance(this).addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(this).get();
                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
                imageCapture = new ImageCapture.Builder().build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, imageCapture);
                preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());
            } catch (Exception e) {
                Log.e("CameraXApp", "Failed to start camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String fileName = "IMG_" + sdf.format(System.currentTimeMillis()) + ".jpg";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)).build();

        imageCapture.takePicture(outputFileOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> Toast.makeText(TakePhoto.this, "Photo saved", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(ImageCaptureException exception) {
                Log.e("CameraXApp", "Photo capture failed", exception);
            }
        });
    }
/*
    private void startRecording() {
        if (recorder != null && recorder.isRecording()) {
            return;
        }

        if (recorder == null) {
            recorder = new Recorder();
        }

        MediaStoreOutputOptions outputOptions = new MediaStoreOutputOptions.Builder(getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI).build();
        QualitySelector qualitySelector = new QualitySelector.Builder().fallbackStrategy(FallbackStrategy.RECORD_CONTINUOUSLY).build();

        recorder.startRecording(outputOptions, qualitySelector, cameraExecutor, new VideoRecordEvent.Callback() {
            @Override
            public void onRecordingStarted(Recording recording) {
                TakePhoto.this.recording = recording;
                runOnUiThread(() -> Toast.makeText(TakePhoto.this, "Recording started", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onRecordingStopped(Recording recording) {
                TakePhoto.this.recording = null;
                runOnUiThread(() -> Toast.makeText(TakePhoto.this, "Recording stopped", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(VideoRecordEvent.Error error) {
                Log.e("CameraXApp", "Recording failed", error.getException());
            }
        });
    }

    private void stopRecording() {
        if (recording == null || !recording.isRecording()) {
            return;
        }

        recording.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    class CameraXApp {
        private static final String TAG = "CameraXApp";
        private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
        private static final String[] REQUIRED_PERMISSIONS;

        static {
            REQUIRED_PERMISSIONS = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            };
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                String[] additionalPermissions = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                String[] combinedPermissions = new String[REQUIRED_PERMISSIONS.length + additionalPermissions.length];
                System.arraycopy(REQUIRED_PERMISSIONS, 0, combinedPermissions, 0, REQUIRED_PERMISSIONS.length);
                System.arraycopy(additionalPermissions, 0, combinedPermissions, REQUIRED_PERMISSIONS.length, additionalPermissions.length);
               // REQUIRED_PERMISSIONS = combinedPermissions;
            }
        }
    }

}
*/