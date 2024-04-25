package ques;

import java.util.Arrays;

public class Sol {
    public static int trap(int[] height) {

        int water = 0;
        int leftMax = Integer.MIN_VALUE;
        int[] rmaxList = getRightMaximumList(height);
        int i = 0;
        if(height.length<3) return 0;
        while (height[i] == 0 && i<height.length) {
            ++i;
        }
        for (int j = i; j < height.length; j++) {
            if (height[j]>leftMax)
                leftMax = height[j];
            water += Math.min(leftMax, rmaxList[j]) - height[j];
        }
        return water;

    }

    private static int[] getRightMaximumList(int[] height) {
        int[] rmaxList = new int[height.length];
        int max = Integer.MIN_VALUE;
        for (int i = height.length - 1; i >= 0; i--) {
            if (height[i] > max)
                max = height[i];
            rmaxList[i] = max;
        }
        return rmaxList;
    }

    public static void main(String[] args) {
        System.out.println(trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }


    //[0,1,0,2,1,0,1,3,2,1,2,1]
    //3333333332221
    //increment till non-zeo
    //Min(lmax, rightmax) - a[i]
    //lmax -> min
}
