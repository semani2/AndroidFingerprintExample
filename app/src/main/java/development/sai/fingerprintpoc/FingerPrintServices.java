package development.sai.fingerprintpoc;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyProperties;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by sai on 1/11/16.
 */
public class FingerPrintServices {

    private final Context mContext;

    public FingerPrintServices(Context context){
        mContext = context;
    }

    public FingerprintManager getFingerprintManager(){
        return mContext.getSystemService(FingerprintManager.class);
    }

    public KeyStore getKeystore(){
        try{
            return KeyStore.getInstance("AndroidKeyStore");
        }
        catch(KeyStoreException e){
            throw new RuntimeException("Failed to get an instance of keystore", e);
        }
    }

    public KeyGenerator getKeyGenerator(){
        try{
            return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        }
        catch(NoSuchAlgorithmException | NoSuchProviderException e){
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }
    }

    public Cipher getCipher(KeyStore keyStore) {
        try {
            return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
    }
}
