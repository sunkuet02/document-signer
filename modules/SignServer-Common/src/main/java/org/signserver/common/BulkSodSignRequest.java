package org.signserver.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sun on 10/13/16.
 */
public class BulkSodSignRequest extends ProcessRequest implements ISignRequest {
    private static final Logger LOG = Logger.getLogger(SODSignRequest.class);

    private static final long serialVersionUID = 1L;
    private int requestID;
    private Map<String, Map<Integer, byte[]> >bulkDataGroups;
    private String ldsVersion;
    private String unicodeVersion;

    public BulkSodSignRequest() {
    }

    public BulkSodSignRequest(int requestID, Map<String, Map<Integer, byte[]> >bulkDataGroups, String ldsVersion, String unicodeVersion) {
        super();
        this.requestID = requestID;
        this.bulkDataGroups = bulkDataGroups;
        this.ldsVersion = ldsVersion;
        this.unicodeVersion = unicodeVersion;
    }

    public Map<String, Map<Integer, byte[]>> getBulkDataGroups() {
        return bulkDataGroups;
    }

    public String getLdsVersion() {
        return ldsVersion;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUnicodeVersion() {
        return unicodeVersion;
    }
    
    

    public BulkSodSignRequest(int requestID, Map<String, Map<Integer, byte[]> >bulkDataGroups) {
        this(requestID, bulkDataGroups, null, null);
    }

    public void parse(DataInput in) throws IOException {
        in.readInt();
        this.requestID = in.readInt();
        int mapSize = in.readInt();
        this.bulkDataGroups = new HashMap<String, Map<Integer, byte[]>>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            int documentIdSize = in.readInt();
            byte [] documentId = new byte[documentIdSize];
            in.readFully(documentId);

            int dataGroupSize = in.readInt();
            Map<Integer, byte[] > dataGroupHashes = new HashMap<Integer, byte[]>(dataGroupSize);
            for(int j = 0; j< dataGroupSize; i++ ) {
                int key = in.readInt();
                int valueSize = in.readInt();
                byte[] value = new byte[valueSize];
                in.readFully(value);
                dataGroupHashes.put(key, value);
            }
            bulkDataGroups.put(documentId.toString(), dataGroupHashes);
        }
        try {
            ldsVersion = in.readUTF();
            if (ldsVersion.isEmpty()) {
                ldsVersion = null;
            }
            try {
                unicodeVersion = in.readUTF();
                if (unicodeVersion.isEmpty()) {
                    unicodeVersion = null;
                }
            } catch (EOFException ignored) {
                LOG.debug("No unicode version in request");
            }
        } catch (EOFException ignored) {
            LOG.debug("No LDS version in request");
        }
    }

    public void serialize(DataOutput out) throws IOException {
        out.writeInt(RequestAndResponseManager.REQUESTTYPE_BULKSODSIGNREQUEST);
        out.writeInt(this.requestID);
        out.writeInt(this.bulkDataGroups.size());
        for(Map.Entry<String, Map<Integer,byte[]> >bulkDataGroup : bulkDataGroups.entrySet()) {
            out.writeInt(bulkDataGroup.getKey().getBytes().length);
            out.write(bulkDataGroup.getKey().getBytes());
            out.writeInt(bulkDataGroup.getValue().size());
            for(Map.Entry<Integer, byte[]> entry : bulkDataGroup.getValue().entrySet()) {
                out.writeInt(entry.getKey().intValue());
                out.writeInt(entry.getValue().length);
                out.write(entry.getValue());
            }
        }
        out.writeUTF(ldsVersion == null ? "" : ldsVersion);
        out.writeUTF(unicodeVersion == null ? "" : unicodeVersion);
    }

    @Override
    public int getRequestID() {
        return requestID;
    }

    @Override
    public Object getRequestData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
