package search;

public class BinarySearchMissing {
    //PRE: l>=0 && r>=0 && l<=r && arr[i]!=null
    // forall i=l..r - 1: arr[i-1]<=arr[i]
    //POST: arr[l`] == x or x not in arr
    public static int iterativeBinarySearch(int arr[], int l, int r, int x) {
        //PRE: l>=0 && r>=0 && l<=r && arr!=null
        // forall i=l..r - 1: arr[i-1]<=arr[i]
        //POST: l`>r && (arr[l`] == x or x not in arr)
        while (l <= r) {
            //PRE: l>=0 && r>=0 && l<=r && arr!=null
            //POST: m=(l+r)/2 && m>=0 && l<=m<=r && arr!=null
            int m = (l + r) / 2;
            //PRE: m>=0 && arr!=null
            //POST: l>=0 && r>=0 && (r==m-1 || l==m+1) && (a[l] >= x >= a[r] || x not in arr)
            if (arr[m] >= x) r = m - 1;
            else l = m + 1;
        }
        if (arr.length == 0) return -1;
        if (l >= arr.length || l < 0) return -l - 1;
        if (arr[l] != x) return -l - 1;
        return l;
    }

    //PRE: l>=0 && r>=0 && l<=r && arr!=null
    // forall i=l..r - 1: arr[i-1]<=arr[i]
    //POST: arr[l`] == x or x not in arr
    public static int recusiveBinarySearch(int arr[], int l, int r, int x) {
        if (l > r) {
            //PRE: l>=0 && r>=0 && l>r && arr!=null
            //POST: arr[l`] == x or x not in arr
            if (arr.length == 0) return -1;
            if (l >= arr.length || l < 0) return -l - 1;
            if (arr[l] != x) return -l - 1;
            return l;
        }
        //PRE: l>=0 && r>=0 && l<=r && arr!=null
        //POST: m=(l+r)/2 && m>=0 && l<=m<=r && arr!=null
        int m = (l + r) / 2;
        //PRE: m=(l+r)/2 && m>=0 && arr!=null
        //POST: forall i=l..r - 1: if(arr[i]>=x): l = min(l, i)
        if (arr[m] >= x) {
            //PRE: m=(l+r)/2 && m>=0 && r = m-1 && r`<=m-` && arr!=null
            //POST: arr[l`] == x or x not in arr
            return recusiveBinarySearch(arr, l, m - 1, x);
        } else {
            //PRE: m=(l+r)/2 && m>=0 && l = m+1 && arr!=null
            //POST: arr[l`] == x or x not in arr && l` >= m + 1
            return recusiveBinarySearch(arr, m + 1, r, x);
        }
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int n = args.length - 1;
        int arr[] = new int[n];
        for (int i = 1; i <= n; i++) {
            arr[i - 1] = Integer.parseInt(args[i]);
        }
        System.out.println(iterativeBinarySearch(arr, 0, n - 1, x));
        //System.err.println(recusiveBinarySearch(arr, 0, n - 1, x));
    }
}
