package com.ramsey.updater;

public class PostUpdateTask {
    public static void main(String[] args) {
        long pid = Long.parseLong(args[0]);
        String scriptPath = args[1];

        ProcessHandle handle = ProcessHandle.of(pid).orElseThrow(() -> new RuntimeException("Process not found"));

        handle.onExit().thenRun(() -> {
            try {
                Runtime.getRuntime().exec("start cmd /c \"echo dw & pause\"" + scriptPath);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


    }
}
