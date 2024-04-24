module edpn.backend.trade {
    requires static lombok;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.jdbc;
    requires spring.tx;
    requires spring.web;
    requires spring.kafka;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires org.slf4j;
    requires edpn.backend.util;
    requires edpn.backend.mybatisutil;
    requires edpn.backend.messageprocessorlib;
    requires org.mybatis.spring;
    requires kafka.clients;
    requires org.mybatis;
    requires jakarta.validation;
    requires io.swagger.v3.oas.annotations;
    requires liquibase.core;
    requires spring.retry;
    requires spring.core;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports io.edpn.backend.trade.adapter.web;
    exports io.edpn.backend.trade.application.domain;
    exports io.edpn.backend.trade.application.domain.filter;
    exports io.edpn.backend.trade.application.port.incomming.validatedcommodity;
    exports io.edpn.backend.trade.application.port.incomming.locatecommodity;
    exports io.edpn.backend.trade.application.port.incomming.commoditymarketinfo;
    exports io.edpn.backend.trade.adapter.web.dto.object;
    exports io.edpn.backend.trade.adapter.web.dto.filter;
    exports io.edpn.backend.trade.adapter.web.dto.object.mapper;
    exports io.edpn.backend.trade.adapter.web.dto.filter.mapper;
    exports io.edpn.backend.trade.application.domain.intermodulecommunication;
    exports io.edpn.backend.trade.application.port.incomming.locatetraderoute;
    exports io.edpn.backend.trade.application.port.incomming.station;
    exports io.edpn.backend.trade.application.port.incomming.system;
    exports io.edpn.backend.trade.adapter.web.validator;
    
}
