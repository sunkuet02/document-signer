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
package org.signserver.module.tsa;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ejbca.util.IPatternLogger;
import org.signserver.server.log.AdminInfo;
import org.signserver.server.log.EjbcaPatternLogger;
import org.signserver.server.log.IWorkerLogger;
import org.signserver.server.log.WorkerLoggerException;

/**
 * Default audit logger for TimeStampSignerS.
 *
 * @version $Id: DefaultTimeStampLogger.java 5160 2014-09-11 15:19:42Z netmackan $
 */
public class DefaultTimeStampLogger implements IWorkerLogger {

    private Pattern pattern;
    private String orderString;
    private static final Logger ACCOUNTLOG =
            Logger.getLogger(IWorkerLogger.class);
    private String logDateFormat;
    private String timeZone;
    private Level logLevel;

    private static final String DEFAULT_LOGPATTERN =
            "\\$\\{(.+?)\\}";

    private static final String DEFAULT_LOGORDER =
            "AUDIT; DefaultTimeStampLogger; "
                + "LOG_ID: ${LOG_ID}; "
                + "CLIENT_IP: ${CLIENT_IP}; "
                + "REQUEST_FULLURL: ${REQUEST_FULLURL}; "
                + "RequestTime: ${LOG_TIME}; "
                + "ResponseTime: ${REPLY_TIME}; "
                + "TimeStamp: ${TSA_TIME}; "
                + "TimeSource: ${TSA_TIMESOURCE}; "
                + "PKIStatus: ${TSA_PKISTATUS}; "
                + "PKIFailureInfo: ${TSA_PKIFAILUREINFO}; "
                + "SerialNumber: ${TSA_SERIALNUMBER}; "
                + "TSA_POLICYID: ${TSA_POLICYID}; "
                + "SIGNER_CERT_SERIALNUMBER: ${SIGNER_CERT_SERIALNUMBER}; "
                + "SIGNER_CERT_ISSUERDN: ${SIGNER_CERT_ISSUERDN}; "
                + "TIMESTAMPREQUEST_ENCODED: ${TSA_TIMESTAMPREQUEST_ENCODED}; "
                + "TSA_TIMESTAMPRESPONSE_ENCODED: ${TSA_TIMESTAMPRESPONSE_ENCODED}; "
                + "ARCHIVE_IDS: ${ARCHIVE_IDS}; "
                + "PURCHASED: ${PURCHASED}; "
                + "TSA_EXCEPTION: ${TSA_EXCEPTION}; "
                + "EXCEPTION: ${EXCEPTION}";

    private static final String DEFAULT_LOGDATEFORMAT =
            "yyyy-MM-dd:HH:mm:ss:z";

    private static final String DEFAULT_LOGTIMEZONE = "GMT";
    
    private static final String DEFAULT_LOGLEVEL = "INFO";

    public DefaultTimeStampLogger() {
    }

    @Override
    public void init(Properties properties) {
        
        this.pattern = Pattern.compile(properties.getProperty("LOGPATTERN",
                DEFAULT_LOGPATTERN));
        this.orderString = properties.getProperty("LOGORDER", DEFAULT_LOGORDER);
        this.logDateFormat = properties.getProperty("LOGDATEFORMAT", DEFAULT_LOGDATEFORMAT);
        this.timeZone = properties.getProperty("LOGTIMEZONE", DEFAULT_LOGTIMEZONE);
        this.logLevel = Level.toLevel(properties.getProperty("LOGLEVEL_DEFAULT",
        		DEFAULT_LOGLEVEL), Level.INFO);
    }

    @Override
    public void log(final AdminInfo adminInfo, Map<String, String> entries) throws WorkerLoggerException {
        final IPatternLogger pl = new EjbcaPatternLogger(this.pattern.matcher(
                this.orderString), this.orderString, ACCOUNTLOG,
                this.logDateFormat, this.timeZone, this.logLevel);

        // TODO: Do a new version of pattern logger instead of this copying
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            pl.paramPut(entry.getKey(), entry.getValue());
        }
        pl.writeln();
        pl.flush();
    }

    @Override
    public void setEjbs(Map<Class<?>, ?> ejbs) {
        // NO-OP for this implementation        
    }

}
