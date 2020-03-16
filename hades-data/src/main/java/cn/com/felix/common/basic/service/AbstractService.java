package cn.com.felix.common.basic.service;

import cn.com.felix.common.basic.domain.AbstractDomain;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public interface AbstractService<D extends AbstractDomain, ID extends Serializable> {

    D findById(ID id);

    void deleteById(ID id);

    Page<D> findByPage(int pageNumber, int pageSize);
}
