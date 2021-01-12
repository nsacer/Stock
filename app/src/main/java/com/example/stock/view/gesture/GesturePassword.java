package com.example.stock.view.gesture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 手势登陆view
 */
public class GesturePassword extends View {

    //圆圈的三种状态:正常、选中、出错
    private final int STATE_NORMAL = 0;
    private final int STATE_SELECT = 1;
    private final int STATE_ERROR = 2;
    //圆圈顺序是否是从左上横向排序
    private boolean isStartHorizontal = Boolean.TRUE;
    boolean movingNoPoint = false;
    float movingX, movingY;
    //是否已经创建了9个圆圈
    private boolean isCache = false;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Point[][] mPoints = new Point[3][3];
    //圆圈的半径
    private float dotRadius = 0;
    private final ArrayList<Point> sPoints = new ArrayList<>();
    private boolean checking = false;
    //输入完成、输入错误清除密码时间（ms）
    private final long CLEAR_TIME = 400;
    //密码支持的最长个数
    private final int pwdMaxLen = 9;
    //密码设置最短个数
    private final int pwdMinLen = 4;
    //是否相应触摸操作
    private boolean isTouchEnable = true;
    //连接线上的箭头Paint
    private Paint arrowPaint;
    //连接线的paint
    private Paint linePaint;
    //选中状态的paint
    private Paint selectedPaint;
    //错误状态的paint
    private Paint errorPaint;
    //正常状态的paint
    private Paint normalPaint;
    //正常、错误箭头颜色
    private int colorArrow = 0x00009988;
    private int colorArrowError = 0x00009933;
    //正常、错误连接线颜色
    private int colorLine = 0xffBE1A25;
    private int colorLineError = 0xffBE1A25;
    //选中状态的内圈、外圈颜色
    private int selectedColor = 0xffBE1A25;
    private int outterSelectedColor = 0xffBE1A25;
    //密码错误状态内圈、外圈的颜色
    private int errorColor = 0xffe94b4b;
    private int outErrorColor = 0xffe94b4b;
    //正常状态的内圈、外圈颜色
    private int dotColor = 0x00333333;
    private int outDotColor = 0xffBE1A25;
    //设置的监听接口
    private OnCompleteListener mCompleteListener;
    //延迟清除UI的timer
    private final Timer timer = new Timer();
    private TimerTask task = null;

    public GesturePassword(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GesturePassword(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GesturePassword(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width_height = MeasureSpec.getSize(widthMeasureSpec) / 10 * 9;
        setMeasuredDimension(width_height, width_height);
    }

    @Override
    public void onDraw(Canvas canvas) {

        if (!isCache) {
            initCache();
        }
        drawToCanvas(canvas);
    }

    private void drawToCanvas(Canvas canvas) {

        boolean inErrorState = false;

        for (Point[] mPoint : mPoints) {

            for (Point p : mPoint) {

                if (p.state == STATE_SELECT) {

                    selectedPaint.setStyle(Style.STROKE);
                    selectedPaint.setColor(outterSelectedColor);
                    canvas.drawCircle(p.x, p.y, dotRadius, selectedPaint);
                    selectedPaint.setStyle(Style.FILL);
                    selectedPaint.setColor(selectedColor);
                    canvas.drawCircle(p.x, p.y, dotRadius / 4, selectedPaint);

                } else if (p.state == STATE_ERROR) {

                    inErrorState = true;
                    errorPaint.setStyle(Style.STROKE);
                    errorPaint.setColor(outErrorColor);
                    canvas.drawCircle(p.x, p.y, dotRadius, errorPaint);
                    errorPaint.setStyle(Style.FILL);
                    errorPaint.setColor(errorColor);
                    canvas.drawCircle(p.x, p.y, dotRadius / 4, errorPaint);

                } else {

                    normalPaint.setColor(outDotColor);
                    canvas.drawCircle(p.x, p.y, dotRadius, normalPaint);
                    normalPaint.setColor(dotColor);
                    canvas.drawCircle(p.x, p.y, dotRadius / 4, normalPaint);

                }
            }
        }

        if (inErrorState) {
            arrowPaint.setColor(colorArrowError);
            linePaint.setColor(colorLineError);
        } else {
            arrowPaint.setColor(colorArrow);
            linePaint.setColor(colorLine);
        }

        if (sPoints.size() > 0) {
//            int tmpAlpha = mPaint.getAlpha();
            Point tp = sPoints.get(0);
            for (int i = 1; i < sPoints.size(); i++) {
                Point p = sPoints.get(i);
                drawLine(tp, p, canvas, linePaint);
                drawArrow(canvas, arrowPaint, tp, p, dotRadius / 4, 38);
                tp = p;
            }
            if (this.movingNoPoint) {
                drawLine(tp, new Point(movingX, movingY, -1), canvas, linePaint);
            }
//            mPaint.setAlpha(tmpAlpha);
        }

    }

    //绘制直线
    private void drawLine(Point start, Point end, Canvas canvas, Paint paint) {

        double d = distanceBetweenTwoPoint(start.x, start.y, end.x, end.y);
        float rx = (float) ((end.x - start.x) * dotRadius / 4 / d);
        float ry = (float) ((end.y - start.y) * dotRadius / 4 / d);
        canvas.drawLine(start.x + rx, start.y + ry, end.x - rx, end.y - ry, paint);
    }

    //绘制箭头
    private void drawArrow(Canvas canvas, Paint paint, Point start, Point end, float arrowHeight, int angle) {

        double d = distanceBetweenTwoPoint(start.x, start.y, end.x, end.y);
        float sin_B = (float) ((end.x - start.x) / d);
        float cos_B = (float) ((end.y - start.y) / d);
        float tan_A = (float) Math.tan(Math.toRadians(angle));
        float h = (float) (d - arrowHeight - dotRadius * 1.1);
        float l = arrowHeight * tan_A;
        float a = l * sin_B;
        float b = l * cos_B;
        float x0 = h * sin_B;
        float y0 = h * cos_B;
        float x1 = start.x + (h + arrowHeight) * sin_B;
        float y1 = start.y + (h + arrowHeight) * cos_B;
        float x2 = start.x + x0 - b;
        float y2 = start.y + y0 + a;
        float x3 = start.x + x0 + b;
        float y3 = start.y + y0 - a;
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.close();
        canvas.drawPath(path, paint);
    }

    //初始化创建9个Point
    private void initCache() {

        float width = getMeasuredWidth();

        float single = width / 6;
        float dou = width / 3;

        int index = 1;
        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                float x, y;
                if (isStartHorizontal) {
                    x = single + dou * j;
                    y = single + dou * i;
                } else {
                    x = single + dou * i;
                    y = single + dou * j;
                }
                mPoints[i][j] = new Point(x, y, index++);
            }
        }

        dotRadius = width / 10;
        isCache = true;

        initPaints();
    }

