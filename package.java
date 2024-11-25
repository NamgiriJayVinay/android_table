// AndroidManifest.xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.facerecognition">

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-feature android:name="android.hardware.camera" android:required="true" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity 
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>

// build.gradle (app level)
plugins {
    id 'com.android.application'
}

android {
    namespace 'com.example.facerecognition'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.facerecognition"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // CameraX
    implementation 'androidx.camera:camera-camera2:1.3.0'
    implementation 'androidx.camera:camera-lifecycle:1.3.0'
    implementation 'androidx.camera:camera-view:1.3.0'
    
    // ML Kit
    implementation 'com.google.mlkit:face-detection:16.1.5'
    
    // TensorFlow Lite
    implementation 'org.tensorflow:tensorflow-lite:2.13.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
}

// res/layout/activity_main.xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.camera.view.PreviewView
        android:id="@+id/previewView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toTopOf="@+id/buttonContainer"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/overlayImage"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toTopOf="@+id/buttonContainer"
        app:layout_constraintTop_toTopOf="parent" />

    <LinearLayout
        android:id="@+id/buttonContainer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp"
        app:layout_constraintBottom_toBottomOf="parent">

        <Button
            android:id="@+id/startRecognitionBtn"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginEnd="8dp"
            android:text="Start Recognition" />

        <Button
            android:id="@+id/storeEmbeddingsBtn"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginStart="8dp"
            android:text="Store Face Embeddings" />

    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>

