public class median {
    public static void main(String []args) {
        int[] A = {1,1,3,5,7,8,9};
        int[] B = {2,4,5};

        // A的长度和startA
        int m = A.length;
        int startA = 0;

        // B的长度和startB
        int n = B.length;
        int startB = 0;

        // A和B的总长度
        int length = m + n;
        double knum;

        // 如果总长度为奇数，则直接返回一个数
        if(length % 2 == 1) {
            knum = 1.0 * find_kth(A, startA, m, B, startB, n, length / 2 + 1);

        }
        // 如果总长度为偶数，则需要返回两个数的平均数
        else {
            knum = 1.0 * (find_kth(A, startA, m, B, startB, n, length / 2)
                    + find_kth(A, startA, m, B, startB, n, length / 2 + 1)) / 2;
        }

        System.out.println(knum);
    }

    public static int find_kth(int[] A, int startA, int m, int[] B, int startB, int n, int k) {


        // 保证A的长度少于B的长度
        if((m - startA) > (n - startB)) {
            return find_kth(B, startB, n, A, startA, m, k);
        }

        // 当A为空，说明A此时的值都在前k中，第k大数在B里，直接返回
        if(startA == m) {
            return B[k-1];
        }

        // 若k==1，比较A[0]和B[0]的值，取最小
        if(k==1) {
            return Math.min(A[startA], B[startB]);
        }

        // 将k划分为两个部分
        int nextStartA = startA + Math.min(m-startA, k/2);
        int nextStartB = startB + k - nextStartA + startA;

        // 如果A[nextStartA - 1] < B[nextStartB - 1]，去掉A的前 nextStartA - startA 个，在剩余的A和B里寻找第 k - nextStartA + startA
        if(A[nextStartA - 1] < B[nextStartB - 1]) {
            return find_kth(A, nextStartA, m, B, startB, n, k - nextStartA + startA);
        }

        // 如果A[nextStartA - 1] > B[nextStartB - 1]，去掉B的前 nextStartB - startB 个，在剩余的B和A里寻找第 k - nextStartB + startB
        else if (A[nextStartA - 1] > B[nextStartB - 1]) {
            return find_kth(A, startA, m, B, nextStartB, n, k - nextStartB + startB);
        }

        // 如果A[nextStartA - 1] == B[nextStartB - 1]，直接返回
        else {
            return A[nextStartA];
        }
    }
}
