package com.submit.temp;

class Solution {
    public int prefixCount(String[] words, String pref) {
        int ans = 0;
        for(String str: words){
            if(str.startsWith(pref))
                ans++;
        }
        return ans;
    }
}