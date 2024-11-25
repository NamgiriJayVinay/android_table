package com.example.sensors_app1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class CameraService extends Service {
    private static final String TAG = "SensorReceiver";

    private static final String CHANNEL_ID = "CameraServiceChannel";
    private Camera camera;
    private WindowManager windowManager;
    private View cameraPreviewView;
    private Handler handler = new Handler();
    private int iterationCount = 0;
    private static final int TOTAL_ITERATIONS = 4;
    private static final int CAMERA_DURATION = 1000; // Duration for camera to stay on in milliseconds
    private static final int WAIT_DURATION = 6000; // Duration to wait before starting the camera again in milliseconds

    private Runnable stopRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        cameraPreviewView = LayoutInflater.from(this).inflate(R.layout.camera_preview, null);

        SurfaceView surfaceView = cameraPreviewView.findViewById(R.id.cameraSurfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                camera = Camera.open();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                }
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                android.graphics.PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(cameraPreviewView, params);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Camera Service")
                .setContentText("Camera is running")
                .build();
        startForeground(1, notification);
        Log.d(TAG, "Camera service started inside camera service on create ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int duration = intent.getIntExtra("DURATION", 0);
        scheduleStop(duration);
        Log.d(TAG, "Camera service started inside on start command ");

        return START_NOT_STICKY;
    }

    private void scheduleStop(int duration) {
        if (stopRunnable != null) {
            handler.removeCallbacks(stopRunnable);
        }
        stopRunnable = () -> {
            iterationCount++;
            if (iterationCount < TOTAL_ITERATIONS) {
                openCamera();
                handler.postDelayed(() -> closeCamera(), CAMERA_DURATION);
                handler.postDelayed(() -> scheduleStop(duration), WAIT_DURATION);
            } else {
                closeCamera();
                stopSelf();
            }
        };
        handler.postDelayed(stopRunnable, duration * 1000); // Convert duration to milliseconds
    }

    private void openCamera() {
        if (camera == null) {
            SurfaceView surfaceView = cameraPreviewView.findViewById(R.id.cameraSurfaceView);
            SurfaceHolder holder = surfaceView.getHolder();
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    camera = Camera.open();
                    try {
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    if (camera != null) {
                        camera.stopPreview();
                        camera.release();
                    }
                }
            });
        }
    }

    private void closeCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (cameraPreviewView != null) {
            windowManager.removeView(cameraPreviewView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        if (cameraPreviewView != null) {
            windowManager.removeView(cameraPreviewView);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Camera Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}


00
package com.example.sensors_app1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class CameraService extends Service {
    private static final String TAG = "CameraService";

    private static final String CHANNEL_ID = "CameraServiceChannel";
    private Camera camera;
    private WindowManager windowManager;
    private View cameraPreviewView;
    private Handler handler = new Handler();
    private int iterationCount = 0;
    private static final int TOTAL_ITERATIONS = 4;
    private static final int CAMERA_DURATION = 1000; // Duration for camera to stay on in milliseconds
    private static final int WAIT_DURATION = 6000; // Duration to wait before starting the camera again in milliseconds

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        cameraPreviewView = LayoutInflater.from(this).inflate(R.layout.camera_preview, null);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Camera Service")
                .setContentText("Camera is running")
                .setSmallIcon(R.drawable.ic_camera) // Replace with your app's icon
                .build();
        startForeground(1, notification);

        Log.d(TAG, "Camera service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCameraCycle();
        return START_NOT_STICKY;
    }

    private void startCameraCycle() {
        if (iterationCount < TOTAL_ITERATIONS) {
            iterationCount++;
            Log.d(TAG, "Iteration: " + iterationCount);
            openCamera();

            // Schedule to stop camera preview after CAMERA_DURATION
            handler.postDelayed(this::closeCamera, CAMERA_DURATION);

            // Schedule next cycle after WAIT_DURATION
            handler.postDelayed(this::startCameraCycle, CAMERA_DURATION + WAIT_DURATION);
        } else {
            stopSelf();
        }
    }

    private void openCamera() {
        if (camera == null) {
            SurfaceView surfaceView = cameraPreviewView.findViewById(R.id.cameraSurfaceView);
            SurfaceHolder holder = surfaceView.getHolder();
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    camera = Camera.open();
                    try {
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                        Log.d(TAG, "Camera preview started");
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting camera preview", e);
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    closeCamera();
                }
            });

            // Add the camera preview view to the window manager
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                            : WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    android.graphics.PixelFormat.TRANSLUCENT
            );
            params.gravity = Gravity.TOP | Gravity.LEFT;

            windowManager.addView(cameraPreviewView, params);
        }
    }

    private void closeCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            Log.d(TAG, "Camera preview stopped");
        }
        if (cameraPreviewView != null && cameraPreviewView.isAttachedToWindow()) {
            windowManager.removeView(cameraPreviewView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCamera();
        Log.d(TAG, "Camera service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Camera Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}