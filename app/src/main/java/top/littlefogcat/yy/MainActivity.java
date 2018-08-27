package top.littlefogcat.yy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class MainActivity extends AppCompatActivity {
    private View mSettingFrame;
    private ImageButton mHeartBtn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpUtils.init(this);

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationService.class);
            startService(intent);
        });

        findViewById(R.id.btnStart1).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationService.class);
            intent.putExtra("type", 1);
            startService(intent);
        });

        mHeartBtn = findViewById(R.id.imageButton);
        mSettingFrame = findViewById(R.id.settingFrame);
        mHeartBtn.setOnClickListener(v -> {
            ScaleAnimation animation = new ScaleAnimation(1, 1.5f,
                    1, 1.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(500);
            mHeartBtn.startAnimation(animation);
            int x = (int) mHeartBtn.getX();
            int y = (int) mHeartBtn.getY();
            int w = mHeartBtn.getWidth();
            int h = mHeartBtn.getHeight();

            if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                ViewAnimationUtils.createCircularReveal(mSettingFrame,
                        x + w / 2, y + h / 2, w / 2,
                        (float) Math.hypot(x + w / 2, y + h / 2))
                        .setDuration(1000)
                        .start();
                mSettingFrame.setVisibility(View.VISIBLE);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSettingFrame.setZ(10);
            mSettingFrame.setOnTouchListener((v, event) -> true);
        }

        findViewById(R.id.btnSettingOk).setOnClickListener(v -> {
            SpUtils.putBoolean(Constant.SP_KEY_SHOW_PERSIST_NOTIFICATION, true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int w = v.getWidth();
                int r = w / 2;

                Animator animator = ViewAnimationUtils.createCircularReveal(mSettingFrame,
                        (int) (v.getX() + r),
                        (int) (v.getY() + r),
                        mSettingFrame.getHeight() - v.getY() + r,
                        0)
                        .setDuration(1000);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSettingFrame.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(MainActivity.this, NotificationService.class);
                        intent.putExtra(Constant.INTENT_EXTRA_FLAG_CLEAR_NOTIFICATION, true);
                        startService(intent);
                    }
                });
                animator.start();
            }
        });
        findViewById(R.id.btnSettingCancel).setOnClickListener(v -> {
            SpUtils.putBoolean(Constant.SP_KEY_SHOW_PERSIST_NOTIFICATION, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int w = v.getWidth();
                int r = w / 2;

                Animator animator = ViewAnimationUtils.createCircularReveal(mSettingFrame,
                        (int) (v.getX() + r),
                        (int) (v.getY() + r),
                        mSettingFrame.getHeight() - v.getY() + r,
                        0)
                        .setDuration(1000);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSettingFrame.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(MainActivity.this, NotificationService.class);
                        intent.putExtra(Constant.INTENT_EXTRA_FLAG_CLEAR_NOTIFICATION, true);
                        startService(intent);
                    }
                });
                animator.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSettingFrame == null || mSettingFrame.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(mSettingFrame,
                    mSettingFrame.getWidth() / 2,
                    0, mSettingFrame.getHeight(), 0)
                    .setDuration(1200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSettingFrame.setVisibility(View.INVISIBLE);
                }
            });
            animator.start();
        }
    }
}
