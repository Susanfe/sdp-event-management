package ch.epfl.sweng.eventmanager.test.repository;

import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;

/**
 * This interface handles a DatabaseReferenceWrapper, answers to it if appropriate, does nothing in the other cases
 * @author Louis Vialar
 */
public interface DbRefHandler {
    Object getValue(DatabaseReferenceWrapper wrapper);
}
