public class mergeSort {
    public static void main(String []args) {
        int[] A = {1,1,2,3,5,7};
        int[] B = {1,3};
        int k = 7;
        int knum = find_kth(A, B, k);
        System.out.println(knum);
    }
    public static int find_kth(int[] A, int[] B, int k) {

        // 数组从前向后遍历
        int i=0;
        int j=0;

        // 计数器
        int m=0;

        // 两个数组比较
        while(i<A.length && j<B.length) {

            // A[i]<B[j] i++
            if(A[i]<B[j]) {
                i=i+1;
                m=m+1;
                if(m>=k) {
                    return A[i-1];
                }
            }

            // A[i]>B[j] j++
            else if(A[i]>B[j]) {
                j=j+1;
                m=m+1;
                if(m>=k) {
                    return B[j-1];
                }
            }
            else {
                i=i+1;
                j=j+1;
                m=m+2;
                if(m>=k) {
                    return A[i-1];
                }
            }

        }

        // 当A元素遍历完，第k大元素在B中
        if(i==A.length) {
            return B[j+k-m-1];
        }

        // 当B元素遍历完，第k大元素在A中
        if(j==B.length) {
            return A[i+k-m-1];
        }

        return -1;
    }
}
