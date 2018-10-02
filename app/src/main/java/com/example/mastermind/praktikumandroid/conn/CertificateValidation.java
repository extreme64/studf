package com.example.mastermind.praktikumandroid.conn;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;



/**
 * Created by Mastermind on 20-Jul-16.
 */
public class CertificateValidation {

    private CertificateFactory cf;
    private InputStream caInput;
    private  String keyStoreType;
    private KeyStore keyStore;
    private  String tmfAlgorithm;
    private TrustManagerFactory tmf;
    public SSLContext SSLcontext;
    private SSLContext context = null;



    public CertificateValidation(AssetManager am, String cert_file_path){

        CertificateFactory cf = null;
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        SSLContext SSLcontext = null;

        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {   e.printStackTrace();  }

        InputStream caInput;

        InputStream inputString = null;
        try {
            inputString = am.open("LetsEncryptAuthorityX3.crt");
        } catch (IOException e) {  e.printStackTrace(); }

        caInput = new BufferedInputStream(inputString);
        Certificate ca=null;
        try {
            ca = cf.generateCertificate(caInput);
        }  catch (CertificateException e) {
            try {
                caInput.close();
            } catch (IOException e1) {  e1.printStackTrace();   }
        }

        // Create a KeyStore containing our trusted CAs
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (KeyStoreException e) {  e.printStackTrace(); }

        try {
            try {
                keyStore.load(null, null);
            } catch (IOException e) {  e.printStackTrace(); }
        } catch (NoSuchAlgorithmException e) { e.printStackTrace();
        } catch (CertificateException e) {  e.printStackTrace(); }
        try {  keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e) {   e.printStackTrace();   }

        // Create a TrustManager that trusts the CAs in our KeyStore
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {   e.printStackTrace();  }

        try {
            tmf.init(keyStore);
        } catch (KeyStoreException e) {  e.printStackTrace();  }

        // Create an SSLContext that uses our TrustManager
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {  e.printStackTrace(); }

        try {
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {  e.printStackTrace(); }
    }


    public SSLContext getSSLcontext() {
        return context;
    }
}
