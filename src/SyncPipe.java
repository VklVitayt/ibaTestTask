import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class SyncPipe implements Runnable {
    private final OutputStream ostrm_;
    private final InputStream istrm_;
    
    public SyncPipe(InputStream istrm, OutputStream ostrm) throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("file.encoding","UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null,null);
        istrm_ = istrm;
        ostrm_ = ostrm;
    }

    public void run() {
        System.setProperty("file.encoding","UTF-8");
        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        charset.setAccessible(true);
        try {
            charset.set(null,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try
        {
            final byte[] buffer = new byte[1024];
            for (int length = 0; (length = istrm_.read(buffer)) != -1; )
            {
                ostrm_.write(buffer, 0, length);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
