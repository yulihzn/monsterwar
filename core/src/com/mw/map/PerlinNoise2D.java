package com.mw.map;

public class PerlinNoise2D {
	double persistence = .2; // 幅度/频率 （抖动程度，越高越抖，最高1）
    int octaves = 3; //倍频（精细度，越高越精细）
    
    public PerlinNoise2D() {}
    
    public PerlinNoise2D(double persistence, int octaves) {
        this.persistence = persistence;
        this.octaves = octaves;
    }

    double noise(int x, int y) { //噪声函数
        int n = x + y * 1367;
        n = (n << 13) ^ n;
        return (1d - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824d);
    }
    
    double smoothedNoise(int x, int y) { //光滑函数
        double corners = (noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, y + 1) + noise(x + 1, y + 1)) / 16,
                sides = (noise(x - 1, y) + noise(x + 1, y) + noise(x, y - 1) + noise(x, y + 1)) / 8,
                center = noise(x, y) / 4;
        return corners + sides + center;
    }
    
    double interpolate(double a, double b, double x) { //余弦插值函数
        double f = (1 - Math.cos(x * 3.1415927)) * .5;
        return a * (1 - f) + b * f;
    }
    
    double interpolatedNoise(double x, double y) {
        int ix = (int) x, iy = (int) y;
        double v1 = smoothedNoise(ix, iy),
                v2 = smoothedNoise(ix + 1, iy),
                v3 = smoothedNoise(ix, iy + 1),
                v4 = smoothedNoise(ix + 1, iy + 1),
                fx = x - ix,
                fy = y - iy,
                i1 = interpolate(v1, v2, fx),
                i2 = interpolate(v3, v4, fx);
        return interpolate(i1, i2, fy);
    }

    public double perlinNoise(double x, double y) {
        double f = 0d, frequency, amplitude;
        for (int i = 0; i < octaves; i++) {
            frequency = Math.pow(2, i);
            amplitude = Math.pow(persistence, i);
            f += interpolatedNoise(x * frequency, y * frequency) * amplitude;
        }
        return f;
    }
}
