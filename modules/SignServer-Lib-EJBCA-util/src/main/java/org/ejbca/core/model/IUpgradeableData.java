/*************************************************************************
 *                                                                       *
 *  EJBCA: The OpenSource Certificate Authority                          *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public           *
 *  License as published by the Free Software Foundation; either         *
 *  version 2.1 of the License, or any later version.                    *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/
 
package org.ejbca.core.model;

/**
 * IUpgradableData is an interface intended to be used by classed saved to database as BLOB. Every
 * such class should put all it's data in one of the Collection data structures and it will only
 * be the collection saved to the database. This is to avoid serialization problems when upgrading
 * the class.
 *
 * @version $Id: IUpgradeableData.java 5585 2008-05-01 20:55:00Z anatom $
 */
public interface IUpgradeableData {
    /**
     * Should return a constant containing the latest available version of the class.
     *
     * @return DOCUMENT ME!
     */
    public abstract float getLatestVersion();

    /**
     * Function returning the current version of the class data.
     *
     * @return DOCUMENT ME!
     */
    public abstract float getVersion();

    /**
     * Function sending the data to be saved to the database.
     *
     * @return DOCUMENT ME!
     */
    public abstract Object saveData();

    /**
     * Function loading saved data into to data structure.
     */
    public abstract void loadData(Object data);

    /**
     * Function that should handle the update of the data in the class so it's up to date with the
     * latest version.
     */
    public abstract void upgrade();
}
