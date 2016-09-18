package com.tigercel.hearthstone.repository;

import com.tigercel.hearthstone.model.app.HFApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by somebody on 2016/8/12.
 */
@Repository
@Transactional
public interface AppRepository extends JpaRepository<HFApp, Long> {

}
