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
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CameraService extends Service {
    private static final String TAG = "CameraService";

    private static final String CHANNEL_ID = "CameraServiceChannel";
    private Camera camera;
    private Handler handler = new Handler();
    private int iterationCount = 0;
    private static final int TOTAL_ITERATIONS = 4;
    private static final int CAMERA_DURATION = 1000; // Duration to keep the camera open in milliseconds
    private static final int WAIT_DURATION = 6000; // Duration to wait before reopening the camera in milliseconds

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

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

            // Schedule to close the camera after CAMERA_DURATION
            handler.postDelayed(this::closeCamera, CAMERA_DURATION);

            // Schedule next cycle after WAIT_DURATION
            handler.postDelayed(this::startCameraCycle, CAMERA_DURATION + WAIT_DURATION);
        } else {
            stopSelf();
        }
    }

    private void openCamera() {
        try {
            if (camera == null) {
                camera = Camera.open();
                Log.d(TAG, "Camera opened");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to open camera", e);
        }
    }

    private void closeCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
            Log.d(TAG, "Camera closed");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCamera();
        Log.d(TAG, "Camera service destroyed");
    }

    @Nullable
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






ppp<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <FrameLayout
        android:layout_width="341dp"
        android:layout_height="wrap_content">

            <com.android.UI.ThemeAwareImageView
                android:id="@+id/card_slider"
                android:layout_width="341dp"
                android:layout_height="385dp"
                android:layout_marginTop="4dp"
                android:background="@drawable/card_large_background_selector"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <androidx.constraintlayout.widget.ConstraintLayout
                android:layout_width="341dp"
                android:layout_height="wrap_content"
                android:orientation="vertical">
                <LinearLayout
                    android:id="@+id/card_headers"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:orientation="horizontal"
                    android:padding="4dp"
                    android:layout_margin="8dp"
                    app:layout_constraintEnd_toEndOf="parent"
                    app:layout_constraintTop_toTopOf="parent">

                    <LinearLayout
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="4dp"
                        android:orientation="vertical">

                        <TextView
                            android:id="@+id/genai_title"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:fontFamily="@font/oneui"
                            android:text="@string/anomalies_detected"
                            android:textColor="?android:textColor"
                            android:textSize="18sp"
                            android:layout_marginLeft="8dp"
                            android:textStyle="bold" />

                        <TextView
                            android:id="@+id/date_time"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:fontFamily="@font/oneui"
                            android:text=""
                            android:textSize="12sp"
                            android:layout_marginLeft="8dp"
                            app:layout_constraintTop_toBottomOf="@+id/genai_title" />

                    </LinearLayout>

                    <ImageView
                        android:id="@+id/starImage"
                        android:layout_width="30dp"
                        android:layout_height="30dp"
                        android:layout_marginLeft="100dp"
                         />
                </LinearLayout>

                <LinearLayout
                    android:id="@+id/anomaly_data"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:orientation="horizontal"
                    android:padding="4dp"
                    android:layout_margin="8dp"
                    android:visibility="visible"
                    app:layout_constraintTop_toBottomOf="@+id/card_headers"
                    tools:layout_editor_absoluteX="10dp">

                    <ImageView
                        android:id="@+id/app_icon"
                        android:layout_width="60dp"
                        android:padding="6dp"
                        android:layout_marginRight="10dp"
                        android:layout_height="60dp" />

                    <TextView
                        android:id="@+id/anomoly_msg"
                        android:layout_width="wrap_content"
                        android:layout_marginLeft="8dp"
                        android:paddingRight="4dp"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/oneui"
                        android:ellipsize="end"
                        android:text="Over last 8 days, SnapChat accessed the camera 229 times when the display was turned off. "
                        android:textColor="?android:textColor"
                        android:textSize="12sp"
                        app:layout_constraintStart_toEndOf="@+id/app_icon" />
                </LinearLayout>
        </androidx.constraintlayout.widget.ConstraintLayout>
    </FrameLayout>
