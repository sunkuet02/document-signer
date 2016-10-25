/*************************************************************************
 *                                                                       *
 *  SignServer: The OpenSource Automated Signing Server                  *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public           *
 *  License as published by the Free Software Foundation; either         *
 *  version 2.1 of the License, or any later version.                    *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/
package org.signserver.common;

import org.ejbca.util.CertTools;
import org.signserver.server.archive.Archivable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.*;

/**
 * A generic work response class implementing the minimal required functionality.
 * <p>
 * Could be used for TimeStamp Responses.
 *
 * @author philip
 * @version $Id: GenericSignResponse.java 2841 2012-10-16 08:31:40Z netmackan $
 */
public class GenericBulkSignResponse extends ProcessResponse {

    private static final long serialVersionUID = 3L;
    protected int tag = RequestAndResponseManager.RESPONSETYPE_GENERICSIGNRESPONSE;
    private int requestID;
    private ProcessedSODDataList processedSODDataList;
    private NotProcessedSodDataList notProcessedSodDataList;
    private transient Certificate signerCertificate;
    private byte[] signerCertificateBytes;


    public GenericBulkSignResponse() {
    }

    /**
     * Creates a GenericWorkResponse, works as a simple VO.
     *
     * @see ProcessRequest
     */
    public GenericBulkSignResponse(int requestID, ProcessedSODDataList processedData, NotProcessedSodDataList notProcessedSodDataList, Certificate signerCertificate) {
        try {
            this.requestID = requestID;
            this.processedSODDataList = processedData;
            this.notProcessedSodDataList = notProcessedSodDataList;
            this.signerCertificate = signerCertificate;
            this.signerCertificateBytes = signerCertificate == null ? null
                    : signerCertificate.getEncoded();
        } catch (CertificateEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return the request ID
     */
    public int getRequestID() {
        return requestID;
    }

    public Certificate getSignerCertificate() {
        if (signerCertificate == null && signerCertificateBytes != null) {
            try {
                signerCertificate = CertTools.getCertfromByteArray(
                        signerCertificateBytes);
            } catch (CertificateException ex) {
                throw new RuntimeException(ex);
            }
        }
        return signerCertificate;
    }

    public ProcessedSODDataList getProcessedSODDataList() {
        return processedSODDataList;
    }

    public NotProcessedSodDataList getNotProcessedSodDataList() {
        return notProcessedSodDataList;
    }

    /**
     * @return the processedData
     */



    public void parse(DataInput in) throws IOException {
        in.readInt();
        this.requestID = in.readInt();

        int certSize = in.readInt();
        if (certSize != 0) {
            byte[] certData = new byte[certSize];
            in.readFully(certData);
            try {
                this.signerCertificate = CertTools.getCertfromByteArray(certData);
            } catch (CertificateException e) {
                try {
                    throw new IOException(e.getMessage()).initCause(e);
                } catch (Throwable e1) {
                    throw new IOException(e.getMessage());
                }
            }
        }

        int documentSize = in.readInt();
        SODData processedSodData = new SODData();
        List<SODData> sodDataList = new ArrayList<SODData>();
        processedSODDataList = new ProcessedSODDataList();
        for (int i = 0; i < documentSize; i++) {
            int documentIDSize = in.readInt();
            byte[] documentID = new byte[documentIDSize];
            in.readFully(documentID);

            int dataSize = in.readInt();
            byte[] sodData = new byte[dataSize];
            in.readFully(sodData);

            processedSodData.setDocumentID(documentID.toString());
            processedSodData.setSodData(sodData);
            sodDataList.add(processedSodData);
        }
        processedSODDataList.setProcessedDataList(sodDataList);

        int notProcessedDataSize = in.readInt();
        List<NotProcessedSodData> notProcessedDataList = new ArrayList<NotProcessedSodData>();
        NotProcessedSodData notProcessedSodData = new NotProcessedSodData();
        notProcessedSodDataList = new NotProcessedSodDataList();
        for (int i = 0 ; i< notProcessedDataSize ; i++ ) {
            int documentIDSize = in.readInt();
            byte[] documentID = new byte[documentIDSize];
            in.readFully(documentID);

            int messageSize = in.readInt();
            byte [] message = new byte[messageSize];
            in.readFully(message);

            notProcessedSodData.setDocumentID(documentID.toString());
            notProcessedSodData.setExceptionMessage(message.toString());
            notProcessedDataList.add(notProcessedSodData);
        }
        notProcessedSodDataList.setNotProcessedDataList(notProcessedDataList);
    }

    public void serialize(DataOutput out) throws IOException {
        out.writeInt(tag);
        out.writeInt(this.requestID);
        if (signerCertificate != null) {
            try {
                byte[] certData = this.signerCertificate.getEncoded();
                out.writeInt(certData.length);
                out.write(certData);
            } catch (CertificateEncodingException e) {
                try {
                    throw new IOException(e.getMessage()).initCause(e);
                } catch (Throwable e1) {
                    throw new IOException(e.getMessage());
                }
            }
        } else {
            out.writeInt(0);
        }
        List<SODData> processedDataList =  processedSODDataList.getProcessedDataList();
        out.writeInt(processedDataList.size());

        for (SODData data: processedDataList) {
            out.writeInt(data.getDocumentID().getBytes().length);
            out.write(data.getDocumentID().getBytes());
            out.writeInt(data.getSodData().length);
            out.write(data.getSodData());
        }
        List<NotProcessedSodData> notProcessedDataList = notProcessedSodDataList.getNotProcessedDataList();
        out.writeInt(notProcessedDataList.size());
        for (NotProcessedSodData notPSdata : notProcessedDataList) {
            out.writeInt(notPSdata.getDocumentID().getBytes().length);
            out.write(notPSdata.getDocumentID().getBytes());

            out.writeInt(notPSdata.getExceptionMessage().getBytes().length);
            out.write(notPSdata.getExceptionMessage().getBytes());
        }
    }
}
