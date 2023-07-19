package io.edpn.backend.exploration.domain.service;

public interface RequestDataService<T> {

    boolean isApplicable(T t);

    void request(T t);
}
