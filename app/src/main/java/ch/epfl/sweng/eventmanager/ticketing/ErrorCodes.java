package ch.epfl.sweng.eventmanager.ticketing;

/**
 * @author Louis Vialar
 */
public enum ErrorCodes {
    DATABASE("error.db_error"),
    UNKNOWN("error.exception"),
    AUTH_MISSING("error.no_auth_token"),
    PERMS_MISSING("error.no_permissions"),
    NOT_FOUND("error.not_found"),
    /**
     * The product is not allowed by this scanner
     */
    PRODUCT_NOT_ALLOWED("error.product_not_allowed"),
    /**
     * The provided barcode has already been scanned
     */
    ALREADY_SCANNED("error.ticket_already_scanned")

    ;
    private final String code;

    ErrorCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
