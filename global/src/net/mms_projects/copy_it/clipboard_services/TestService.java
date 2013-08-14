package net.mms_projects.copy_it.clipboard_services;

import java.util.concurrent.Executor;

import net.mms_projects.copy_it.ClipboardListener;
import net.mms_projects.copy_it.listeners.EnabledListener;

public class TestService implements ClipboardServiceInterface {

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
    public void enable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addEnabledListener(EnabledListener listener) {
        // TODO Auto-generated method stub
        
    }

}
