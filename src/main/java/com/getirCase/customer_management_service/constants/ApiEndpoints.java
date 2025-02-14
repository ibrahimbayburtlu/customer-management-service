package com.getirCase.customer_management_service.constants;

public final class ApiEndpoints {
    private ApiEndpoints() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String CUSTOMER_BASE = "/api/customers";
    public static final String GET_CUSTOMER = "/{id}";
    public static final String CREATE_CUSTOMER = "";
    public static final String DELETE_CUSTOMER = "/{id}";
    public static final String UPDATE_CUSTOMER = "/{id}";
    public static final String UPDATE_TIER = "/{id}/tier";
}
