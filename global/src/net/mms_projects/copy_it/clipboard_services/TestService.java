package net.mms_projects.copy_it.clipboard_services;

import net.mms_projects.copy_it.ClipboardListener;

import java.util.concurrent.Executor;

public class TestService implements CopyServiceInterface, PasteServiceInterface {

    public static String SERVICE_NAME = "test";
    protected ClipboardListener listener;
    private String testContent = "bla";
    private Executor executor;

    public TestService(ClipboardListener listener) {
        this.listener = listener;
    }

    @Override
    public void requestSet(String content) {
        this.setContent(content);
        this.listener.onClipboardContentChange(this.testContent);
    }

    @Override
    public void requestGet() {
        System.out.println("Requested content: " + this.testContent);
        this.listener.onContentGet(this.getContent());
    }

    @Override
    public String getContent() {
        return this.testContent;
    }

    @Override
    public void setContent(String content) {
        this.testContent = content;
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public Executor getExecutor() {
        return this.executor;
    }

    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void activateCopy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deactivateCopy() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCopyActivated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void activatePaste() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deactivatePaste() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isPasteActivated() {
        // TODO Auto-generated method stub
        return false;
    }

}
