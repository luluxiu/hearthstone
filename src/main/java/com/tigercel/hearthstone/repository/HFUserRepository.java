package com.tigercel.hearthstone.repository;

import com.tigercel.hearthstone.model.HFUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by somebody on 2016/8/2.
 */
@Repository
@Transactional
public interface HFUserRepository extends JpaRepository<HFUser, Long> {
    HFUser findByName(String name);
}
