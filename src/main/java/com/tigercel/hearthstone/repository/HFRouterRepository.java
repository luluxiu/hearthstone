package com.tigercel.hearthstone.repository;

import com.tigercel.hearthstone.model.router.HFDevRouter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by somebody on 2016/8/10.
 */
@Repository
@Transactional
public interface HFRouterRepository extends JpaRepository<HFDevRouter, Long> {

    HFDevRouter findByUdid(String mac);
}
