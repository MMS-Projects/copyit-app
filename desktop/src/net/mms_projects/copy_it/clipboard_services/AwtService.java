package net.mms_projects.copy_it.clipboard_services;

import net.mms_projects.copy_it.ClipboardListener;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.concurrent.Executor;

public class AwtService implements CopyServiceInterface, PasteServiceInterface, ClipboardOwner {

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
    public void requestGet() {
        this.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String data = getContent();

                listener.onContentGet(data);
            }
        });
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
