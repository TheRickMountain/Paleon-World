package com.paleon.engine.utils;


import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rick on 06.10.2016.
 */
public class Utils {

    private Utils() {}

    public static float[] joinArrays(float[] vertices, float[] uvs) {
        float[] data = new float[vertices.length + uvs.length];
        int count = 0;
        int vertexCount = 0;
        int uvsCount = 0;
        for(int i = 0; i < data.length / 4; i++) {
            data[count] = vertices[vertexCount];
            vertexCount++;
            count++;
            data[count] = vertices[vertexCount];
            vertexCount++;
            count++;
            data[count] = uvs[uvsCount];
            uvsCount++;
            count++;
            data[count] = uvs[uvsCount];
            uvsCount++;
            count++;
        }
        return data;
    }

    public static float getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        float dX = x1 - x2;
        float dY = y1 - y2;
        float distance = (float) Math.sqrt((dX * dX) + (dY * dY));
        return distance;
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static Vector3f calculateNormal(Vector3f point0, Vector3f point1, Vector3f point2) {
        Vector3f vectorA = new Vector3f();
        point1.sub(point0, vectorA);

        Vector3f vectorB = new Vector3f();
        point2.sub(point0, vectorB);

        Vector3f normal = new Vector3f();
        vectorA.cross(vectorB, normal);

        normal.normalize();
        return normal;
    }

    public static String [] removeEmptyStrings(String[] data)
    {
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < data.length; i++)
            if(!data[i].equals(""))
                result.add(data[i]);

        String[] res = new String[result.size()];
        result.toArray(res);

        return res;
    }

    public static float getAverageOfList(List<Float> numbers) {
        float total = 0;
        for (Float number : numbers) {
            total += number;
        }
        return total / numbers.size();
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static float[] listFloatToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static int[] listIntToArray(List<Integer> list) {
        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
        return result;
    }

}
