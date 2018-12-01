寻找两个有序数组的中位数
======================

# 一. 题目描述

> 有两个大小为 m 和 n 的排序数组 nums1 和 nums2，请找出两个排序数组的中位数并且总的运行时间复杂度为 O(log (m+n)) 。

**示例 1:**

>nums1 = [1, 3] 
>nums2 = [2]
>
>中位数是 2.0

**示例 2:**

>nums1 = [1, 2] 
>nums2 = [3, 4]
>
>中位数是 (2 + 3)/2 = 2.5

# 二. 分析

> 这道题更通用的形式是：给定两个已经排好序的数组，找到两者所有元素中第k大的元素。解法有以下两种：

## (一) 直接merge，寻找第k大元素

> 使用两个指针pA和pB，分别指向A和B数组的第一个元素，一个计数器m，利用merge sort的原理：
>
> 当A[pA]<B[pB]，pA++，m++
>
> 当A[pA]>B[pB]，pB++，m++
>
> 当A[pA]=B[pB]，pA++，pB++，m++
>
> 当m=k时，停止。代码如下：

```java
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
```

## (二) 二分思路

> 可以考虑从 k 入手。如果每次都能够删除一个一定在第 k 大元素之前的元素，那么需要进行 k 次。但是如果每次都删除一半，由于 A 和 B 都是有序的，应该充分利用这里面的信息，类似于二分查找，也是充分利用了“有序”。思路如下：

```
假设 A 和 B 的元素个数都大于 k/2，将A的第k/2个元素(即A[k/2-1])和B的第k/2个元素(即B[k/2-1])进行比较，有以下三种情况(为了简化这里先假设 k 为偶数，所得到的结论对于k是奇数也是成立的):

• A[k/2-1] == B[k/2-1] 
• A[k/2-1] > B[k/2-1] 
• A[k/2-1] < B[k/2-1]

当A[k/2-1]<B[k/2-1]，则A[0]到A[k/2-1]的肯定在A∪B的top k元素的范围内，即A[k/2-1]不可能大于A∪B的第k大元素。因此，可以删除A数组的这k/2个元素A[0:k/2-1]；

当A[k/2-1]>B[k/2-1]时，可以删除B数组的k/2个元素B[0:k/2-1]；

当A[k/2-1] == B[k/2-1]时，说明找到了第k大的元素，直接返回 A[k/2-1] 或 B[k/2-1] 即可。

递归函数的终止条件：
当A或B为空时，直接返回B[k-1]或A[k-1]
当k=1时，返回min(A[0], B[0])
当A[k/2-1]==B[k/2-1]时，返回A[k/2-1]==B[k/2-1]
```

> 代码如下：

```java
public class binaryKth {
    public static void main(String []args) {
        int[] A = {1,1,3,5,7,8,9};
        int[] B = {2,4,5};

        int k = 6;

        // A的长度和startA
        int m = A.length;
        int startA = 0;

        // B的长度和startB
        int n = B.length;
        int startB = 0;

        int knum = find_kth(A, startA, m, B, startB, n, k);
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

        // 如果A[nextStartA] < B[nextStartB]，去掉A的前nextStartA-startA个，在剩余的A和B里寻找第k-nextStartA + startA
        if(A[nextStartA] < B[nextStartB]) {
            return find_kth(A, nextStartA, m, B, startB, n, k - nextStartA + startA);
        }

        // 如果A[nextStartA] > B[nextStartB]，去掉B的前nextStartB-startB个，在剩余的B和A里寻找第k-nextStartB + startB
        else if (A[nextStartA] > B[nextStartB]) {
            return find_kth(A, startA, m, B, nextStartB, n, k - nextStartB + startB);
        }

        // 如果A[nextStartA] == B[nextStartB]，直接返回
        else {
            return A[nextStartA];
        }
    }
}
```

> 当要求中位数时，代码如下：

```java
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
```