</LinearLayout>



    com.android.UI.ThemeAwareImageView cardBackground;

    public CardViewHolder(View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.genai_title);
        cardBackground = itemView.findViewById(R.id.card_slider);
        galaxyStarsImageView=itemView.findViewById(R.id.starImage);
        messageTextView = itemView.findViewById(R.id.anomoly_msg);
        feedbackButton=itemView.findViewById(R.id.feedback_btn);
        reviewPremissionButton = itemView.findViewById(R.id.review_btn);
        dismissButton = itemView.findViewById(R.id.dimiss_btn);
        appIconImageView=itemView.findViewById(R.id.app_icon);
        uninstallButton=itemView.findViewById(R.id.uninstall_btn);
        dateTime=itemView.findViewById(R.id.date_time);

    }
 private void setCollapsedText(String fullText, String collapsedText,CardViewHolder holder,Context mContext, String sentence3) {
        SpannableString spannableString = new SpannableString(collapsedText + "... Read More" + sentence3);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                setExpandedText(fullText, collapsedText,holder,mContext,sentence3);
            }
        };
        spannableString.setSpan(clickableSpan, collapsedText.length() + 1, spannableString.length() - sentence3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), collapsedText.length() + 1, spannableString.length() - sentence3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), collapsedText.length() + 1, spannableString.length() - sentence3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.messageTextView.setText(spannableString);
        holder.messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        holder.messageTextView.setMaxLines(2);
    }

    private void setExpandedText(String fullText, String collapsedText,CardViewHolder holder,Context mContext, String sentence3) {
        SpannableString spannableString = new SpannableString(fullText + "... Read Less" + sentence3);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                setCollapsedText(fullText, collapsedText, holder, mContext, sentence3);
            }
        };
        spannableString.setSpan(clickableSpan, fullText.length() + 1, spannableString.length() - sentence3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), fullText.length() + 1, spannableString.length() - sentence3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), fullText.length() + 1, spannableString.length() - sentence3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.messageTextView.setText(spannableString);
        holder.messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        holder.messageTextView.setMaxLines(Integer.MAX_VALUE);
    }


 public void onBindViewHolder(CardViewHolder holder, int position) {
        Pair<Integer, String> tipEntry = tipsList.get(position);
        int tipId = tipEntry.first;
        String tipMessage = tipEntry.second;

        String anomaliesDetectedStr =  activity.getString(R.string.anomalies_detected);
        holder.titleTextView.setText("("+ tipsList.size() + ") " + anomaliesDetectedStr);
        String appName = mPrivacyTipsDataPointDao.getAppNameFromTipId(tipId);
        String package_name=getPackageNameFromAppName(activity.getApplicationContext(),appName);
        long anomalyTimestamp=mPrivacyTipsDataPointDao.getTimeStampFromTipId(tipId);
        int anomalySource = mPrivacyTipsDataPointDao.getAnomalySourceFromTipId(tipId);
        pkg=package_name;
        Drawable app_icon =getAppIconFromAppName(activity.getApplicationContext(), appName);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(anomalyTimestamp);
        String formattedDate = sdf.format(date);
        String customForamttedDateTime = formattedDate;
        String strToday=activity.getString(R.string.today_at);
        String strYesterday=activity.getString(R.string.yesterday);
        try {
            customForamttedDateTime = getFormattedDateString(formattedDate,strToday,strYesterday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.dateTime.setText(customForamttedDateTime);
        holder.appIconImageView.setImageDrawable(app_icon);

        String tipMessage_feedback = tipMessage.replaceAll("<RM>", "").replaceAll("<S>", "");
        if (anomalySource == 1) {
            String sentence1 = tipMessage.split("<RM>")[0].trim();
            String sentence2 =  " " + tipMessage.split("<RM>")[1].split("<S>")[0].trim();
            String sentence3 = "\n" + tipMessage.split("<S>")[1].trim();

            Log.i(TAG,"sentence 1 :"+sentence1);
            Log.i(TAG,"sentence 2 :"+sentence2);
            Log.i(TAG,"sentence 3 :"+sentence3);
            String fullText = sentence1 + " " + sentence2;
            String collapsedText = sentence1;
            setCollapsedText(fullText,collapsedText,holder,mContext, sentence3);
            // TODO : set height of holder.cardBackground dynamically here


        } else {
            holder.messageTextView.setText(tipMessage);
        }

      
        });

    ans
// Method to adjust the height of a View dynamically
private void adjustCardHeight(CardViewHolder holder, String fullText, String collapsedText) {
    // Measure the height of the TextView with collapsed and expanded text
    holder.messageTextView.setText(collapsedText);
    holder.messageTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    int collapsedHeight = holder.messageTextView.getMeasuredHeight();

    holder.messageTextView.setText(fullText);
    holder.messageTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    int expandedHeight = holder.messageTextView.getMeasuredHeight();

    // Dynamically set the height of the cardBackground based on the TextView's content
    ViewGroup.LayoutParams layoutParams = holder.cardBackground.getLayoutParams();
    layoutParams.height = expandedHeight > collapsedHeight ? expandedHeight + 50 : collapsedHeight + 50; // Add padding for better spacing
    holder.cardBackground.setLayoutParams(layoutParams);
}
  
