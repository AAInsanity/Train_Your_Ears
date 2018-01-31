package com.example.app3;

/**
 * Created by dengyitong on 2018/1/29.
 */

public class KeyValuePair<Key, Value> {

    private Key key;
    private Value value;

    public KeyValuePair(Key k, Value v) {
        this.key = k;
        this.value = v;
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    public void setValue( Value v ) {
        this.value = v;
    }

    public void setKey( Key k ) {
        this.key = k;
    }

    public String toString() {
        String str = "key: " + key + ", value: " + value;
        return str;
    }

    public static void main (String[] Args) {
        KeyValuePair<Integer, String> k1 = new KeyValuePair<Integer, String>(7, "a");
        System.out.println(k1.toString());
        System.out.println(k1.getKey());
        System.out.println(k1.getValue());
        k1.setValue("bazinga");
        System.out.println(k1.toString());
    }

}
