package com.bignerdranch.android.initialtwittersyncadapter.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;

public class RowItemView extends View {
    private static final int DIVIDER_SIZE = 1;
    private static final int LARGE_TEXT_SIZE = 14;
    private static final int SMALL_TEXT_SIZE = 12;
    private static final int PADDING_SIZE = 8;

    private String mTweet;
    private String mUsername;
    private Drawable mProfileImage;

    private Paint mBackgroundPaint;
    private Paint mDividerPaint;

    private TextPaint mTweetTextPaint;
    private TextPaint mUsernameTextPaint;

    private float mScreenDensity;
    private float mDividerSize;
    float mLargeTextSize;
    float mSmallTextSize;
    float mPaddingSize;

    public RowItemView(Context context) {
        this(context, null);
    }

    public RowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScreenDensity = context.getResources().getDisplayMetrics().density;
        mDividerSize = Math.round(DIVIDER_SIZE * mScreenDensity);

        Configuration configuration = context.getResources().getConfiguration();
        float textScale = configuration.fontScale * mScreenDensity;
        mLargeTextSize = Math.round(LARGE_TEXT_SIZE * textScale);
        mSmallTextSize = Math.round(SMALL_TEXT_SIZE * textScale);

        mPaddingSize = Math.round(PADDING_SIZE * mScreenDensity);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mDividerPaint = new Paint();
        mDividerPaint.setColor(Color.LTGRAY);
        mDividerPaint.setStrokeWidth(mDividerSize);

        mTweetTextPaint = new TextPaint();
        mTweetTextPaint.setTextSize(mLargeTextSize);
        mTweetTextPaint.setTextAlign(Paint.Align.LEFT);
        mTweetTextPaint.setColor(Color.BLACK);
        mTweetTextPaint.setAntiAlias(true);
        mUsernameTextPaint = new TextPaint();
        mUsernameTextPaint.setTextSize(mSmallTextSize);
        mUsernameTextPaint.setTextAlign(Paint.Align.LEFT);
        mUsernameTextPaint.setColor(Color.BLACK);
        mUsernameTextPaint.setAntiAlias(true);
    }

    public void setTweet(String tweet) {
        mTweet = tweet;
        invalidate();

        setContentDescription(mTweet);
    }

    public void setUsername(String username) {
        mUsername = username;
        invalidate();
    }

    public void setProfileImage(GlideDrawable profileImage) {
        mProfileImage = profileImage;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = calculateWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            height = heightSize;
        } else {
            height = calculateHeight();
        }

        setMeasuredDimension(width, height);
    }

    private int calculateWidth() {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(size);
        // use window width if unspecified
        return size.x;
    }

    private int calculateHeight() {
        int layoutPadding = getPaddingTop() + getPaddingBottom();
        Paint.FontMetrics tweetFm = mTweetTextPaint.getFontMetrics();
        float tweetHeight = getFontHeight(tweetFm);
        Paint.FontMetrics usernameFm = mUsernameTextPaint.getFontMetrics();
        float usernameHeight = getFontHeight(usernameFm);
        float totalHeight = layoutPadding + mPaddingSize + tweetHeight
                + usernameHeight + mPaddingSize + mDividerSize;
        return (int) totalHeight;
    }

    private float getFontHeight(Paint.FontMetrics metrics) {
        return (float) (Math.ceil(Math.abs(metrics.top)) +
                Math.ceil(Math.abs(metrics.bottom)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();

        // draw paint to clear canvas
        canvas.drawPaint(mBackgroundPaint);
        // Draw the divider across the bottom of the canvas
        float dividerY = height - (mDividerSize / 2);
        canvas.drawLine(0, dividerY, width, dividerY, mDividerPaint);

        canvas.clipRect(mPaddingSize, mPaddingSize, width - mPaddingSize, height - mPaddingSize);

        float textLeft = mPaddingSize;

        if (mProfileImage != null) {
            mProfileImage.setBounds(
                    (int) mPaddingSize,
                    (int) mPaddingSize,
                    (int) mPaddingSize + mProfileImage.getIntrinsicWidth(),
                    (int) mPaddingSize + mProfileImage.getIntrinsicHeight());
            mProfileImage.draw(canvas);

            textLeft += mPaddingSize + mProfileImage.getIntrinsicWidth();
        }

        Paint.FontMetrics tweetFm = mTweetTextPaint.getFontMetrics();
        float tweetTop = (float) Math.ceil(Math.abs(tweetFm.top));
        float tweetBottom = (float) Math.ceil(Math.abs(tweetFm.bottom));
        float tweetBaseline = mPaddingSize + tweetTop;
        canvas.drawText(mTweet, textLeft, tweetBaseline, mTweetTextPaint);

        if (mUsername != null) {
            Paint.FontMetrics usernameFm = mUsernameTextPaint.getFontMetrics();
            float usernameTop = (float) Math.ceil(Math.abs(usernameFm.top));
            float usernameBaseline = tweetBaseline + tweetBottom + usernameTop;
            canvas.drawText(mUsername, textLeft, usernameBaseline, mUsernameTextPaint);
        }
    }
}
