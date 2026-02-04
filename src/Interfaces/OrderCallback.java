package Interfaces;

@FunctionalInterface
public interface OrderCallback {
    void onSuccess(String message);
}