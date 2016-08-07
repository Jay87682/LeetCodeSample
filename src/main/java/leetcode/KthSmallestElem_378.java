package leetcode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jay on 8/4/16.
 */

/*
 * 378. Kth Smallest Element in a Sorted Matrix
 *  Given a n x n matrix where each of the rows and columns are sorted in ascending order,
 *  find the kth smallest element in the matrix.
 *  Note that it is the kth smallest element in the sorted order, not the kth distinct element.
 *
 *  Idea 1:
 *  * Sorted the matrix until find the Kth min number
 *  * Take one element as the center, drawing a cross.
 *    We can get four phases, the top-left is always smaller and the down-right is always bigger.
 *  * Solution:
 *    1. Choose the element in the first column as the max number and find the min number on previous
 *       rows each time.
 *    2. Using 1. until scanning to last row
 *    3. Find the min number via vertical scanning
 *    (NOTE: Previous 3 steps will be interrupted util find the kth min number)
 *
 *  TODO:
 *  * If kth > (nxn)/2, find the (n*n)-k large value
 */


public class KthSmallestElem_378 {
    //Test saamples in leetcode@378
    private int[][] Matrix = {
            /*
            {3, 8, 8},
            {3, 8, 8},
            {3, 9, 13}
            */
            /*
            {1, 5, 9},
            {10, 11, 13},
            {12, 13, 15}
            */

            {1, 3, 5},
            {6, 7, 12},
            {11, 14, 14}
            /*
            {1, 2},
            {1, 3}
            */
            /*
            {1, 3, 5, 7, 9},
            {2, 4, 6, 8, 10},
            {11, 13, 15, 17 ,19},
            {12, 14, 16, 18, 20},
            {21, 22, 23, 24, 25}
             */
    };

    private int K = 8;
    //

    public KthSmallestElem_378(){}

    final String QUESTION_PREFIX = this.getClass().getName() + " answer: ";

    //The min number will be sorted in the buffer with 80k size
    final int MAX_SIZE = 81920; //80k

    public void Solution(){
        System.out.println(QUESTION_PREFIX + kthSmallest(Matrix, K));
    }

    private int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int max_idx = n - 1;

        //Amount of element in matrix is over size whether
        if (!outOfBound(n))
            return -1;

        //Quick detect the edge
        if ( k == 1 )
            return matrix[0][0];
        else if ( k == (n * n) )
            return matrix[max_idx][max_idx];

        //Record the min number
        ArrayList<Integer> sortBuckBuf = new ArrayList<>();
        //Record the scanning idx of the row
        ArrayList<Integer> breakPointBuf = new ArrayList<>();



        //Init (Add the [0, 0] first)
        sortBuckBuf.add(matrix[0][0]);
        breakPointBuf.add(1);
        int curMaxIndex = 1;
        int curMax = matrix[curMaxIndex][0];

        int min, min_x;
        while (curMaxIndex < n) {
            do {
                min_x = findMinmun(matrix, breakPointBuf, curMaxIndex, n);

                if (matrix[min_x][breakPointBuf.get(min_x)] <= curMax) {
                    if (checkKthMin(sortBuckBuf, matrix[min_x][breakPointBuf.get(min_x)], k))
                        return sortBuckBuf.get(k-1);
                    breakPointBuf.set(min_x, breakPointBuf.get(min_x) + 1);
                }else {
                    break;
                }

                if (checkFull(breakPointBuf, n))
                    break;
            } while (true);

            if (checkKthMin(sortBuckBuf, curMax, k))
                return sortBuckBuf.get(k-1);

            breakPointBuf.add(1);

            curMaxIndex++;
            if (curMaxIndex < n)
                curMax = matrix[curMaxIndex][0];
        }

        System.out.println("pre-last bp: "  + breakPointBuf.toString());
        System.out.println("pre-last Sort Buf: " + sortBuckBuf.toString());

        while (sortBuckBuf.size() < (n*n)) {

            min_x = findMinmun(matrix, breakPointBuf, curMaxIndex, n);
            min = matrix[min_x][breakPointBuf.get(min_x)];

            if (checkKthMin(sortBuckBuf, min, k))
                return sortBuckBuf.get(k-1);

            breakPointBuf.set(min_x, breakPointBuf.get(min_x) + 1);
        }

        System.out.println("bp: "  + breakPointBuf.toString());
        System.out.println("Sort Buf : " + Arrays.toString(sortBuckBuf.toArray()));

        return 0;
    }

    private boolean outOfBound(int matrix_length){
        return matrix_length < (MAX_SIZE/2) ? true : false;
    }

    private boolean checkFull(ArrayList<Integer> bp_buf, int matrix_size){
        for (int i = 0; i < bp_buf.size(); i++)
            if (bp_buf.get(i) < matrix_size)
                return false;

        return true;
    }

    private boolean checkKthMin(ArrayList<Integer> sort_buf, int min_value, int k_th) {
        sort_buf.add(min_value);
        if (sort_buf.size() == k_th)
            return true;

        return false;
    }

    private int findMinmun(int[][] matrix_main, ArrayList<Integer> bp_buf, int cur_bound, int matrix_leng) {
        int min = Integer.MAX_VALUE;
        int min_idx = 0, tmp_idx = 0;

        do {
            if (bp_buf.get(tmp_idx) == matrix_leng) {
                tmp_idx++;
                continue;
            }

            if (matrix_main[tmp_idx][bp_buf.get(tmp_idx)] <= min) {
                min_idx = tmp_idx;
                min = matrix_main[min_idx][bp_buf.get(min_idx)];
            }
            tmp_idx++;
        } while (tmp_idx < cur_bound);
        return min_idx;
    }
}
