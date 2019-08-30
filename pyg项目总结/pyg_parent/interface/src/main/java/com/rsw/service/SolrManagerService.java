package com.rsw.service;

public interface SolrManagerService {

    void saveItemByGoodId(Long id);

    void deleteItemFromSolr(Long id);
}
