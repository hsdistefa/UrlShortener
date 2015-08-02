package com.urlshortener;


/**
 * Denotes which stage the server is running
 */
public enum Stage {
    INTEG,  // single box integration tests
    ADHOC,  // adhoc single-box tests
    CLUSTER,  // test cluster
    PROD,  // production
}
