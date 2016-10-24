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
package org.signserver.admin.gui;

/**
 * Table model for archive query conditions.
 * 
 * @author Marcus Lundblad
 * @version $Id: ArchiveConditionsTableModel.java 4972 2014-08-15 13:42:58Z malu9369 $
 */
public class ArchiveConditionsTableModel extends ConditionsTableModel {

    @Override
    protected QueryColumn getColumnFromName(final String name) {
        for (final ArchiveColumn col : ArchiveColumn.values()) {
            if (col.getName().equals(name)) {
                return col;
            }
        }
        throw new IllegalArgumentException("Column not found: " + name);
    }
}
