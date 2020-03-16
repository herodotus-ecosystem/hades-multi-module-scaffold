package cn.com.felix.common.basic.service;

import cn.com.felix.common.basic.domain.AbstractDomain;

import java.io.Serializable;

public abstract class BaseService<D extends AbstractDomain, ID extends Serializable> implements AbstractService<D, ID> {

}
