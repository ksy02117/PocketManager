package com.example.pocketmanager.network;

public interface APIListener<T> {
    public void getResult(T success);
}