// MainActivity.java
package com.example.facerecognition;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private PreviewView previewView;
    private ImageView overlayImage;
    private Button startRecognitionBtn;
    private Button storeEmbeddingsBtn;
    private FaceDetector faceDetector;
    private FaceRecognizer faceRecognizer;
    private ProcessCameraProvider cameraProvider;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String userName = "YourName"; // Replace with actual name
    private List<float[]> storedEmbeddings;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupFaceDetector();
        setupButtons();
        checkAndRequestPermissions();
    }

    private void initializeViews() {
        previewView = findViewById(R.id.previewView);
        overlayImage = findViewById(R.id.overlayImage);
        startRecognitionBtn = findViewById(R.id.startRecognitionBtn);
        storeEmbeddingsBtn = findViewById(R.id.storeEmbeddingsBtn);
    }

    private void setupFaceDetector() {
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();
        faceDetector = FaceDetection.getClient(options);
        faceRecognizer = new FaceRecognizer(this);
        storedEmbeddings = new ArrayList<>();
    }

    private void setupButtons() {
        startRecognitionBtn.setOnClickListener(v -> {
            if (cameraProvider != null) {
                cameraProvider.unbindAll();
                startRecognitionBtn.setText("Start Recognition");
            } else {
                startCamera();
                startRecognitionBtn.setText("Stop Recognition");
            }
        });

        storeEmbeddingsBtn.setOnClickListener(v -> storeFaceEmbeddings());
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), this::processImage);
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        
        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
        } catch (Exception e) {
            Toast.makeText(this, "Use case binding failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void processImage(ImageProxy imageProxy) {
        if (isProcessing) {
            imageProxy.close();
            return;
        }

        isProcessing = true;
        Image mediaImage = imageProxy.getImage();
        
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            
            faceDetector.process(image)
                    .addOnSuccessListener(faces -> {
                        if (!faces.isEmpty()) {
                            processFaces(faces, mediaImage.getWidth(), mediaImage.getHeight());
                        }
                    })
                    .addOnCompleteListener(task -> {
                        isProcessing = false;
                        imageProxy.close();
                    });
        } else {
            isProcessing = false;
            imageProxy.close();
        }
    }

    private void processFaces(List<Face> faces, int width, int height) {
        Bitmap overlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setTextSize(50f);

        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            canvas.drawRect(bounds, paint);
            
            if (!storedEmbeddings.isEmpty()) {
                float[] currentEmbedding = faceRecognizer.getFaceEmbeddings(
                        InputImage.fromBitmap(overlay, 0), face);
                float similarity = faceRecognizer.compareFaces(
                        currentEmbedding, storedEmbeddings.get(0));
                
                if (similarity > 0.8f) {
                    canvas.drawText(userName, bounds.left, bounds.top - 10, paint);
                }
            }
        }

        runOnUiThread(() -> {
            overlayImage.setImageBitmap(overlay);
        });
    }

    private void storeFaceEmbeddings() {
        File documentsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File faceFolder = new File(documentsPath, userName);

        if (!faceFolder.exists() || !faceFolder.isDirectory()) {
            Toast.makeText(this, "Face folder not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        File[] faceImages = faceFolder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".jpg") || 
                name.toLowerCase().endsWith(".jpeg") || 
                name.toLowerCase().endsWith(".png"));

        if (faceImages == null || faceImages.length == 0) {
            Toast.makeText(this, "No face images found!", Toast.LENGTH_SHORT).show();
            return;
        }

        storedEmbeddings.clear();
        int processedImages = 0;

        for (File image : faceImages) {
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            if (bitmap != null) {
                InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
                faceDetector.process(inputImage)
                        .addOnSuccessListener(faces -> {
                            if (!faces.isEmpty()) {
                                float[] embeddings = faceRecognizer.getFaceEmbeddings(inputImage, faces.get(0));
                                storedEmbeddings.add(embeddings);
                            }
                        });
                processedImages++;
            }
        }

        if (processedImages > 0) {
            Toast.makeText(this, "Face embeddings stored successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to process face images", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }
}

// FaceRecognizer.java
package com.example.facerecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.Interpreter</antArtifact>

// FaceRecognizer.java (continued)
package com.example.facerecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class FaceRecognizer {
    private Interpreter tflite;
    private static final int EMBEDDING_SIZE = 128;
    private static final int INPUT_SIZE = 112; // Face image size required by the model
    private final ImageProcessor imageProcessor;

    public FaceRecognizer(Context context) {
        try {
            MappedByteBuffer modelBuffer = FileUtil.loadMappedFile(context, "mobile_face_net.tflite");
            Interpreter.Options options = new Interpreter.Options();
            tflite = new Interpreter(modelBuffer, options);

            // Setup image processor for preprocessing
            imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(INPUT_SIZE, INPUT_SIZE, ResizeOp.ResizeMethod.BILINEAR))
                .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float[] getFaceEmbeddings(InputImage image, Face face) {
        // Extract face region
        Bitmap faceBitmap = cropFace(image, face);
        if (faceBitmap == null) return null;

        // Preprocess face image
        ByteBuffer inputBuffer = preprocessImage(faceBitmap);
        
        // Get embeddings using TFLite model
        float[][] outputArray = new float[1][EMBEDDING_SIZE];
        tflite.run(inputBuffer, outputArray);
        
        // Normalize embeddings
        float[] embeddings = outputArray[0];
        float sum = 0;
        for (float val : embeddings) {
            sum += val * val;
        }
        float norm = (float) Math.sqrt(sum);
        for (int i = 0; i < embeddings.length; i++) {
            embeddings[i] = embeddings[i] / norm;
        }
        
        return embeddings;
    }

    private Bitmap cropFace(InputImage image, Face face) {
        try {
            // Get the original bitmap from InputImage
            Bitmap originalBitmap;
            if (image.getBitmapInternal() != null) {
                originalBitmap = image.getBitmapInternal();
            } else {
                return null;
            }

            // Get face bounding box
            Rect bounds = face.getBoundingBox();
            
            // Ensure bounds are within bitmap dimensions
            bounds.left = Math.max(0, bounds.left);
            bounds.top = Math.max(0, bounds.top);
            bounds.right = Math.min(originalBitmap.getWidth(), bounds.right);
            bounds.bottom = Math.min(originalBitmap.getHeight(), bounds.bottom);
            
            // Add padding around face (20%)
            int padding = (int) (Math.min(bounds.width(), bounds.height()) * 0.2);
            bounds.left = Math.max(0, bounds.left - padding);
            bounds.top = Math.max(0, bounds.top - padding);
            bounds.right = Math.min(originalBitmap.getWidth(), bounds.right + padding);
            bounds.bottom = Math.min(originalBitmap.getHeight(), bounds.bottom + padding);

            // Crop face region
            Bitmap faceBitmap = Bitmap.createBitmap(
                originalBitmap,
                bounds.left,
                bounds.top,
                bounds.width(),
                bounds.height()
            );

            // Resize to model input size
            return Bitmap.createScaledBitmap(faceBitmap, INPUT_SIZE, INPUT_SIZE, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ByteBuffer preprocessImage(Bitmap bitmap) {
        try {
            // Convert bitmap to TensorImage
            TensorImage tensorImage = TensorImage.fromBitmap(bitmap);
            
            // Process the image
            tensorImage = imageProcessor.process(tensorImage);
            
            // Get the ByteBuffer
            ByteBuffer inputBuffer = tensorImage.getBuffer();
            
            // Normalize pixel values to [-1, 1]
            int numPixels = INPUT_SIZE * INPUT_SIZE;
            for (int i = 0; i < numPixels * 3; i += 3) {
                float r = ((inputBuffer.getFloat(i * 4)) / 255.0f) * 2 - 1;
                float g = ((inputBuffer.getFloat((i + 1) * 4)) / 255.0f) * 2 - 1;
                float b = ((inputBuffer.getFloat((i + 2) * 4)) / 255.0f) * 2 - 1;
                
                inputBuffer.putFloat(i * 4, r);
                inputBuffer.putFloat((i + 1) * 4, g);
                inputBuffer.putFloat((i + 2) * 4, b);
            }
            
            return inputBuffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float compareFaces(float[] embeddings1, float[] embeddings2) {
        if (embeddings1 == null || embeddings2 == null || 
            embeddings1.length != EMBEDDING_SIZE || embeddings2.length != EMBEDDING_SIZE) {
            return 0.0f;
        }

        float dotProduct = 0;
        for (int i = 0; i < EMBEDDING_SIZE; i++) {
            dotProduct += embeddings1[i] * embeddings2[i];
        }
        
        // Cosine similarity (normalized embeddings, so just dot product)
        return dotProduct;
    }

    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
    }
}

// res/values/strings.xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Face Recognition</string>
</resources>

// res/values/themes.xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="AppTheme" parent="Theme.MaterialComponents.Light.DarkActionBar">
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryDark">@color/purple_700</item>
        <item name="colorAccent">@color/teal_200</item>
    </style>
</resources>

// res/values/colors.xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>



// FaceEmbeddingsStorage.java
package com.example.facerecognition;

import android.content.Context;
import android.util.Log;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FaceEmbeddingsStorage {
    private static final String EMBEDDINGS_FILE = "face_embeddings.dat";
    private static final String TAG = "FaceEmbeddingsStorage";
    private final Context context;

    public FaceEmbeddingsStorage(Context context) {
        this.context = context;
    }

    public void saveEmbeddings(List<float[]> embeddings, String userName) {
        try {
            FileOutputStream fos = context.openFileOutput(EMBEDDINGS_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            // Save username
            oos.writeUTF(userName);
            
            // Save number of embeddings
            oos.writeInt(embeddings.size());
            
            // Save each embedding array
            for (float[] embedding : embeddings) {
                oos.writeInt(embedding.length);
                for (float value : embedding) {
                    oos.writeFloat(value);
                }
            }
            
            oos.close();
            fos.close();
            Log.d(TAG, "Embeddings saved successfully for user: " + userName);
        } catch (IOException e) {
            Log.e(TAG, "Error saving embeddings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public EmbeddingsData loadEmbeddings() {
        List<float[]> embeddings = new ArrayList<>();
        String userName = "";
        
        try {
            FileInputStream fis = context.openFileInput(EMBEDDINGS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            // Read username
            userName = ois.readUTF();
            
            // Read number of embeddings
            int numEmbeddings = ois.readInt();
            
            // Read each embedding array
            for (int i = 0; i < numEmbeddings; i++) {
                int embeddingLength = ois.readInt();
                float[] embedding = new float[embeddingLength];
                for (int j = 0; j < embeddingLength; j++) {
                    embedding[j] = ois.readFloat();
                }
                embeddings.add(embedding);
            }
            
            ois.close();
            fis.close();
            Log.d(TAG, "Embeddings loaded successfully for user: " + userName);
        } catch (IOException e) {
            Log.e(TAG, "Error loading embeddings: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new EmbeddingsData(embeddings, userName);
    }

    public static class EmbeddingsData {
        public final List<float[]> embeddings;
        public final String userName;

        public EmbeddingsData(List<float[]> embeddings, String userName) {
            this.embeddings = embeddings;
            this.userName = userName;
        }
    }
}

// Update MainActivity.java - Add these modifications:

public class MainActivity extends AppCompatActivity {
    // Add new field
    private FaceEmbeddingsStorage embeddingsStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... existing code ...
        
        // Initialize storage
        embeddingsStorage = new FaceEmbeddingsStorage(this);
        
        // Load saved embeddings
        loadSavedEmbeddings();
    }

    private void loadSavedEmbeddings() {
        FaceEmbeddingsStorage.EmbeddingsData data = embeddingsStorage.loadEmbeddings();
        if (!data.embeddings.isEmpty()) {
            storedEmbeddings = data.embeddings;
            userName = data.userName;
            Toast.makeText(this, "Loaded embeddings for: " + userName, Toast.LENGTH_SHORT).show();
        }
    }

    private void storeFaceEmbeddings() {
        // ... existing code for processing images ...

        // After processing all images and getting embeddings, save them
        if (!storedEmbeddings.isEmpty()) {
            embeddingsStorage.saveEmbeddings(storedEmbeddings, userName);
            Toast.makeText(this, "Face embeddings stored successfully", Toast.LENGTH_SHORT).show();
        }
    }
}