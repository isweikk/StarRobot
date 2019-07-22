package com.weikk.widget;

public interface OnRockerListener {
    /**
     * 报告圆盘坐标，相对中心点的坐标
     * @param x
     * @param y
     */
    public void reportPosition(float x, float y);

    /**
     * 报告圆盘的方向，3点钟为0度和360度，顺时针
     * @param direction
     * @param distance
     */
    public void onRocker(float direction, float distance);
}
