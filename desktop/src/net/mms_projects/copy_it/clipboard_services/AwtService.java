package net.mms_projects.copy_it.clipboard_services;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.concurrent.Executor;

import net.mms_projects.copy_it.ClipboardListener;

public class AwtService implements ClipboardServiceInterface, ClipboardOwner {

    private Executor executor;
    private ClipboardListener listener;

    public AwtService(ClipboardListener listener) {
        this.listener = listener;
    }

    @Override
    public void activateCopy() {
    }

    @Override
    public void deactivateCopy() {
    }

    @Override
    public boolean isCopyActivated() {
        return false;
    }

    @Override
    public void requestSet(final String content) {
        this.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                setContent(content);
            }
        });
    }

    @Override
    public void activatePaste() {
    }

    @Override
    public void deactivatePaste() {
    }

    @Override
    public boolean isPasteActivated() {
        return false;
    }

    @Override
    public String getContent() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                        contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                //highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void setContent(String content) {
        StringSelection stringSelection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
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
    public String getServiceName() {
        return "awt";
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }
}
