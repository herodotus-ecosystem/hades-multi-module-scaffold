package cn.com.felix.weapp.repository;

import cn.com.felix.weapp.domain.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WxUserRepository extends JpaRepository<WxUser, String> {

    WxUser findByOpenId(String openId);
}