    //初始化画笔
    private void initPaints() {

        //圆圈的宽度，包括选中和非选中状态
        int mWidthCircle = 2;

        arrowPaint = new Paint();
        arrowPaint.setColor(colorArrow);
        arrowPaint.setStyle(Style.FILL);
        arrowPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(colorLine);
        linePaint.setStyle(Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(mWidthCircle);

        selectedPaint = new Paint();
        selectedPaint.setStyle(Style.FILL);
        selectedPaint.setAntiAlias(true);
        selectedPaint.setStrokeWidth(mWidthCircle);

        errorPaint = new Paint();
        errorPaint.setStyle(Style.FILL);
        errorPaint.setAntiAlias(true);
        errorPaint.setStrokeWidth(mWidthCircle);

        normalPaint = new Paint();
        normalPaint.setStyle(Style.STROKE);
        normalPaint.setAntiAlias(true);
        normalPaint.setStrokeWidth(mWidthCircle);
    }

    /**
     * 检查返回选中的圆圈
     *
     * @param x 触摸的x坐标
     * @param y 触摸的y坐标
     * @return 返回选中的Point
     */
    private Point checkSelectPoint(float x, float y) {

        for (Point[] mPoint : mPoints) {
            for (Point p : mPoint) {
                if (checkInRound(p.x, p.y, dotRadius, (int) x, (int) y)) {
                    return p;
                }
            }
        }
        return null;
    }

    //重置所有圆圈为正常状态
    private void resetAllPoint() {

        for (Point p : sPoints) {
            p.state = STATE_NORMAL;
        }
        sPoints.clear();
        enableTouch();
    }

    /**
     * @param p
     * @return
     */
    private int crossPoint(Point p) {

        if (sPoints.contains(p)) {
            if (sPoints.size() > 2) {
                if (sPoints.get(sPoints.size() - 1).index != p.index) {
                    return 2;
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    private void addPoint(Point point) {
        if (sPoints.size() > 0) {
            Point lastPoint = sPoints.get(sPoints.size() - 1);
            int dx = Math.abs(lastPoint.getColNum() - point.getColNum());
            int dy = Math.abs(lastPoint.getRowNum() - point.getRowNum());
            if ((dx > 1 || dy > 1) && (dx == 0 || dy == 0 || dx == dy)) {
                int middleIndex = (point.index + lastPoint.index) / 2 - 1;
                Point middlePoint = mPoints[middleIndex / 3][middleIndex % 3];
                if (middlePoint.state != STATE_SELECT) {
                    middlePoint.state = STATE_SELECT;
                    sPoints.add(middlePoint);
                }
            }
        }
        this.sPoints.add(point);
    }

    private String toPointString() {
        if (sPoints.size() >= pwdMinLen && sPoints.size() <= pwdMaxLen) {
            StringBuilder sf = new StringBuilder();
            for (Point p : sPoints) {
                sf.append(p.index);
            }
            return sf.toString();
        } else {
            return "";
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isTouchEnable) {
            return false;
        }

        movingNoPoint = false;

        float ex = event.getX();
        float ey = event.getY();
        boolean isFinish = false;
        boolean redraw = false;
        Point p = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (task != null) {
                    task.cancel();
                    task = null;
                }
                resetAllPoint();
                p = checkSelectPoint(ex, ey);
                if (p != null) {
                    checking = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (checking) {
                    p = checkSelectPoint(ex, ey);
                    if (p == null) {
                        movingNoPoint = true;
                        movingX = ex;
                        movingY = ey;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                p = checkSelectPoint(ex, ey);
                checking = false;
                isFinish = true;
                break;
        }
        if (!isFinish && checking && p != null) {

            int rk = crossPoint(p);
            if (rk == 2) {
                movingNoPoint = true;
                movingX = ex;
                movingY = ey;
                redraw = true;
            } else if (rk == 0) {

                p.state = STATE_SELECT;
                addPoint(p);
                redraw = true;
            }
        }

        if (redraw) {

        }
        if (isFinish) {
            if (this.sPoints.size() == 1) {

                this.resetAllPoint();
            } else if (sPoints.size() < pwdMinLen) {

                errorAllPoint();
                clearPassword();
                //触发密码太短接口方法
                if (mCompleteListener != null) mCompleteListener.onPasswordTooShort();
            } else if (sPoints.size() == pwdMaxLen) {

//                error();
//                clearPassword();
                //九个连接完直接保存完成（也可以根据需求做其他处理）
                if (mCompleteListener != null) {
                    disableTouch();
                    mCompleteListener.onComplete(toPointString());
                }
            } else if (mCompleteListener != null) {

                disableTouch();
                //密码输入完成触发完成监听回调
                mCompleteListener.onComplete(toPointString());
            }
        }
        this.postInvalidate();
        return true;
    }

    //将所有的Point处理为错误状态
    private void errorAllPoint() {
        for (Point p : sPoints) {
            p.state = STATE_ERROR;
        }
    }

    //将UI置为错误状态
    public void markError() {
        markError(CLEAR_TIME);
    }

    /**
     * 将Point状态全部置为错误状态
     *
     * @param time 多久之后清除错误状态UI
     */
    public void markError(final long time) {
        errorAllPoint();
        clearPassword(time);
    }

    //设置当前可以触摸操作
    public void enableTouch() {
        isTouchEnable = true;
    }

    //设置当前不可以触摸操作
    public void disableTouch() {
        isTouchEnable = false;
    }

    //清楚掉密码UI（默认留存时间）
    public void clearPassword() {
        clearPassword(CLEAR_TIME);
    }

    /**
     * 清除绘制的连线UI
     *
     * @param time 多久之后清除UI（ms）
     */
    public void clearPassword(final long time) {
        if (time > 1) {
            if (task != null) {
                task.cancel();
            }
            postInvalidate();
            task = new TimerTask() {
                public void run() {
                    resetAllPoint();
                    postInvalidate();
                }
            };
            timer.schedule(task, time);
        } else {
            resetAllPoint();
            postInvalidate();
        }
    }

    /**
     * 使用勾股定理计算两个点之间的直线距离
     *
     * @param x1 第一个点的x坐标
     * @param y1 第一个点的y坐标
     * @param x2 第二个点的x坐标
     * @param y2 第二个点的y坐标
     * @return 两个点之间的直线距离
     */
    private double distanceBetweenTwoPoint(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2));
    }

    //检测手指触摸区域是否在圆圈内
    private boolean checkInRound(float sx, float sy, float r, float x, float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }

    //供外部调用设置密码完成监听接口回调
    public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    //密码输入完成、密码太短的监听接口
    public interface OnCompleteListener {

        void onComplete(String password);

        void onPasswordTooShort();
    }

    //圆圈内部类
    static class Point {

        public float x;
        public float y;
        public int state = 0;
        public int index;

        Point(float x, float y, int value) {
            this.x = x;
            this.y = y;
            this.index = value;
        }

        public int getColNum() {
            return (index - 1) % 3;
        }

        public int getRowNum() {
            return (index - 1) / 3;
        }
    }
}
